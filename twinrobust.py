import json
import logging
import os
import shutil
import subprocess
import sys
import time
from concurrent.futures import ThreadPoolExecutor, as_completed
from datetime import datetime

import dotenv
import pandas as pd
import requests
from pandas import DataFrame
from sshtunnel import SSHTunnelForwarder

OUTPUT_DATA_PATH = "./SimulatorDataOutput"
PAYLOAD_PATH = "./SimulatorPayloads"
TWINROBUST_PATH = "./twinRobust"
LIB_MODEL_PATH = "./KnowledgeBase/Model"
API_BASE_URL = "http://localhost:1337"

logging.basicConfig(level=logging.INFO, format="%(asctime)s [%(levelname)s] %(name)s: %(message)s")
dotenv.load_dotenv()
class Simulation:
    def __init__(self, simulation):
        self.simulation = simulation
        self.simulatorHost = os.getenv("SIM_HOST")
        self.simulatorPort = int(os.getenv("SIM_PORT"))

    def run_simulation(self):
        return self._createSimulation()

    def _createSimulation(self) -> DataFrame:
        result = self._call_api()
        return result
    def _call_api(self) -> DataFrame:
        simulation_id = self.start_simulation()
        try:
            while True:
                simulation_status = self.check_simulation_status(simulation_id)
                if simulation_status in ["done", "error", "failed"]:
                    break
                time.sleep(5)
            status_response = requests.get(f"http://{self.simulatorHost}:{self.simulatorPort}/check_status?id={simulation_id}")

            if status_response.status_code != 200:
                print(f"Error checking status: {status_response.status_code}")
                print(f"Response: {status_response.text}")
                return None

            results_response = requests.get(f"http://{self.simulatorHost}:{self.simulatorPort}/retrieve_results?id={simulation_id}")

            if results_response.status_code != 200:
                print(f"Error retrieving results: {results_response.status_code}")
                print(f"Response: {results_response.text}")
                return None

            results = results_response.json()
            return pd.DataFrame(results['microgrid_data'])

        except Exception as e:
            print(f"Error during simulation process: {str(e)}")
            return None

    def start_simulation(self):
        payload = self.simulation.to_simulator_dict()
        try:
            start_response = requests.post(f"http://{self.simulatorHost}:{self.simulatorPort}/start_simulation", json=payload)
            if start_response.status_code != 200:
                print(f"Error starting simulation: {start_response.status_code}")
                print(f"Response: {start_response.text}")
                return None

            simulation_data = start_response.json()
            simulation_id = simulation_data["id"]
            print(f"Simulation started with ID: {simulation_id}")

            return simulation_id

        except Exception as e:
            print(f"Error during simulation process: {str(e)}")
            return None


    def check_simulation_status(self,simulation_id):
        status_response = requests.get(f"http://{self.simulatorHost}:{self.simulatorPort}/check_status?id={simulation_id}")
        status_data = status_response.json()
        simulation_status = status_data["status"]

        if simulation_status == "done":
            print(f"Simulation {simulation_id} completed successfully!")
        elif simulation_status == "running":
            print(f"Simulation {simulation_id} still running!")
        else:
            print(f"Simulation failed with status: {simulation_status}")
            error_message = status_data.get("error_message")
            if error_message:
                print(f"Error details: {error_message}")

        return simulation_status

class ExperimentManager:
    def __init__(self,data_output_path,url):
        self.paths = {}
        self.data_output_path = data_output_path
        self.url = url

    def add_simulation_payload(self, pair_name, sim_name, payload, isBaseline):
        print(f"Adding simulation payload for pair '{pair_name}', sim '{sim_name}', isBaseline={isBaseline}")
        if pair_name not in self.paths:
            self.paths[pair_name] = {"base": {}, "additional": {}}
        if isBaseline:
            self.paths[pair_name]["base"][sim_name] = payload
        else:
            self.paths[pair_name]["additional"][sim_name] = payload

    def run_simulations(self):
        dfs = []
        max_workers = min(6, os.cpu_count() or 1)

        with SSHTunnelForwarder(
                (os.getenv("SSH_HOST"), 22),
                ssh_username=os.getenv("SSH_USERNAME"),
                ssh_password=os.getenv("SSH_PASSWORD"),
                remote_bind_address=(os.getenv("SIM_HOST"), 1337),
                local_bind_address=('127.0.0.1', int(os.getenv("SIM_PORT")))
        ) as tunnel:
            tasks = [
                (pair_name, role, sim_name, payload)
                for pair_name, roles in self.paths.items()
                for role, runs in roles.items()
                for sim_name, payload in runs.items()
            ]

            with ThreadPoolExecutor(max_workers=max_workers) as executor:
                futures = {
                    executor.submit(self._run_single, pair_name, role, sim_name, payload): (pair_name, sim_name)
                    for pair_name, role, sim_name, payload in tasks
                }
                for future in as_completed(futures):
                    pair_name, sim_name = futures[future]
                    try:
                        dfs.append(future.result())
                    except Exception as e:
                        logging.error(f"Simulation '{sim_name}' in pair '{pair_name}' failed: {e}")

            time.sleep(2)

        if not dfs:
            raise ValueError("No simulation results to concatenate.")
        df = pd.concat(dfs, ignore_index=True)
        self.saveData(df)

    @staticmethod
    def _run_single(pair_name: str, role: str, sim_name: str, payload) -> pd.DataFrame:
        sim = Simulation(payload)
        result_df = sim.run_simulation()
        result_df["simulation_name"] = pair_name
        result_df["simulation_run"] = sim_name
        result_df["role"] = role
        # needed for the KPIs: violation rate and values per day
        data = payload.to_simulator_dict()
        result_df["grid_limit"] = data["simulation"]["grid"]["grid_limit"]
        result_df["timestep"] = data["simulation"]["initial_values"].get("TIMESTEP", 900)
        return result_df

    def get_output_path(self) -> str:
        date = datetime.now().strftime("%Y%m%d_%H%M%S")
        return f"{self.data_output_path}/{date}.csv"

    def saveData(self, df):
        path = self.get_output_path()
        os.makedirs(self.data_output_path, exist_ok=True)
        df.to_csv(path, index=False)
        logging.info(f"Saved results to {path}")
        self._save_kpi_summary(df, path)

    def _save_kpi_summary(self, df: pd.DataFrame, main_path: str):
        """Per run: cost and CO2 per day and the grid limit violation rate; every additional relative to its baseline."""
        rows = []
        for (pair_name, run), group in df.groupby(["simulation_name", "simulation_run"], sort=False):
            steps_per_day = 86400 / group["timestep"].iloc[0]
            rows.append({
                "simulation_name": pair_name,
                "simulation_run": run,
                "role": group["role"].iloc[0],
                "steps": len(group),
                "grid_import_cost_per_day": group["grid_import_cost"].mean() * steps_per_day,
                "grid_co2_production_per_day": group["grid_co2_production"].mean() * steps_per_day,
                "grid_limit_violation_rate": (group["grid_import"] > group["grid_limit"]).mean(),
            })
        summary = pd.DataFrame(rows)
        kpis = ["grid_import_cost_per_day", "grid_co2_production_per_day", "grid_limit_violation_rate"]

        relatives = []
        for pair_name, group in summary.groupby("simulation_name", sort=False):
            bases = group[group["role"] == "base"]
            if bases.empty:
                logging.error(f"Pair '{pair_name}': baseline result missing, no relative deviation")
                continue
            base = bases.iloc[0]
            for _, additional in group[group["role"] == "additional"].iterrows():
                relative = {"simulation_name": pair_name, "simulation_run": additional["simulation_run"], "role": "relative_deviation_%"}
                for kpi in kpis:
                    # a baseline of 0 has no relative deviation
                    relative[kpi] = round((additional[kpi] - base[kpi]) / base[kpi] * 100, 2) if base[kpi] != 0 else None
                relatives.append(relative)

        final = pd.concat([summary, pd.DataFrame(relatives)], ignore_index=True)
        # runs finish in any order; group by pair, baseline first
        order = {"base": 0, "additional": 1, "relative_deviation_%": 2}
        final = final.sort_values(["simulation_name", "role", "simulation_run"], key=lambda c: c.map(order) if c.name == "role" else c)
        kpi_path = main_path.replace(".csv", "_kpi_summary.csv")
        final.to_csv(kpi_path, index=False)
        logging.info(f"Saved KPI summary to {kpi_path}")


class JsonPayload:
    """A payload file of twinRobust; offers to_simulator_dict() like ExperimentManager expects."""

    def __init__(self, path):
        with open(path) as f:
            self.data = json.load(f)

    def to_simulator_dict(self):
        return self.data


def run_twinrobust(output_dir, model_paths):
    mvn = shutil.which("mvn")
    if mvn is None:
        sys.exit("Maven (mvn) not found on the PATH")
    given = [os.path.abspath(p) for p in model_paths]
    model = os.path.abspath(LIB_MODEL_PATH)
    inside = any(model == g or model.startswith(g.rstrip(os.sep) + os.sep) for g in given)
    paths = [os.path.abspath(output_dir)] + ([] if inside else [model]) + given
    for path in paths[1:]:
        if not os.path.exists(path):
            sys.exit(f"Not found: {path}")
    exec_args = " ".join(f'"{p}"' for p in paths)
    logging.info("Running twinRobust")
    result = subprocess.run([mvn, "-q", "-f", os.path.join(TWINROBUST_PATH, "pom.xml"), "compile",
                             "org.codehaus.mojo:exec-maven-plugin:3.5.0:java",
                             "-Dexec.mainClass=org.example.Main", "-Dexec.cleanupDaemonThreads=false",
                             f"-Dexec.args={exec_args}"])
    if result.returncode != 0:
        sys.exit(f"twinRobust failed with exit code {result.returncode}")


def main():
    if len(sys.argv) < 2:
        sys.exit("usage: python twinrobust.py <sysml file or directory> ...")
    directory = os.path.join(PAYLOAD_PATH, datetime.now().strftime("%Y%m%d_%H%M%S"))
    run_twinrobust(directory, sys.argv[1:])
    with open(os.path.join(directory, "experiments.json")) as f:
        experiments = json.load(f)

    manager = ExperimentManager(OUTPUT_DATA_PATH, API_BASE_URL)
    for experiment in experiments:
        pair = os.path.splitext(experiment["base"])[0]
        manager.add_simulation_payload(pair, pair, JsonPayload(os.path.join(directory, experiment["base"])), True)
        for condition in experiment["conditions"]:
            name = os.path.splitext(condition)[0]
            manager.add_simulation_payload(pair, name, JsonPayload(os.path.join(directory, condition)), False)
    manager.run_simulations()


if __name__ == "__main__":
    main()
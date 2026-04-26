from concurrent.futures import ThreadPoolExecutor, as_completed
from datetime import datetime, timedelta

import pandas as pd
import requests
from numpy import random
import json
import logging
from pandas import DataFrame
import os
import time
from sshtunnel import SSHTunnelForwarder
from src.MGModel.mgmodel import MGModel
from src.Simulator.SimulationRequest import Simulation
class ExperimentManager:

    def __init__(self,data_output_path,url):
        self.paths = {}
        self.data_output_path = data_output_path
        self.url = url

    def add_simulation_payload(self, pair_name, sim_name, payload: MGModel, isBaseline):
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
        kpi_cols = ["grid_import_cost", "grid_co2_production"]
        all_summaries = []

        for pair_name, group in df.groupby("simulation_name"):
            summary = group.groupby("simulation_run")[kpi_cols].mean().reset_index()
            summary.columns = ["simulation_run"] + [f"avg_{c}" for c in kpi_cols]
            summary.insert(0, "simulation_name", pair_name)

            counts = group.groupby("simulation_run").size().reset_index(name="count")
            summary = summary.merge(counts, on="simulation_run")

            role_map = group.drop_duplicates("simulation_run").set_index("simulation_run")["role"]
            baseline_name = next((n for n in role_map.index if role_map[n] == "base"), None)
            additional_name = next((n for n in role_map.index if role_map[n] == "additional"), None)

            if baseline_name and additional_name:
                baseline = summary[summary["simulation_run"] == baseline_name].iloc[0]
                additional = summary[summary["simulation_run"] == additional_name].iloc[0]

                relative = {"simulation_name": pair_name, "simulation_run": "relative_deviation_%"}
                for col in [f"avg_{c}" for c in kpi_cols]:
                    base_val = baseline[col]
                    add_val = additional[col]
                    relative[col] = round(((add_val - base_val) / base_val) * 100, 2) if base_val != 0 else None
                relative["count"] = round(
                    counts[counts["simulation_run"] == additional_name]["count"].values[0] /
                    counts[counts["simulation_run"] == baseline_name]["count"].values[0], 4
                )
                summary = pd.concat([summary, pd.DataFrame([relative])], ignore_index=True)

            all_summaries.append(summary)

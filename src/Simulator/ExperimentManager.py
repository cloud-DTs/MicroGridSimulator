from datetime import datetime, timedelta

import pandas as pd
import requests
from numpy import random
import json
import logging
from pandas import DataFrame
import os
import time

from src.MGModel.mgmodel import MGModel
from src.Simulator.SimulationRequest import Simulation


class ExperimentManager:

    def __init__(self,data_output_path,url):
        self.paths = {}
        self.data_output_path = data_output_path
        self.url = url

    def add_simulation_payload(self, name, payload:MGModel):
        if name in self.paths:
            logging.warning(f"Path {name} already exists")
        self.paths[name]=payload

    def run_simulations(self):
        dfs = []
        for name, payload in self.paths.items():
            print(name)
            sim = Simulation(payload,self.url)
            result_df = sim.run_simulation()
            result_df["simulation_name"] = name
            dfs.append(result_df)

        df = pd.concat(dfs, ignore_index=True)
        self.saveData(df)

    def get_output_path(self) -> str:
        date = datetime.now().strftime("%Y%m%d_%H%M%S")
        names = "_".join(self.paths.keys())
        return f"{self.data_output_path}/{names}_{date}.csv"

    def saveData(self, df):
        path = self.get_output_path()
        os.makedirs(self.data_output_path, exist_ok=True)
        df.to_csv(path, index=False)
        logging.info(f"Saved results to {path}")
        self._save_kpi_summary(df, path)

    def _save_kpi_summary(self, df: pd.DataFrame, main_path: str):
        kpi_cols = ["grid_import_cost", "grid_co2_production"]

        summary = df.groupby("simulation_name")[kpi_cols].mean().reset_index()
        summary.columns = ["simulation_name"] + [f"avg_{c}" for c in kpi_cols]
        baseline_name = [n for n in df["simulation_name"].unique() if "base" in n.lower()]
        additional_name = [n for n in df["simulation_name"].unique() if "base" not in n.lower()]
        counts = df.groupby("simulation_name").size().reset_index(name="count")
        summary = summary.merge(counts, on="simulation_name")
        if baseline_name and additional_name:
            baseline = summary[summary["simulation_name"] == baseline_name[0]].iloc[0]
            additional = summary[summary["simulation_name"] == additional_name[0]].iloc[0]
            baseline_count = counts[counts["simulation_name"] == baseline_name[0]]["count"].values[0]
            additional_count = counts[counts["simulation_name"] == additional_name[0]]["count"].values[0]

            relative = {"simulation_name": "relative_deviation_%"}
            for col in [f"avg_{c}" for c in kpi_cols]:
                base_val = baseline[col]
                add_val = additional[col]
                relative[col] = round(((add_val - base_val) / base_val) * 100, 2) if base_val != 0 else None

            relative["count"] = round(additional_count / baseline_count, 4)
            summary = pd.concat([summary, pd.DataFrame([relative])], ignore_index=True)

        kpi_path = main_path.replace(".csv", "_kpi_summary.csv")
        summary.to_csv(kpi_path, index=False)
        logging.info(f"Saved KPI summary to {kpi_path}")

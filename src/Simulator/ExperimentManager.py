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

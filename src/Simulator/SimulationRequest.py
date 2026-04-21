import pandas as pd
import requests
from numpy import random
import json
import logging
from pandas import DataFrame
import os
import time
from datetime import datetime, timedelta
from src.MGModel.mgmodel import MGModel
import requests

class Simulation:
    def __init__(self, simulation:MGModel):
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

import os
import logging
from src.Collector.collector import collect_all_simulation_config_files
from src.MGModel.mgmodel import MGModel
from src.Simulator.ExperimentManager import ExperimentManager
INPUT_PATH = "DigitalTwinProfileSysMLv2/output/"
STANDARD_IMPORT_PREFIX = "simulation"
ST_TWIN_NAME = "StTwin"
THRESHOLDS_JSON_PATH = "./KnowledgeBase/Thresholds.json"
OUTPUT_DATA_PATH = "./SimulatorDataOutput"
API_BASE_URL = "http://localhost:1337"
logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s [%(levelname)s] %(name)s: %(message)s"
)



def main():
    res = collect_all_simulation_config_files(INPUT_PATH, ST_TWIN_NAME, STANDARD_IMPORT_PREFIX)
    manager = ExperimentManager(OUTPUT_DATA_PATH,API_BASE_URL)
    for simulation_file in res:
        print("Starting simulation")
        mg = MGModel(simulation_file,THRESHOLDS_JSON_PATH)
        manager.add_simulation_payload(mg.simulation.name,mg)

    manager.run_simulations()

if __name__ == '__main__':
    main()

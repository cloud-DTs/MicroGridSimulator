import os
import logging
from src.Collector.collector import collect_all_simulation_files
from src.MGModel.mgmodel import MGModel
from src.Simulator.ExperimentManager import ExperimentManager

INPUT_PATH = "DigitalTwinProfileSysMLv2/output/"
STANDARD_IMPORT_PREFIX = "simulation"
ST_TWIN_NAME = "StTwin"
OUTPUT_JSON_PATH = "./knowledgebase"
logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s [%(levelname)s] %(name)s: %(message)s"
)
def main():
    res = collect_all_simulation_files(INPUT_PATH,ST_TWIN_NAME,STANDARD_IMPORT_PREFIX)
    manager = ExperimentManager()
    for simulation_file in res:
        mg = MGModel(simulation_file)
        jsonPath = mg.to_simulator_json(OUTPUT_JSON_PATH)
        manager.add_path(jsonPath)


if __name__ == '__main__':
    main()

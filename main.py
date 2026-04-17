import os
import logging
from src.Collector.collector import collect_all_simulation_files
from src.MGModel.mgmodel import MGModel

INPUT_PATH = "DigitalTwinProfileSysMLv2/output/"
STANDARD_IMPORT_PREFIX = "simulation"
ST_TWIN_NAME = "StTwin"
logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s [%(levelname)s] %(name)s: %(message)s"
)
def main():
    res = collect_all_simulation_files(INPUT_PATH,ST_TWIN_NAME,STANDARD_IMPORT_PREFIX)
    for simulation_file in res:
        mg = MGModel(simulation_file.config_hierarchy_path,simulation_file.config_iot_devices_path)
        print(mg.to_simulator_json())
if __name__ == '__main__':
    main()

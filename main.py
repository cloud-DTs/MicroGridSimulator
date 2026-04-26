import json
import os
import logging
import tempfile
import sys
from argparse import ArgumentParser

from flask import after_this_request

from src.Collector.collector import collect_all_simulation_config_files
from src.MGModel.mgmodel import MGModel
from src.Simulator.ExperimentManager import ExperimentManager
INPUT_PATH = "DigitalTwinProfileSysMLv2/output/"
STANDARD_IMPORT_PREFIX = "simulation"
ST_TWIN_NAME = "StTwin"
THRESHOLDS_JSON_PATH = "./KnowledgeBase/Thresholds.json"
STTwinSysmlPath = "./KnowledgeBase/StandardGrid.sysml"
LIB_MODEL_PATH = "./KnowledgeBase/Model.sysml"
OUTPUT_DATA_PATH = "./SimulatorDataOutput"
API_BASE_URL = "http://localhost:1337"
import argparse
logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s [%(levelname)s] %(name)s: %(message)s"
)
import dotenv
dotenv.load_dotenv()

class MultiConfig:
    def __init__(self):
        self.name = ""
        self.baseline = ""
        self.additional = ""

    def read_from_dict(self,d):
        self.name = d["name"]
        self.baseline = d["BaseLineSysMlPath"]
        self.additional = d["AdditionalEnvSysMlPath"]

def runSysmlConverter(inputDir,outputDir):
    import subprocess
    logging.info("Running SysML Converter")
    result = subprocess.run(
        ["python", "-m", "src.main", f"{inputDir}", f"{outputDir}"],
        cwd="DigitalTwinProfileSysMLv2/",
        stdout=subprocess.DEVNULL,
        stderr=subprocess.DEVNULL
    )
    if result.returncode != 0:
        logging.error(f"SysML Converter failed with exit code {result.returncode}")
        sys.exit(1)
    logging.info("Finished SysML Converter")

def containsTwin(name,path):
    with open(path, "r") as f:
        if(f.read().__contains__(f"#Twin def {name}")):
            return True
        else:
            return False
def simulate(configPath):
    if not os.path.exists(configPath):
        logging.error(f"Config file not found: {configPath}")
        sys.exit(1)
    pairs = []
    with open(configPath, "r") as json_file:
        config = json.load(json_file)
        for conf in config:
            mConfig = MultiConfig()
            mConfig.read_from_dict(conf)
            pairs.append(mConfig)

    manager = ExperimentManager(OUTPUT_DATA_PATH, API_BASE_URL)
    temp_dirs = []

    try:
        for pair in pairs:
            for sysmlFile, is_baseline in [(pair.additional, False), (pair.baseline, True)]:
                inputDir = tempfile.TemporaryDirectory()
                outputDir = tempfile.TemporaryDirectory()
                temp_dirs.extend([inputDir, outputDir])
                for file in [sysmlFile,LIB_MODEL_PATH, STTwinSysmlPath]:
                    if not os.path.exists(file):
                        logging.error(f"File not found: {file}")
                        sys.exit(1)
                    dest = os.path.join(inputDir.name, os.path.basename(file))
                    with open(dest, "w") as f, open(file, "r") as orig:
                        f.write(orig.read())

                runSysmlConverter(inputDir.name, outputDir.name)
                simPath = collect_all_simulation_config_files(outputDir.name, ST_TWIN_NAME, STANDARD_IMPORT_PREFIX)
                if simPath is None:
                    logging.error(f"Pair '{pair.name}': no simulation configs found in {outputDir.name}")
                    continue
                simPath.name = os.path.splitext(os.path.basename(sysmlFile))[0]
                mg = MGModel(simPath, THRESHOLDS_JSON_PATH)
                manager.add_simulation_payload(pair.name, simPath.name, mg, is_baseline)

        if not manager.paths:
            logging.error("No simulations registered — aborting.")
            return

        manager.run_simulations()

    finally:
        for d in temp_dirs:
            d.cleanup()

def main():
    parser = ArgumentParser()
    parser.add_argument(
        "--config",
        default="multi_config.json",
        help="JSON file with pair definitions (only used with --mode multi)."
    )

    args = parser.parse_args()
    simulate(args.config)

if __name__ == '__main__':
    main()

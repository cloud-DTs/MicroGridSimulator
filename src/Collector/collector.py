import os
import logging
import sys
from dataclasses import dataclass

from src.Collector.SimulationFiles import SimulationFilePaths

def get_marker(path):
    with open(path, "r") as f:
        first_line = f.readline().strip()
    if first_line.startswith("//"):
        return first_line[2:]
    return None
def collect_all_simulation_config_files(input_path, standardTwinName, standardImportPrefix, marker=None):
    for root, dirs, files in os.walk(input_path):
        if standardTwinName in root:
            continue
        for file in files:
            file_path = os.path.join(root, file)
            with open(file_path, "r") as f:
                content = f.read()
            if standardImportPrefix not in content:
                continue
            if marker is not None:
                found = False
                for other in os.listdir(root):
                    other_path = os.path.join(root, other)
                    if get_marker(other_path) == marker:
                        found = True
                        break
                if not found:
                    continue
            sim = SimulationFilePaths()
            sim.config_hierarchy_path   = os.path.join(root, "config_hierarchy.json")
            sim.config_iot_devices_path = os.path.join(root, "config_iot_devices.json")
            return sim
    return None


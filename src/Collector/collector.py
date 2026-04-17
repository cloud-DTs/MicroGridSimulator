import os
import logging
from dataclasses import dataclass

from src.Collector.SimulationFiles import SimulationFilePaths


def collect_all_simulation_files(input_path,standardTwinName,standardImportPrefix) -> list[SimulationFilePaths]:
    res = []
    visited = set()
    for root, dirs, files in os.walk(input_path):
        if standardTwinName in root:
            continue
        for file in files:
            file_path = os.path.join(root, file)
            with open(file_path, "r") as f:
                if standardImportPrefix in f.read():
                    if root not in visited:
                        visited.add(root)
                        sim = SimulationFilePaths()
                        sim.name = os.path.basename(root)
                        sim.config_hierarchy_path = os.path.join(root, "config_hierarchy.json")
                        sim.config_iot_devices_path = os.path.join(root, "config_iot_devices.json")
                        res.append(sim)
    return res


import json
import os.path
from datetime import datetime
from traceback import print_tb

from src.Collector.SimulationFiles import SimulationFilePaths
from src.entities.battery import BatteryEntity
from src.entities.pv import PVEntity
from src.entities.grid import GridEntity
from src.entities.wallbox import WallBoxEntity
from src.entities.car import CarEntity
from src.entities.load import LoadEntity
from src.entities.microgrid import MicroGridEntity
from src.entities.simulation import SimulationEntity

class MGModel:
    def __init__(self,simulatorFilePaths: SimulationFilePaths,thresholdpath):
        self.battery    = None
        self.pv         = None
        self.grid       = None
        self.simulation = None
        self.microgrid  = None
        self.load       = None
        self.wallboxes  = []
        self.cars       = []
        self.name = simulatorFilePaths.name
        self.steps = 0

        self._load(simulatorFilePaths.config_hierarchy_path, simulatorFilePaths.config_iot_devices_path,thresholdpath)

    def _load(self, hierarchy_path: str, iot_devices_path: str, thresholdpath: str):
        with open(hierarchy_path) as f:
            hierarchy = json.load(f)
        with open(iot_devices_path) as f:
            devices = json.load(f)
        with open(thresholdpath) as f:
            thresholds = json.load(f)
        props_index = {
            d["id"]: {p["name"]: p for p in d["properties"]}
            for d in devices
        }

        for entity in hierarchy:
            if entity["name"] == "simulation":
                const_id = entity["id"] + "const_component"
                props = props_index.get(const_id, {})
                self.simulation = SimulationEntity(props)
                sim_dict = self.simulation.to_simulation()
                start = datetime.fromisoformat(sim_dict["SIMULATION_START_TIME"])
                end = datetime.fromisoformat(sim_dict["SIMULATION_END_TIME"])
                self.steps = int((end - start).total_seconds() / sim_dict["TIMESTEP"])
                break

        for entity in hierarchy:
            name     = entity["name"]
            const_id = entity["id"] + "const_component"
            props    = props_index.get(const_id, {})
            if name == "simulation":
                continue

            elif name == "battery":
                self.battery = BatteryEntity(props,thresholds,self.steps)
            elif name == "pv":
                self.pv = PVEntity(props,self.steps)
            elif name == "grid":
                self.grid = GridEntity(props,self.steps)

            elif name == "load":
                self.load = LoadEntity(props,self.steps)
            elif name.startswith("wallbox_"):
                name = name.replace("wallbox_", "")
                self.wallboxes.append(WallBoxEntity(name, props))
            elif name.startswith("car_"):
                self.cars.append(CarEntity(name, props))
            elif name == "microgrid":
                self.microgrid = MicroGridEntity(props,self.cars)

    def to_simulator_dict(self) -> dict:
        res = {
            "testbed": {
                "battery": self.battery.to_testbed(),
                "grid":    self.grid.to_testbed(),
                "wallbox": [w.to_testbed() for w in self.wallboxes],
            },
            "simulation": {
                "battery":        self.battery.to_simulation(),
                "pv":             self.pv.to_simulation(),
                "grid":           self.grid.to_simulation(),
                "load":           self.load.to_simulation(),
                "microgrid":      self.microgrid.to_simulation(),
                "initial_values": self.simulation.to_simulation(),
            }
        }
       
        return res

    def to_simulator_json(self,path) -> str:
      
        with open(f"{self.name}.json","w") as f:
            json.dump(self.to_simulator_dict(),f,indent=4)
        return f"{os.path.join(path,self.name)}.json"
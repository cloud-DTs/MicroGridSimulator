import json
from src.entities.battery import BatteryEntity
from src.entities.pv import PVEntity
from src.entities.grid import GridEntity
from src.entities.wallbox import WallBoxEntity
from src.entities.car import CarEntity
from src.entities.load import LoadEntity
from src.entities.microgrid import MicroGridEntity
from src.entities.simulation import SimulationEntity

class MGModel:
    def __init__(self, hierarchy_path: str, iot_devices_path: str):
        self.battery    = None
        self.pv         = None
        self.grid       = None
        self.simulation = None
        self.microgrid  = None
        self.load       = None
        self.wallboxes  = []
        self.cars       = []
        self._load(hierarchy_path, iot_devices_path)

    def _load(self, hierarchy_path: str, iot_devices_path: str):
        with open(hierarchy_path) as f:
            hierarchy = json.load(f)
        with open(iot_devices_path) as f:
            devices = json.load(f)
        props_index = {
            d["id"]: {p["name"]: p for p in d["properties"]}
            for d in devices
        }

        for entity in hierarchy:
            name     = entity["name"]
            const_id = entity["id"] + "const_component"
            props    = props_index.get(const_id, {})

            if name == "battery":
                self.battery = BatteryEntity(props)
            elif name == "pv":
                self.pv = PVEntity(props)
            elif name == "grid":
                self.grid = GridEntity(props)
            elif name == "simulation":
                self.simulation = SimulationEntity(props)
            elif name == "load":
                self.load = LoadEntity(props)
            elif name.startswith("wallbox_"):
                self.wallboxes.append(WallBoxEntity(name, props))
            elif name.startswith("car_"):
                self.cars.append(CarEntity(name, props))
            elif name == "microgrid":
                self.microgrid = MicroGridEntity(props,self.cars)

    def to_simulator_json(self) -> dict:
        return {
            "testbed": {
                "battery": self.battery.to_testbed(),
                "pv":      self.pv.to_testbed(),
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
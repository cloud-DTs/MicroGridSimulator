from dataclasses import dataclass
@dataclass
class SimulationFilePaths:
    name:str = ""
    config_hierarchy_path:str = ""
    config_iot_devices_path:str = ""
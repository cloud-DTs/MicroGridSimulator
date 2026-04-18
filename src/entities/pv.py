from src.entities.AbstracteEntity import AbstractEntity


class PVEntity(AbstractEntity):
    def __init__(self,prop):
        super().__init__(prop)

    def to_testbed(self) -> dict:
        return {
            "max_output": self._val("max_output"),
            "min_output": self._val("min_output"),
        }

    def to_simulation(self) -> dict:
        return self._load_csv("pv_timeseries_path")
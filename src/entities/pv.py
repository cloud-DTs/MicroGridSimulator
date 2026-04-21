from src.entities.AbstracteEntity import AbstractEntity, TimeSeriesEntity


class PVEntity(TimeSeriesEntity):
    def __init__(self,prop,step):
        super().__init__(prop,step)

    def to_testbed(self) -> dict:
        return {
            "max_output": self._val("max_output"),
            "min_output": self._val("min_output")
        }

    def to_simulation(self) -> dict:
        return {
            "pv_timeseries_path":self._load_csv("pv_timeseries_path"),
            "noise_level": self._val("noise_level"),
            "pv_start_time": self._val("pv_start_time"),
            "pv_end_time": self._val("pv_end_time")
        }
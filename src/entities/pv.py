from src.entities.AbstracteEntity import AbstractEntity, TimeSeriesEntity


class PVEntity(TimeSeriesEntity):
    def __init__(self,prop,step):
        super().__init__(prop,step)

   

    def to_simulation(self) -> list:
        return self._load_csv("pv_timeseries_path")
        
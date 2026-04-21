from src.entities.AbstracteEntity import AbstractEntity, TimeSeriesEntity


class LoadEntity(TimeSeriesEntity):
    def __init__(self,prop,step):
        super().__init__(prop,step)

    def to_simulation(self) -> dict:
        return self._load_csv("load_timeseries_path")
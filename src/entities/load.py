from src.entities.AbstracteEntity import AbstractEntity


class LoadEntity(AbstractEntity):
    def __init__(self,prop):
        super().__init__(prop)

    def to_simulation(self) -> dict:
        return self._load_csv("load_timeseries_path")
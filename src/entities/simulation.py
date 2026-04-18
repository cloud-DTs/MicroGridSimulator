from src.entities.AbstracteEntity import AbstractEntity


class SimulationEntity(AbstractEntity):
    def __init__(self,prop):
        self.prop = prop
        super().__init__(prop)

    def to_simulation(self) -> dict:
        return {
            "TIMESTEP": self._val("timestep"),
            "SIMULATION_START_TIME": self._val("start_time"),
            "SIMULATION_END_TIME": self._val("end_time"),
            "random_seed": self._val("random_seed"),
        }

from src.entities.AbstracteEntity import AbstractEntity


class SimulationEntity(AbstractEntity):
    def __init__(self,prop):
        self.prop = prop
        super().__init__(prop)

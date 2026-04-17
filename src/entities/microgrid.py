from src.entities.AbstracteEntity import AbstractEntity


class MicroGridEntity(AbstractEntity):
    def __init__(self,prop,cars):
        super().__init__(prop)
        self.cars = cars

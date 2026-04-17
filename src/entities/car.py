from src.entities.AbstracteEntity import AbstractEntity


class CarEntity(AbstractEntity):
    def __init__(self,name,prop):
        super().__init__(prop)
        self.name = name
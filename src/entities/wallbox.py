from src.entities.AbstracteEntity import AbstractEntity


class WallBoxEntity(AbstractEntity):
    def __init__(self,name,prop):
        super().__init__(prop)
        self.name = name

    def to_testbed(self) -> dict:
        return {
            "name": self.name,
            "max_power_output": self._val("max_power_output"),
            "control": {
                "ID": self._val("control_ID"),
                "pv_threshold": self._val("pv_threshold"),
                "strategy_mode": self._val("strategy_mode"),
            }
        }

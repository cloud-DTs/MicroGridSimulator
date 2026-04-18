from src.entities.AbstracteEntity import AbstractEntity


class MicroGridEntity(AbstractEntity):
    def __init__(self,prop,cars):
        super().__init__(prop)
        self.cars = cars

    def to_simulation(self) -> dict:
        return {
            "unbalanced_module": bool(self._val("unbalanced_module")),
            "generate_charging_events": {
                "number_charging_events": self._val("number_charging_events"),
                "max_capacity": self._val("max_capacity"),
                "min_capacity": self._val("min_capacity"),
                "max_charge": self._val("max_charge"),
                "min_charge": self._val("min_charge"),
                "min_soc": self._val("min_soc"),
                "max_soc": self._val("max_soc"),
                "earliest_arrival": self._val("earliest_arrival"),
                "latest_departure": self._val("latest_departure"),
                "min_charging_length": self._val("min_charging_length"),
                "max_charging_length": self._val("max_charging_length"),
                "weekend_free": self._val("weekend_free") == "true",
            },
            "charging_events": [c.to_charging_event() for c in self.cars]
        }
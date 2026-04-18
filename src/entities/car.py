from src.entities.AbstracteEntity import AbstractEntity


class CarEntity(AbstractEntity):
    def __init__(self,name,prop):
        super().__init__(prop)
        self.name = name.replace("car_", "")

    def to_charging_event(self) -> dict:
        pref = self._val("preference_wallbox")
        return {
            "name": self.name,
            "capacity": self._val("capacity"),
            "max_charge": self._val("max_charge"),
            "soc": self._val("soc"),
            "arrival_date": self._val("arrival_date"),
            "departure_date": self._val("departure_date"),
            "preference_wallbox": [w.strip().replace("wallbox_", "") for w in pref.split(",")] if pref else []
        }

from src.entities.AbstracteEntity import AbstractEntity


class BatteryEntity(AbstractEntity):

    def __init__(self,prop,thresholds):
        super().__init__(prop)
        self.thresholds = thresholds
    def to_testbed(self) -> dict:
        dynamic_threshold_strategy = self._val("dynamic_threshold_strategy")
        res =  {
            "min_capacity":    self._val("min_capacity"),
            "max_capacity":    self._val("max_capacity"),
            "max_charge":      self._val("max_charge"),
            "max_discharge":   self._val("max_discharge"),
            "discharge_efficiency": self._val("discharge_efficiency"),
            "charge_efficiency":    self._val("charge_efficiency"),
            "operating_temperature": [
                self._val("operating_temperature_low"),
                self._val("operating_temperature_high")
            ],
            "control": {
                "ID": self._val("control_ID"),
                "thresholds": self.thresholds,            }
        }
        dynamic_threshold_strategy = self._val("dynamic_threshold_strategy")
        if dynamic_threshold_strategy:
            res["control"]["dynamic_threshold_strategy"] = dynamic_threshold_strategy
            if dynamic_threshold_strategy == "co2_based":
                res["control"]["co2_reward"] = self._load_csv("co2_reward_timeseries_path")


        return res



    def to_simulation(self) -> dict:
        return {"init_soc": self._val("init_soc")}
import logging

from src.entities.AbstracteEntity import AbstractEntity, TimeSeriesEntity


class GridEntity(TimeSeriesEntity):
    def __init__(self,prop,step):
        super().__init__(prop,step)

    def to_testbed(self) -> dict:
        return {
            "grid_status": 1 if self._val("grid_status")>0 else 0,
            "max_import": self._val("max_import"),
            "max_export": self._val("max_export"),
        }

    def to_simulation(self) -> dict:
        return {
            "import_price": self.useApiIfEmpty("import_price_timeseries_path"),
            "export_price": self._load_csv("export_price_timeseries_path"),
            "co2_per_kWh": self.useApiIfEmpty("co2_per_kWh_timeseries_path"),
            "grid_limit": self._val("grid_limit"),
        }

    def useApiIfEmpty(self,key):
        if self._val(key) == "//":
            logging.info(f"Grid attribute {key} is //. Using API")
            return True
        return self._load_csv(key)
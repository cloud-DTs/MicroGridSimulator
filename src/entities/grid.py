from src.entities.AbstracteEntity import AbstractEntity


class GridEntity(AbstractEntity):
    def __init__(self,prop):
        super().__init__(prop)

    def to_testbed(self) -> dict:
        return {
            "grid_status": 1 if self._val("grid_status")>0 else 0,
            "max_import": self._val("max_import"),
            "max_export": self._val("max_export"),
        }

    def to_simulation(self) -> dict:
        return {
            "import_price": self._load_csv("import_price_timeseries_path"),
            "export_price": self._load_csv("export_price_timeseries_path"),
            "co2_per_kWh": self._load_csv("co2_per_kWh_timeseries_path"),
            "grid_limit": self._val("grid_limit"),
        }


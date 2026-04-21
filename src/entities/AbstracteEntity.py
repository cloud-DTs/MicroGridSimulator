import logging
import os
from traceback import print_tb

import pandas as pd
EMPTY_PATH = "//"
class AbstractEntity:
    def __init__(self,prop):
        self.prop = prop

    def _val(self, key):
        return self.prop.get(key,{}).get("initValue",None)

    def _load_csv(self, val: str) -> list:
        path = self._val(val)
        if path == EMPTY_PATH or not path:
            logging.error(f"Path {path} of {val} is empty please provide a valid path")
            exit(1)
        if not os.path.exists(path):
            logging.error(f"Path  {path} of {val} does not exist")
            exit(1)
        return pd.read_csv(path)['value'].to_list()



    def to_testbed(self)->dict:
        return self.prop

    def to_simulation(self)->dict:
        res = {}
        return self.prop
class TimeSeriesEntity(AbstractEntity):
    def __init__(self,prop,steps):
        super().__init__(prop)
        self.steps = steps

    def _load_csv(self, val: str) -> list:
        data = super()._load_csv(val)
        target = self.steps
        missingValues = target - len(data)
        if missingValues > 0:
            filledData = [data[i % len(data)] for i in range(self.steps)]
            return filledData
        return data
import logging
import os

import pandas as pd
EMPTY_PATH = "//"
class AbstractEntity:
    def __init__(self,prop):
        self.prop = prop

    def _val(self, key):
        return self.prop.get(key,{}).get("initValue",None)

    def _load_csv(self, val: str) -> dict:
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
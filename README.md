# MicroGrid Simulator — Knowledge Base Preparer

This tool connects a **SysML v2 Digital Twin model** to the **MicroGrid Simulator API**.
It reads your experiment configuration from SysML files, converts them into simulator-ready JSON payloads, runs the simulations, and saves the results as a CSV for analysis.

---

## Architecture

```
KnowledgeBase/*.sysml          ← Your experiment configuration
        ↓
DigitalTwinProfileSysMLv2      ← SysML v2 Converter (Docker)
        ↓
output/*/config_hierarchy.json
output/*/config_iot_devices.json
        ↓
MicroGridSimulator (this tool) ← KB Preparer + Experiment Runner
        ↓
results/<BaselineName>_<AdditionalName>_<timestamp>.csv
```

---

## Prerequisites

- Docker
- Python 3.12+

---

## Setup

### 1. Start the SysML v2 Docker environment

```bash
cd DigitalTwinProfileSysMLv2/apiserver
docker-compose up -d
cd ../..
```

### 2. Create and activate a Python virtual environment

```bash
python3.12 -m venv .venv
source .venv/bin/activate       # Linux/Mac
.venv\Scripts\activate          # Windows
```

### 3. Install dependencies

```bash
pip install -r requirements.txt
```

---

## Configuration

Your experiments are defined in two SysML files inside `./KnowledgeBase/`:

| File | Purpose |
|---|---|
| `BaselineEnvironmentalConditions.sysml` | Reference simulation (e.g. Nov 1) |
| `AdditionalEnvironmentalConditions.sysml` | Collection simulation (e.g. Nov 2–30) |

Both files follow the same structure and directly mirror the simulator's parameters:

```sysml
package BaselineEnvironmentalConditions {
    private import MetaModelDigitalTwin::*;
    private import ModelDefault::*;
    private import StandardMG::*;

    #Twin def Base :> StTwin {

        :>> pv {
            :>> pv_timeseries_path { :>> value = "./data/pv_nov.csv"; }
        }

        :>> grid {
            :>> import_price_timeseries_path { :>> value = "./data/prices_nov.csv"; }
            :>> export_price_timeseries_path { :>> value = "./data/prices_nov.csv"; }
            :>> co2_per_kWh_timeseries_path  { :>> value = "./data/co2_nov.csv"; }
        }

        :>> battery {
            :>> co2_reward_timeseries_path   { :>> value = "./data/co2_nov.csv"; }
            :>> dynamic_threshold_strategy   { :>> value = "co2_based"; }
        }

        :>> microgrid {
            :>> unbalanced_module        { :>> value = "0"; }
            :>> number_charging_events   { :>> value = "15"; }
        }

        :>> simulation {
            :>> simulationName { :>> value = "BaseLineNovember"; }  // used to identify results
            :>> start_time     { :>> value = "2025-11-01T00:00"; }
            :>> end_time       { :>> value = "2025-11-02T00:00"; }  // 1 day = next day 00:00
        }

        car1 :> charging_events {   // charging events must start with 'car'
            :>> arrival_date       { :>> value = "2025-11-01T08:00"; }
            :>> departure_date     { :>> value = "2025-11-01T16:00"; }
            :>> preference_wallbox { :>> value = "wallbox_Keba"; }
        }

        :>> load {
            :>> load_timeseries_path { :>> value = "./data/load_nov.csv"; }
        }
    }
}
```

### Timeseries CSV Format

All timeseries files (PV, prices, CO2, load) must be CSV files with a single `value` column.
The number of rows must exactly match the number of simulation steps:

```
steps = (end_time - start_time) / timestep
```

Example for 1 day with `timestep = 3600s` (1 hour):

```csv
value
0.0
0.0
50.0
200.0
500.0
...
```

### Battery Thresholds

Battery control thresholds are configured separately in `Threshold.json`:

```json
{
    "thresholds": [90, 70, 10]
}
```

Values represent `[threshold_high, threshold_mid, threshold_low]` as percentage of battery SOC.

---

## Running

### Step 1 — Convert SysML to JSON

```bash
cd DigitalTwinProfileSysMLv2
python -m src.main ../KnowledgeBase/
cd ..
```

This reads all `.sysml` files in `KnowledgeBase/` and generates the converter output under `DigitalTwinProfileSysMLv2/output/`.

### Step 2 — Run the experiment

```bash
python main.py
```

This prepares the simulator payloads, runs both simulations, and saves the results.

---

## Output

Results are saved to:

```
results/<BaselineName>_<AdditionalName>_<YYYYMMDD_HHMMSS>.csv
```

Example: `results/BaseLineNovember_AdditionalNovember_20251119_143022.csv`

The CSV contains one row per timestep with all simulator output columns, plus a `simulation_name` column to distinguish between baseline and additional runs:

| datetime | grid_import_cost | grid_co2_production | simulation_name    |
|---|---|---|---|
| 2025-11-01T00:00 | 12.3 | 45.1 | BaseLineNovember   |
| ... | ... | ... | ...                |
| 2025-11-02T00:00 | 14.1 | 48.3 | AdditionalNovember |

### Key KPIs

| Column | Unit |
|---|---|
| `grid_import_cost` | cents |
| `grid_co2_production` | gCO2eq |
---

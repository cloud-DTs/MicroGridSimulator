# MicroGrid Simulator — Knowledge Base Preparer

This tool connects a **SysML v2 Digital Twin model** to the **MicroGrid Simulator API**.
It reads your experiment configuration from SysML files, converts them into simulator-ready JSON payloads, runs the simulations, and saves the results as a CSV for analysis.

---

## Architecture

```
multi_config.json              ← Experiment pair definitions
        ↓
KnowledgeBase/*.sysml          ← Your SysML experiment files
        ↓
DigitalTwinProfileSysMLv2      ← SysML v2 Converter
        ↓
(temp)/*/config_hierarchy.json
(temp)/*/config_iot_devices.json
        ↓
MicroGridSimulator (this tool) ← KB Preparer + Experiment Runner
        ↓
SimulatorDataOutput/<timestamp>.csv
SimulatorDataOutput/<timestamp>_kpi_summary.csv
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

### Experiment pairs — `multi_config.json`

Define one or more baseline/additional pairs:

```json
[
  {
    "name": "November",
    "BaseLineSysMlPath":      "KnowledgeBase/BaselineEnvironmentalConditions.sysml",
    "AdditionalEnvSysMlPath": "KnowledgeBase/AdditionalEnvironmentalConditions.sysml"
  },
  {
    "name": "December",
    "BaseLineSysMlPath":      "KnowledgeBase/BaselineDecember.sysml",
    "AdditionalEnvSysMlPath": "KnowledgeBase/AdditionalDecember.sysml"
  }
]
```

| Field | Description |
|---|---|
| `name` | Identifier for this experiment pair, used in output filenames and KPI summary |
| `BaseLineSysMlPath` | Path to the baseline SysML file |
| `AdditionalEnvSysMlPath` | Path to the additional SysML file |

### SysML file structure

Both baseline and additional files follow the same structure:

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
            :>> simulationName { :>> value = "BaseLineNovember"; }
            :>> start_time     { :>> value = "2025-11-01T00:00"; }
            :>> end_time       { :>> value = "2025-11-02T00:00"; }
        }

        car1 :> charging_events {
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

> **Important:** The baseline Twin name must contain `Base` (case-insensitive) so the KPI summary can identify it correctly — e.g. `#Twin def Base` or `#Twin def BaseLineNovember`.

### Timeseries CSV format

All timeseries files (PV, prices, CO2, load) must be CSV files with a single `value` column.
The number of rows must exactly match the number of simulation steps:

```
steps = (end_time - start_time) / timestep
```

Example for 1 day with `timestep = 3600s`:

```csv
value
0.0
0.0
50.0
200.0
500.0
```

### Battery thresholds — `KnowledgeBase/Thresholds.json`

```json
{
    "thresholds": [90, 70, 10]
}
```

Values represent `[threshold_high, threshold_mid, threshold_low]` as percentage of battery SOC.

---

## Running

```bash
# uses multi_config.json by default
python main.py

# or with a custom config file
python main.py --config my_experiment.json
```

| Argument | Default | Description |
|---|---|---|
| `--config` | `multi_config.json` | Path to the experiment pair config JSON |

The tool will automatically:
1. Copy the SysML files into a temporary directory
2. Run the SysML converter
3. Build simulator payloads
4. Run all simulations via SSH tunnel
5. Save results and KPI summary

---

## Output

Results are saved to `SimulatorDataOutput/`:

```
SimulatorDataOutput/<PairNames>_<timestamp>.csv
SimulatorDataOutput/<PairNames>_<timestamp>_kpi_summary.csv
```

### Raw results CSV

One row per timestep, all simulator output columns plus identifiers:

| datetime | grid_import_cost | grid_co2_production | simulation_name | simulation_run |
|---|---|---|---|---|
| 2025-11-01T00:00 | 12.3 | 45.1 | November | BaselineEnvironmentalConditions |
| ... | | | | |

### KPI summary CSV

Aggregated averages and relative deviation per pair:

| simulation_name | simulation_run | avg_grid_import_cost | avg_grid_co2_production | count |
|---|---|---|---|---|
| November | BaselineEnvironmentalConditions | 12.3 | 45.1 | 720 |
| November | AdditionalEnvironmentalConditions | 14.1 | 48.3 | 720 |
| November | relative_deviation_% | 14.63 | 7.1 | 1.0 |
| December | BaselineDecember | ... | ... | 744 |
| ... | | | | |

### Key KPIs

| Column | Unit |
|---|---|
| `grid_import_cost` | cents |
| `grid_co2_production` | gCO2eq |

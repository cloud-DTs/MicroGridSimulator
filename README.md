# MicroGrid Simulator: TwinRobust Experiment Runner

This tool runs robustness experiments for the MicroGrid Simulator, defined as **SysML v2 twins**.
A baseline twin describes the testbed and the simulation; every additional condition is a child twin that only redefines what differs from the baseline.
`twinRobust` (Java) turns the twins into simulator payloads, `twinrobust.py` runs them on the simulator API and writes the results and a KPI summary.

---

## Architecture

```
KnowledgeBase/Model/*.sysml          simulator model (ST_Twin, variants, defaults)
<your folder>/*.sysml                baseline twins and their child twins
        ↓
twinRobust (Java, Maven)             SysML twins to simulator payloads
        ↓
SimulatorPayloads/<timestamp>/*.json one payload per twin
SimulatorPayloads/<timestamp>/experiments.json
        ↓
twinrobust.py                        runs all payloads over the SSH tunnel
        ↓
SimulatorDataOutput/<timestamp>.csv
SimulatorDataOutput/<timestamp>_kpi_summary.csv
```

---

## Prerequisites

| Tool | Version | Note |
|---|---|---|
| Python | 3.12+ | |
| Java (JDK) | 21 | twinRobust needs Java 21 |
| Maven | 3.9+ | `mvn` must be on the PATH; `mvn -v` has to show Java 21 |

---

## Setup

### 1. Python environment

```bash
python3.12 -m venv .venv
source .venv/bin/activate
.venv\Scripts\activate
pip install -r requirements.txt
```

### 2. Simulator connection: `.env`

```
SSH_HOST=...
SSH_USERNAME=...
SSH_PASSWORD=...
SIM_HOST=...
SIM_PORT=...
```

The simulator API is reached through an SSH tunnel to `SIM_HOST:1337`, forwarded to `127.0.0.1:SIM_PORT`.

### 3. Folder layout

```
MicroGridSimulator/
    twinrobust.py
    twinRobust/              Java project (pom.xml)
    KnowledgeBase/Model/     simulator model
    <your folder>/           your twins, for example testfiles/
```

---

## Defining experiments

### Baseline and conditions

A **baseline** is a twin that specializes `ST_Twin` directly. Every twin that specializes a baseline is one **condition** of it.
Write the values of the baseline with `default`, so the conditions can override them with `=`.

```sysml
package Scenario {
    private import MetaModelDigitalTwin::*;
    public import Model::*;

    #Twin def Baseline :> ST_Twin {
        :>> testbed {
            :>> battery : BatteryTestBed_Config {
                :>> control : BatteryControl_GridFriendly {
                    :>> thresholds : Thresholds_Path { :>> value default "testfiles/Thresholds.json"; }
                }
            }
        }
        :>> simulation {
            :>> battery : BatterySimulation_Config { :>> init_soc { :>> value default "0.5"; } }
            :>> grid : GridSimulation_Config { :>> grid_limit { :>> value default "25000"; } }
            :>> initial_values {
                :>> timestep { :>> value default "10"; }
                :>> start_time { :>> value default "2025-08-18T00:00"; }
                :>> end_time { :>> value default "2025-08-25T00:00"; }
            }
        }
    }
}

package ScenarioAdditional {
    private import MetaModelDigitalTwin::*;
    private import Model::*;
    private import Scenario::*;

    // only what differs from the baseline
    #Twin def Winter :> Baseline {
        :>> simulation { :>> battery { :>> init_soc { :>> value = "0.2"; } } }
    }
}
```

Rules:

1. A condition can override values, but it cannot pick another variant than its baseline (for example `GridImport_Price_Cent_Per_kWh_Static` instead of `_TimeSeries_Array`).
2. Several baselines in one run are fine; each one becomes its own pair.

### Paths

Relative paths in the SysML files (thresholds file, `*_TimeSeries_Path`) are resolved against the folder `twinrobust.py` is started from, so `testfiles/Thresholds.json` means `MicroGridSimulator/testfiles/Thresholds.json`.

### Battery thresholds: `Thresholds.json`

```json
{
    "thresholds": [90, 70, 10]
}
```

Integers from 0 to 100 in descending order (`[high, mid, low]`, percent of battery SOC), for example the result of MOMoT.


## Running

```bash
python twinrobust.py <sysml file or folder> ...
```

Example:

```bash
python twinrobust.py testfiles/
```

`KnowledgeBase/Model` is added automatically, unless one of the given folders already contains it.

The tool will:

1. run twinRobust through Maven and write the payloads and `experiments.json` to `SimulatorPayloads/<timestamp>/`
2. open the SSH tunnel
3. run all simulations, at most 6 in parallel
4. save the raw results and the KPI summary

---

## Output

### Raw results: `SimulatorDataOutput/<timestamp>.csv`

One row per time step with all simulator output columns, plus:

| Column | Description |
|---|---|
| `simulation_name` | the pair, named after its baseline |
| `simulation_run` | the twin |
| `role` | `base` or `additional` |
| `grid_limit` | grid limit of the run in W |
| `timestep` | time step of the run in seconds |

### KPI summary: `SimulatorDataOutput/<timestamp>_kpi_summary.csv`

One row per run, followed by one `relative_deviation_%` row per condition.

| KPI | Unit | Computation |
|---|---|---|
| `grid_import_cost_per_day` | cents per day | mean of `grid_import_cost` per step × steps per day (86400 / timestep) |
| `grid_co2_production_per_day` | gCO2eq per day | mean of `grid_co2_production` per step × steps per day |
| `grid_limit_violation_rate` | share of time | steps with `grid_import > grid_limit` / all steps |

Relative deviation: `(condition − baseline) / baseline × 100`. If the baseline value is 0, the deviation is left empty.

Values per day make a one day baseline comparable with week long conditions. The violation rate counts time, not events: one step over the limit counts as one time step.


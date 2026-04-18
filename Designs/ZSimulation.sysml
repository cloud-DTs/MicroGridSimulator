package NovemberScenario {
    private import MetaModelDigitalTwin::*;
    private import MG::*;
    private import StandardMG::*;

    #Twin def NTwin :> StTwin {

        #entity pv :>> pv {
            #constAttribute :>> pv_timeseries_path {
                :>> value = "./data/pv_nov.csv";
            }
        }

        #entity grid :>> grid {
            #constAttribute :>> import_price_timeseries_path {
                :>> value = "./data/prices_nov.csv";
            }
            #constAttribute :>> export_price_timeseries_path {
                :>> value = "./data/prices_nov.csv";
            }

            #constAttribute :>> co2_per_kWh_timeseries_path {
                :>> value = "./data/prices_nov.csv";
            }
        }

        #entity battery :>> battery {
            #constAttribute :>> co2_reward_timeseries_path {
                :>> value = "./data/co2_nov.csv";
            }
            #constAttribute :>> dynamic_threshold_strategy {
                :>> value = "co2_based";
            }
        }

        #entity simulation :>> simulation {
            #constAttribute :>> start_time { :>> value = "2025-11-01T00:00"; }
            #constAttribute :>> end_time   { :>> value = "2025-11-30T00:00"; }
        }

        #entity car_Tesla : ChargingEvents {
            #constAttribute :>> arrival_date      { :>> value = "2025-11-01T08:00"; }
            #constAttribute :>> departure_date    { :>> value = "2025-11-01T16:00"; }
            #constAttribute :>> preference_wallbox { :>> value = "wallbox_Keba"; }
        }

        #entity load :>> load {
            #constAttribute :>> load_timeseries_path {
                :>> value = "./data/load_nov.csv";
            }
        }
    }
}
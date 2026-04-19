package BaselineEnvironmentalConditions {
    private import MetaModelDigitalTwin::*;
    private import ModelDefault::*;
    private import StandardMG::*;

    #Twin def Base :> StTwin{

            :>> pv {
                :>> pv_timeseries_path {
                    :>> value = "./data/basel_pv_nov.csv";
                }
            }

            :>> grid {
                :>> import_price_timeseries_path {
                    :>> value = "./data/prices_nov.csv";
                }
                :>> export_price_timeseries_path {
                    :>> value = "./data/prices_nov.csv";
                }

                :>> co2_per_kWh_timeseries_path {
                    :>> value = "./data/prices_nov.csv";
                }
            }
            :>> battery {
                :>> co2_reward_timeseries_path {
                    :>> value = "./data/co2_nov.csv";
                
                }
                :>> dynamic_threshold_strategy {
                    :>> value = "co2_based";
                }
            }
            :>> microgrid{
                :>>unbalanced_module {
                    :>> value = "0";
                }
                :>>number_charging_events {
                    :>> value = "15";
                }
            }

            :>> simulation {
                :>> simulationName { :>> value = "BaseLineNovember"; }
                :>> start_time { :>> value = "2025-11-01T00:00"; }
                :>> end_time   { :>> value = "2025-11-02T00:00"; }
            }

            car1 :>charging_events{
               :>> arrival_date      { :>> value = "2025-11-01T08:00"; }
               :>> departure_date    { :>> value = "2025-11-01T16:00"; }
               :>> preference_wallbox { :>> value = "wallbox_Keba"; }
            }
            car2 :>charging_events{
                :>> arrival_date      { :>> value = "2025-11-01T09:00"; }
                :>> departure_date    { :>> value = "2025-11-01T17:00"; }
                :>> preference_wallbox { :>> value = "wallbox_Versi"; }
            }

            :>> load {
                :>> load_timeseries_path {
                    :>> value = "./data/load_nov.csv";
                }
            
            }
    }
}
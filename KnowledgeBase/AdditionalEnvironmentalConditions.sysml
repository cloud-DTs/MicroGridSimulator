package AdditionalEnvironmentalConditions {
    private import MetaModelDigitalTwin::*;
    private import ModelDefault::*;
    private import StandardMG::*;

    #Twin def Add :> StTwin {

            :>> pv {
                :>> pv_timeseries_path {
                    :>> value = "./data/NovemberFirstTry/additional_pov.csv";
                }
                
            }

            :>> grid {
                :>> export_price_timeseries_path {
                    :>> value = "./data/NovemberFirstTry/export_grid_price.csv";
                }
            }
            :>> battery {
                :>> dynamic_threshold_strategy {
                    :>> value = "null";
                }
              :>>  threshold_high {
            :>> value default "90";
        }
            :>> threshold_mid {
            :>> value default "70";
        }
            :>> threshold_low {
            :>> value default "10";
        }
        dynamic_threshold_strategy {
            :>> value default "null";
            :>> dataType default AttributeDataType::STRING;
        }
            }
            :>> microgrid{
                :>>unbalanced_module {
                    :>> value = "0";
                }
                :>>number_charging_events {
                    :>> value = "1";
                }
            }

            :>> simulation {
                :>> simulationName { :>> value = "AdditionalNovember"; }
                :>> start_time { :>> value = "2025-11-01T00:00"; }
                :>> end_time   { :>> value = "2025-12-01T00:00"; }
                :>> timestep { :>> value = "900"; }
            }

            :>> load {
                :>> load_timeseries_path {
                    :>> value = "./data/NovemberFirstTry/load_nov.csv";
                }

            }
    }
}
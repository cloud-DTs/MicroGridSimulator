package ModelDefault {

    private import MetaModelDigitalTwin::*;

    #Entity def Battery{

        #constAttribute min_capacity {
            :>> value default "100";
            :>> dataType default AttributeDataType::INTEGER;
        }
        #constAttribute max_capacity {
            :>> value default "70000";
            :>> dataType default AttributeDataType::INTEGER;
        }
        #constAttribute max_charge {
            :>> value default "52000";
            :>> dataType default AttributeDataType::INTEGER;
        }
        #constAttribute max_discharge {
            :>> value default "52000";
            :>> dataType default AttributeDataType::INTEGER;
        }
        #constAttribute discharge_efficiency {
            :>> value default "0.99";
            :>> dataType default AttributeDataType::DOUBLE;
        }
        #constAttribute charge_efficiency {
            :>> value default "0.75";
            :>> dataType default AttributeDataType::DOUBLE;
        }

        #constAttribute operating_temperature_low{
            :>> value default "-20";
            :>>dataType default AttributeDataType::INTEGER;
        }

        #constAttribute operating_temperature_high{
            :>> value default "50";
            :>>dataType default AttributeDataType::INTEGER;
        }

        #constAttribute control_ID {
            :>> value default "grid-friendly";
            :>> dataType default AttributeDataType::STRING;
        }
        #constAttribute threshold_high {
            :>> value default "90";
            :>> dataType default AttributeDataType::INTEGER;
        }
        #constAttribute threshold_mid {
            :>> value default "70";
            :>> dataType default AttributeDataType::INTEGER;
        }
        #constAttribute threshold_low {
            :>> value default "10";
            :>> dataType default AttributeDataType::INTEGER;
        }
        #constAttribute dynamic_threshold_strategy {
            :>> value default "null";
            :>> dataType default AttributeDataType::STRING;
        }

        #constAttribute co2_reward_timeseries_path{
            :>> value default "//";
            :>> dataType default AttributeDataType::STRING;
        }

        #constAttribute init_soc{
            :>> value default "0.5";
            :>> dataType default AttributeDataType::DOUBLE;
        }
    }

    #Entity def PV{

        #constAttribute max_output{
            :>> value default "1400";
            :>> dataType default AttributeDataType::INTEGER;
        }

        #constAttribute min_output{
            :>> value default "0";
            :>> dataType default AttributeDataType::INTEGER;
        }

        #constAttribute pv_timeseries_path{
            :>> value default "//";
            :>> dataType default AttributeDataType::STRING;
        }

        #constAttribute pv_start_time{
            :>> value default "06:30";
            :>> dataType default AttributeDataType::STRING;
        }
        #constAttribute pv_end_time{
            :>> value default "18:30";
            :>> dataType default AttributeDataType::STRING;
        }
        #constAttribute noise_level{
            :>> value default "0.1";
            :>> dataType default AttributeDataType::DOUBLE;
        }
    }

    #Entity def Grid{
        #constAttribute grid_status{
            :>> value default "1";
            :>> dataType default AttributeDataType::INTEGER;
        }
        #constAttribute max_import{
            :>> value default "250000";
            :>> dataType default AttributeDataType::INTEGER;
        }

        #constAttribute max_export{
            :>> value default "250000";
            :>> dataType default AttributeDataType::INTEGER;
        }

        #constAttribute import_price_timeseries_path{
            :>> value default "//";
            :>> dataType default AttributeDataType::STRING;
        }

        #constAttribute export_price_timeseries_path{
            :>> value default "//";
            :>> dataType default AttributeDataType::STRING;
        }

        #constAttribute co2_per_kWh_timeseries_path{
            :>> value default "//";
            :>> dataType default AttributeDataType::STRING;
        }
        #constAttribute grid_limit {
            :>> value default "25000";
            :>> dataType default AttributeDataType::INTEGER;
        }
    }

    #Entity def WallBox{
         #constAttribute max_power_output {
            :>> value default "11000";
            :>> dataType default AttributeDataType::INTEGER;
        }
        #constAttribute control_ID {
            :>> value default "adaptive-proportional-uniform-wallbox-strategy";
            :>> dataType default AttributeDataType::STRING;
        }
        #constAttribute pv_threshold {
            :>> value default "500";
            :>> dataType default AttributeDataType::INTEGER;
        }
        #constAttribute strategy_mode {
            :>> value default "blue";
            :>> dataType default AttributeDataType::STRING;
        }
    }

    #Entity def Simulation {
        #constAttribute simulationName{
            :>> value default "NAME";
            :>> dataType default AttributeDataType::STRING;
        }
        #constAttribute start_time {
            :>> value default "2025-11-01T00:00";
            :>> dataType default AttributeDataType::STRING;
        }
        #constAttribute end_time {
            :>> value default "2025-11-02T00:00";
            :>> dataType default AttributeDataType::STRING;
        }
        #constAttribute timestep {
            :>> value default "10";
            :>> dataType default AttributeDataType::INTEGER;
        }
        #constAttribute random_seed {
            :>> value default "42";
            :>> dataType default AttributeDataType::INTEGER;
        }
    }


    #Entity def MicroGrid {
    #constAttribute unbalanced_module {
        :>> value default "0";
        :>> dataType default AttributeDataType::INTEGER;
    }
    #constAttribute number_charging_events {
        :>> value default "15";
        :>> dataType default AttributeDataType::INTEGER;
    }
    #constAttribute max_capacity {
        :>> value default "100000";
        :>> dataType default AttributeDataType::INTEGER;
    }
    #constAttribute min_capacity {
        :>> value default "40000";
        :>> dataType default AttributeDataType::INTEGER;
    }
    #constAttribute max_charge {
        :>> value default "150000";
        :>> dataType default AttributeDataType::INTEGER;
    }
    #constAttribute min_charge {
        :>> value default "11000";
        :>> dataType default AttributeDataType::INTEGER;
    }
    #constAttribute min_soc {
        :>> value default "0.1";
        :>> dataType default AttributeDataType::DOUBLE;
    }
    #constAttribute max_soc {
        :>> value default "0.7";
        :>> dataType default AttributeDataType::DOUBLE;
    }
    #constAttribute earliest_arrival {
        :>> value default "06:30";
        :>> dataType default AttributeDataType::STRING;
    }
    #constAttribute latest_departure {
        :>> value default "18:30";
        :>> dataType default AttributeDataType::STRING;
    }
    #constAttribute min_charging_length {
        :>> value default "00:30";
        :>> dataType default AttributeDataType::STRING;
    }
    #constAttribute max_charging_length {
        :>> value default "07:30";
        :>> dataType default AttributeDataType::STRING;
    }
    #constAttribute weekend_free {
        :>> value default "false";
        :>> dataType default AttributeDataType::STRING;
    }
    }

    #Entity def ChargingEvents{
         #constAttribute capacity {
        :>> value default "70000";
        :>> dataType default AttributeDataType::INTEGER;
    }
    #constAttribute max_charge {
        :>> value default "11000";
        :>> dataType default AttributeDataType::INTEGER;
    }
    #constAttribute soc {
        :>> value default "0.2";
        :>> dataType default AttributeDataType::DOUBLE;
    }
    #constAttribute arrival_date {
        :>> value default "2025-11-01T08:00";
        :>> dataType default AttributeDataType::STRING;
    }
    #constAttribute departure_date {
        :>> value default "2025-11-01T16:00";
        :>> dataType default AttributeDataType::STRING;
    }
    #constAttribute preference_wallbox {
        :>> value default "wallbox_KebaP30,wallbox_SieVersicharge";
        :>> dataType default AttributeDataType::STRING;
    }
    }

    #Entity def Load {
    #constAttribute load_timeseries_path {
        :>> value default "//";
        :>> dataType default AttributeDataType::STRING;
    }
}

}
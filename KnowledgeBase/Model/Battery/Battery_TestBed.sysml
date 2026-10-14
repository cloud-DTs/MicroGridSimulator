package BatteryTestBed{
    private import MetaModelDigitalTwin::*;

    // empty = no testbed.battery
    #Entity def BatteryTestBed{

    }

    #Entity def BatteryTestBed_Config:>BatteryTestBed{
        #constAttribute min_capacity:>const_attributes{
            :>> value default "100";
            :>> dataType = AttributeDataType::INTEGER;
        }
        #constAttribute max_capacity:>const_attributes{
            :>> value default "70000";
            :>> dataType = AttributeDataType::INTEGER;
        }
        #constAttribute max_charge:>const_attributes{
            :>> value default "52000";
            :>> dataType = AttributeDataType::INTEGER;
        }
        #constAttribute max_discharge:>const_attributes{
            :>> value default "52000";
            :>> dataType = AttributeDataType::INTEGER;
        }
        #constAttribute discharge_efficiency:>const_attributes{
            :>> value default "0.99";
            :>> dataType = AttributeDataType::DOUBLE;
        }
        #constAttribute charge_efficiency:>const_attributes{
            :>> value default "0.75";
            :>> dataType = AttributeDataType::DOUBLE;
        }
        #constAttribute operating_temperature:>const_attributes:OperatingTemperature{ :>> value default "[-20, 50]"; }
        #entity control:>sbEntities:BatteryControl[1];
    }

    // operating_temperature: empty = field missing
    #ConstAttribute def OperatingTemperature{ }

    // [min, max], min < max
    #ConstAttribute def OperatingTemperature_Value:>OperatingTemperature{
        :>> dataType = AttributeDataType::VECTOR_INTEGER;
    }

    // empty = no control
    #Entity def BatteryControl{

    }

    // thresholds_mode:      ARRAY (thresholds_array, 0..100 descending) or PATH (thresholds_path, MOMoT file {"thresholds":[90,70,10]})
    // threshold_strategy:   NONE (static thresholds), TIME_BASED or CO2_BASED
    // co2_reward_mode:      ARRAY (co2_reward_array, renewable share 0..1 per step) or PATH (co2_reward_path); only for CO2_BASED
    #Entity def BatteryControl_GridFriendly:>BatteryControl{
        #constAttribute control_ID:>const_attributes{
            :>> value = "grid-friendly";
            :>> dataType = AttributeDataType::STRING;
        }
        #constAttribute thresholds_mode:>const_attributes{
            :>> value default "ARRAY";
            :>> dataType = AttributeDataType::STRING;
        }
        #constAttribute thresholds_array:>const_attributes{
            :>> value default "[90, 70, 10]";
            :>> dataType = AttributeDataType::VECTOR_INTEGER;
        }
        #constAttribute thresholds_path:>const_attributes{
            :>> dataType = AttributeDataType::STRING;
        }
        #constAttribute threshold_strategy:>const_attributes{
            :>> value default "NONE";
            :>> dataType = AttributeDataType::STRING;
        }
        #constAttribute co2_reward_mode:>const_attributes{
            :>> value default "ARRAY";
            :>> dataType = AttributeDataType::STRING;
        }
        #constAttribute co2_reward_array:>const_attributes{
            :>> dataType = AttributeDataType::VECTOR_DOUBLE;
        }
        #constAttribute co2_reward_path:>const_attributes{
            :>> dataType = AttributeDataType::STRING;
        }
    }
}

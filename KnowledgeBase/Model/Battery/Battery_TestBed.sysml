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

    // thresholds: empty = field missing
    #ConstAttribute def Thresholds{ }
    #ConstAttribute def Thresholds_Path:>Thresholds{
    :>> dataType = AttributeDataType::STRING;
}

    // 0..100, descending, not empty
    #ConstAttribute def Thresholds_Value:>Thresholds{
        :>> dataType = AttributeDataType::VECTOR_INTEGER;
    }

    // empty = no control
    #Entity def BatteryControl{

    }

    #Entity def BatteryControl_GridFriendly:>BatteryControl{
        #constAttribute control_ID:>const_attributes{
            :>> value = "grid-friendly";
            :>> dataType = AttributeDataType::STRING;
        }
        #constAttribute thresholds:>const_attributes:Thresholds{ :>> value default "[90, 70, 10]"; }
        #entity threshold_strategy:>sbEntities:ThresholdStrategy[1];
    }

    // empty = static thresholds ("dynamic_threshold_strategy" missing = null)
    #Entity def ThresholdStrategy{

    }

    

    // "dynamic_threshold_strategy": "time_based"
    #Entity def ThresholdStrategy_TimeBased:>ThresholdStrategy{

    }

    // "dynamic_threshold_strategy": "co2_based", co2_reward = renewable share 0..1 per step
    #Entity def ThresholdStrategy_Co2Based_TimeSeries_Array:>ThresholdStrategy{
        #constAttribute co2_reward:>const_attributes{
            :>> value default "[0.0]";
            :>> dataType = AttributeDataType::VECTOR_DOUBLE;
        }
    }

    #Entity def ThresholdStrategy_Co2Based_TimeSeries_Path:>ThresholdStrategy{
        #constAttribute co2_reward_path:>const_attributes{
            :>> value default "//";
            :>> dataType = AttributeDataType::STRING;
        }
    }
}

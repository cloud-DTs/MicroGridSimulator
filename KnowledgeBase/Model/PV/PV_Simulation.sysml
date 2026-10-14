package PVSimulation{
    private import MetaModelDigitalTwin::*;

    // empty = no simulation.pv
    #Entity def PVSimulation{

    }

    // "pv": 50
    #Entity def PVSimulation_Static:>PVSimulation{
        #constAttribute pv_value:>const_attributes{
            :>> value default "0.0";
            :>> dataType = AttributeDataType::DOUBLE;
        }
    }

    // "pv": [10, 20, ...]
    #Entity def PVSimulation_TimeSeries_Array:>PVSimulation{
        #constAttribute pv_timeseries:>const_attributes{
            :>> value default "[0.0]";
            :>> dataType = AttributeDataType::VECTOR_DOUBLE;
        }
    }

    // csv file, loaded into "pv": [...]
    #Entity def PVSimulation_TimeSeries_Path:>PVSimulation{
        #constAttribute pv_timeseries_path:>const_attributes{
            :>> value default "//";
            :>> dataType = AttributeDataType::STRING;
        }
    }

    // "pv": {"pv_start_time", "pv_end_time", "noise_level"}
    #Entity def PVSimulation_Generator:>PVSimulation{
        #constAttribute pv_start_time:>const_attributes{
            :>> value default "06:30";
            :>> dataType = AttributeDataType::STRING;
        }
        #constAttribute pv_end_time:>const_attributes{
            :>> value default "18:30";
            :>> dataType = AttributeDataType::STRING;
        }
        #constAttribute noise_level:>const_attributes{
            :>> value default "0.1";
            :>> dataType = AttributeDataType::DOUBLE;
        }
    }
}

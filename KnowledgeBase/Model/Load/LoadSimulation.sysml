package LoadSimulation{
    private import MetaModelDigitalTwin::*;

    // empty = no simulation.load
    #Entity def LoadSimulation{

    }

    // "load": [50, 60, ...]
    #Entity def LoadSimulation_TimeSeries_Array:>LoadSimulation{
        #constAttribute load_timeseries:>const_attributes{
            :>> value default "[0.0]";
            :>> dataType = AttributeDataType::VECTOR_DOUBLE;
        }
    }

    // csv file, loaded into "load": [...]
    #Entity def LoadSimulation_TimeSeries_Path:>LoadSimulation{
        #constAttribute load_timeseries_path:>const_attributes{
            :>> value default "//";
            :>> dataType = AttributeDataType::STRING;
        }
    }

    // "load": [{"load_consumption": 100}, ...]: one "#entity x :> consumer" per load;
    // the slot "consumer" itself is no load, the builder skips it
    #Entity def LoadSimulation_Static:>LoadSimulation{
        #entity consumer:>sbEntities:LoadConsumer[0..*];
    }

    #Entity def LoadConsumer{
        #constAttribute load_consumption:>const_attributes{
            :>> value default "0";
            :>> dataType = AttributeDataType::DOUBLE;
        }
    }
}

package LoadSimulation{
    private import MetaModelDigitalTwin::*;

    // empty = no simulation.load
    #Entity def LoadSimulation{

    }

    // load_mode selects the form, so a child twin can switch it:
    //   STATIC "load": [{"load_consumption"}, ...], one "x :> consumer" per load
    //   ARRAY  "load": load_array (one value per step)
    //   PATH   "load": csv file load_path with a column "value"
    // the slot "consumer" itself is no load, the builder skips it
    #Entity def LoadSimulation_Config:>LoadSimulation{
        #constAttribute load_mode:>const_attributes{
            :>> value default "STATIC";
            :>> dataType = AttributeDataType::STRING;
        }
        #constAttribute load_array:>const_attributes{
            :>> dataType = AttributeDataType::VECTOR_DOUBLE;
        }
        #constAttribute load_path:>const_attributes{
            :>> dataType = AttributeDataType::STRING;
        }
        #entity consumer:>sbEntities:LoadConsumer[0..*];
    }

    #Entity def LoadConsumer{
        #constAttribute load_consumption:>const_attributes{
            :>> value default "0";
            :>> dataType = AttributeDataType::DOUBLE;
        }
    }
}

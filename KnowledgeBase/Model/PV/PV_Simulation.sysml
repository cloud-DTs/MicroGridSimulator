package PVSimulation{
    private import MetaModelDigitalTwin::*;

    // empty = no simulation.pv
    #Entity def PVSimulation{

    }

    // pv_mode selects the form, so a child twin can switch it:
    //   STATIC    "pv": pv_value
    //   ARRAY     "pv": pv_array (one value per step)
    //   PATH      "pv": csv file pv_path with a column "value"
    //   GENERATOR "pv": {pv_start_time, pv_end_time, noise_level}; needs testbed.pv
    #Entity def PVSimulation_Config:>PVSimulation{
        #constAttribute pv_mode:>const_attributes{
            :>> value default "STATIC";
            :>> dataType = AttributeDataType::STRING;
        }
        #constAttribute pv_value:>const_attributes{
            :>> value default "0.0";
            :>> dataType = AttributeDataType::DOUBLE;
        }
        #constAttribute pv_array:>const_attributes{
            :>> dataType = AttributeDataType::VECTOR_DOUBLE;
        }
        #constAttribute pv_path:>const_attributes{
            :>> dataType = AttributeDataType::STRING;
        }
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

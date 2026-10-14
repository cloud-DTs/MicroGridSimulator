package GridTestBed{
    private import MetaModelDigitalTwin::*;

    // empty = no testbed.grid
    #Entity def GridTestBed{

    }

    #Entity def GridTestBed_Config:>GridTestBed{
        #constAttribute grid_status:>const_attributes{
            :>> value default "1";
            :>> dataType = AttributeDataType::INTEGER;
        }
        #constAttribute max_import:>const_attributes{
            :>> value default "250000";
            :>> dataType = AttributeDataType::INTEGER;
        }
        #constAttribute max_export:>const_attributes{
            :>> value default "250000";
            :>> dataType = AttributeDataType::INTEGER;
        }
    }
}

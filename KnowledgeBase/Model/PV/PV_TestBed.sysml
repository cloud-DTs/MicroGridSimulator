package PVTestBed{
    private import MetaModelDigitalTwin::*;

    // empty = no testbed.pv
    #Entity def PVTestBed{

    }

    #Entity def PVTestBed_Config:>PVTestBed{
        #constAttribute max_output:>const_attributes{
            :>> value default "1400";
            :>> dataType = AttributeDataType::INTEGER;
        }
        #constAttribute min_output:>const_attributes{
            :>> value default "0";
            :>> dataType = AttributeDataType::INTEGER;
        }
    }
}

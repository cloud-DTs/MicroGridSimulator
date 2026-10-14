package WallBoxTestBed{
    private import MetaModelDigitalTwin::*;

    // empty = no testbed.wallbox
    #Entity def WallBoxes{ }

    // testbed.wallbox list: one "#entity x :> wallbox" per wallbox;
    // the slot "wallbox" itself is no wallbox, the builder skips it
    #Entity def WallBoxes_Config:>WallBoxes{
        #entity wallbox:>sbEntities:WallBoxTestBed[0..*];
    }

    // one entry of testbed.wallbox
    // control_mode: NONE (no control), CONSTANT or ADAPTIVE_PROPORTIONAL_UNIFORM; a control uses pv_threshold and strategy_mode
    #Entity def WallBoxTestBed{
        // unique, referenced by preference_wallbox
        #constAttribute name:>const_attributes{
            :>> value default "Wallbox";
            :>> dataType = AttributeDataType::STRING;
        }
        #constAttribute max_power_output:>const_attributes{
            :>> value default "11000";
            :>> dataType = AttributeDataType::INTEGER;
        }
        #constAttribute control_mode:>const_attributes{
            :>> value default "NONE";
            :>> dataType = AttributeDataType::STRING;
        }
        #constAttribute pv_threshold:>const_attributes{
            :>> value default "500";
            :>> dataType = AttributeDataType::INTEGER;
        }
        #constAttribute strategy_mode:>const_attributes{
            :>> value default "blue";
            :>> dataType = AttributeDataType::STRING;
        }
    }
}

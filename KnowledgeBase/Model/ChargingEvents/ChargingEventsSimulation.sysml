package ChargingEventSimulation{
    private import MetaModelDigitalTwin::*;

    // one entry of simulation.microgrid.charging_events
    #Entity def ChargingEventSimulation{
        #constAttribute name:>const_attributes{
            :>> value default "car";
            :>> dataType = AttributeDataType::STRING;
        }
        #constAttribute capacity:>const_attributes{
            :>> value default "70000";
            :>> dataType = AttributeDataType::INTEGER;
        }
        #constAttribute max_charge:>const_attributes{
            :>> value default "11000";
            :>> dataType = AttributeDataType::INTEGER;
        }
        #constAttribute soc:>const_attributes{
            :>> value default "0.2";
            :>> dataType = AttributeDataType::DOUBLE;
        }
        #constAttribute arrival_date:>const_attributes{
            :>> value default "2025-11-01T08:00";
            :>> dataType = AttributeDataType::STRING;
        }
        #constAttribute departure_date:>const_attributes{
            :>> value default "2025-11-01T16:00";
            :>> dataType = AttributeDataType::STRING;
        }
        // wallbox names in priority order, [] = random wallbox
        #constAttribute preference_wallbox:>const_attributes{
            :>> value default "[]";
            :>> dataType = AttributeDataType::VECTOR_STRING;
        }
    }
}

package MicroGridSimulation{
    private import MetaModelDigitalTwin::*;
    private import ChargingEventSimulation::*;

    // empty = no simulation.microgrid
    #Entity def MicroGridSimulation{

    }

    #Entity def MicroGridSimulation_Config:>MicroGridSimulation{
        // "true" / "false"
        #constAttribute unbalanced_module:>const_attributes{
            :>> value default "false";
            :>> dataType = AttributeDataType::STRING;
        }
        #entity generate_charging_events:>sbEntities:GenerateChargingEvents[1];
        #entity charging_events:>sbEntities:ChargingEvents[1];
    }

    // empty = no charging_events
    #Entity def ChargingEvents{ }

    // charging_events list: one "#entity x :> car" per car;
    // the slot "car" itself is no car, the builder skips it
    #Entity def ChargingEvents_Config:>ChargingEvents{
        #entity car:>sbEntities:ChargingEventSimulation[0..*];
    }

    // empty = no generate_charging_events
    #Entity def GenerateChargingEvents{

    }

    #Entity def GenerateChargingEvents_Config:>GenerateChargingEvents{
        #constAttribute number_charging_events:>const_attributes{
            :>> value default "15";
            :>> dataType = AttributeDataType::INTEGER;
        }
        #constAttribute max_capacity:>const_attributes{
            :>> value default "100000";
            :>> dataType = AttributeDataType::INTEGER;
        }
        #constAttribute min_capacity:>const_attributes{
            :>> value default "40000";
            :>> dataType = AttributeDataType::INTEGER;
        }
        #constAttribute max_charge:>const_attributes{
            :>> value default "150000";
            :>> dataType = AttributeDataType::INTEGER;
        }
        #constAttribute min_charge:>const_attributes{
            :>> value default "11000";
            :>> dataType = AttributeDataType::INTEGER;
        }
        #constAttribute min_soc:>const_attributes{
            :>> value default "0.1";
            :>> dataType = AttributeDataType::DOUBLE;
        }
        #constAttribute max_soc:>const_attributes{
            :>> value default "0.7";
            :>> dataType = AttributeDataType::DOUBLE;
        }
        #constAttribute earliest_arrival:>const_attributes{
            :>> value default "06:30";
            :>> dataType = AttributeDataType::STRING;
        }
        #constAttribute latest_departure:>const_attributes{
            :>> value default "18:30";
            :>> dataType = AttributeDataType::STRING;
        }
        #constAttribute min_charging_length:>const_attributes{
            :>> value default "00:30";
            :>> dataType = AttributeDataType::STRING;
        }
        #constAttribute max_charging_length:>const_attributes{
            :>> value default "07:30";
            :>> dataType = AttributeDataType::STRING;
        }
        // "true" / "false"
        #constAttribute weekend_free:>const_attributes{
            :>> value default "true";
            :>> dataType = AttributeDataType::STRING;
        }
    }
}

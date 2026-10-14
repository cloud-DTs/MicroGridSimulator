package SimulationMain{
    private import MetaModelDigitalTwin::*;

    // simulation.initial_values, required
    #Entity def SimulationMain{
        #constAttribute simulationName:>const_attributes{
            :>> value default "NAME";
            :>> dataType = AttributeDataType::STRING;
        }
        #constAttribute start_time:>const_attributes{
            :>> value default "2025-11-01T00:00";
            :>> dataType = AttributeDataType::STRING;
        }
        #constAttribute end_time:>const_attributes{
            :>> value default "2025-11-02T00:00";
            :>> dataType = AttributeDataType::STRING;
        }
        // seconds, 1..3600
        #constAttribute timestep:>const_attributes{
            :>> value default "10";
            :>> dataType = AttributeDataType::INTEGER;
        }
        #constAttribute random_seed:>const_attributes:RandomSeed{ :>> value default "42"; }
    }

    // random_seed: empty = field missing
    #ConstAttribute def RandomSeed{ }

    #ConstAttribute def RandomSeed_Value:>RandomSeed{
        :>> dataType = AttributeDataType::INTEGER;
    }
}

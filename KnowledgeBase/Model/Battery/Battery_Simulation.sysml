package BatterySimulation{
    private import MetaModelDigitalTwin::*;

    // empty = no simulation.battery
    #Entity def BatterySimulation{

    }

    #Entity def BatterySimulation_Config:>BatterySimulation{
        // relative 0..1
        #constAttribute init_soc:>const_attributes{
            :>> value default "0.5";
            :>> dataType = AttributeDataType::DOUBLE;
        }
    }
}

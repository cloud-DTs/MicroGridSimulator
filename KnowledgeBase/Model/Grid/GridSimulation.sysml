package GridSimulation{
    private import MetaModelDigitalTwin::*;

    // empty = no simulation.grid
    #Entity def GridSimulation{

    }

    // every grid value is chosen with a *_mode attribute instead of a variant, so a child twin can switch it:
    //   STATIC   uses *_value (number)
    //   ARRAY    uses *_array (time series, one value per step)
    //   PATH     uses *_path (csv file with a column "value")
    //   REALTIME fetched by the simulator (import price: aWATTar, co2: Electricity Maps); not for export_price
    #Entity def GridSimulation_Config:>GridSimulation{
        #constAttribute import_price_mode:>const_attributes{
            :>> value default "STATIC";
            :>> dataType = AttributeDataType::STRING;
        }
        #constAttribute import_price_value:>const_attributes{
            :>> value default "0";
            :>> dataType = AttributeDataType::DOUBLE;
        }
        #constAttribute import_price_array:>const_attributes{
            :>> dataType = AttributeDataType::VECTOR_DOUBLE;
        }
        #constAttribute import_price_path:>const_attributes{
            :>> dataType = AttributeDataType::STRING;
        }

        #constAttribute export_price_mode:>const_attributes{
            :>> value default "STATIC";
            :>> dataType = AttributeDataType::STRING;
        }
        #constAttribute export_price_value:>const_attributes{
            :>> value default "0";
            :>> dataType = AttributeDataType::DOUBLE;
        }
        #constAttribute export_price_array:>const_attributes{
            :>> dataType = AttributeDataType::VECTOR_DOUBLE;
        }
        #constAttribute export_price_path:>const_attributes{
            :>> dataType = AttributeDataType::STRING;
        }

        #constAttribute co2_mode:>const_attributes{
            :>> value default "STATIC";
            :>> dataType = AttributeDataType::STRING;
        }
        #constAttribute co2_value:>const_attributes{
            :>> value default "0";
            :>> dataType = AttributeDataType::DOUBLE;
        }
        #constAttribute co2_array:>const_attributes{
            :>> dataType = AttributeDataType::VECTOR_DOUBLE;
        }
        #constAttribute co2_path:>const_attributes{
            :>> dataType = AttributeDataType::STRING;
        }

        #constAttribute grid_limit:>const_attributes{
            :>> value default "25000";
            :>> dataType = AttributeDataType::INTEGER;
        }
        #constAttribute peak_shaving_penalty_in_EUR:>const_attributes:PeakShavingPenalty{ :>> value default "0.0"; }
    }

    // peak_shaving_penalty_in_EUR: empty = field missing
    #ConstAttribute def PeakShavingPenalty{ }

    #ConstAttribute def PeakShavingPenalty_Value:>PeakShavingPenalty{
        :>> dataType = AttributeDataType::DOUBLE;
    }
}

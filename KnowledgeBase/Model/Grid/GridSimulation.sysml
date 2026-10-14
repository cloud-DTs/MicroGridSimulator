package GridSimulation{
    private import MetaModelDigitalTwin::*;

    // empty = no simulation.grid
    #Entity def GridSimulation{

    }

    // defaults live on the slots; a variant only selects the form, so ":>> value" never shadows a second value
    #Entity def GridSimulation_Config:>GridSimulation{
        #constAttribute import_price_cent_per_kWh:>const_attributes:GridImport_Price_Cent_Per_kWh{ :>> value default "0"; }
        #constAttribute export_price_cent_per_kWh:>const_attributes:GridExport_Price_Cent_Per_kWh{ :>> value default "0"; }
        #constAttribute co2_g_per_kWh:>const_attributes:GridCo2_G_Per_kWh{ :>> value default "0"; }
        #constAttribute grid_limit:>const_attributes{
            :>> value default "25000";
            :>> dataType = AttributeDataType::INTEGER;
        }
        #constAttribute peak_shaving_penalty_in_EUR:>const_attributes:PeakShavingPenalty{ :>> value default "0.0"; }
    }

    // import_price: empty = field missing = 0
    #ConstAttribute def GridImport_Price_Cent_Per_kWh{ }

    #ConstAttribute def GridImport_Price_Cent_Per_kWh_Static:>GridImport_Price_Cent_Per_kWh{
        :>> dataType = AttributeDataType::DOUBLE;
    }
    #ConstAttribute def GridImport_Price_Cent_Per_kWh_TimeSeries_Array:>GridImport_Price_Cent_Per_kWh{
        :>> dataType = AttributeDataType::VECTOR_DOUBLE;
    }
    #ConstAttribute def GridImport_Price_Cent_Per_kWh_TimeSeries_Path:>GridImport_Price_Cent_Per_kWh{
        :>> dataType = AttributeDataType::STRING;
    }
    // true: aWATTar, Austrian market; the builder writes true, no value needed
    #ConstAttribute def GridImport_Price_Cent_Per_kWh_RealTime:>GridImport_Price_Cent_Per_kWh{
        :>> dataType = AttributeDataType::STRING;
    }

    // export_price: empty = field missing = 0, no real time variant
    #ConstAttribute def GridExport_Price_Cent_Per_kWh{ }

    #ConstAttribute def GridExport_Price_Cent_Per_kWh_Static:>GridExport_Price_Cent_Per_kWh{
        :>> dataType = AttributeDataType::DOUBLE;
    }
    #ConstAttribute def GridExport_Price_Cent_Per_kWh_TimeSeries_Array:>GridExport_Price_Cent_Per_kWh{
        :>> dataType = AttributeDataType::VECTOR_DOUBLE;
    }
    #ConstAttribute def GridExport_Price_Cent_Per_kWh_TimeSeries_Path:>GridExport_Price_Cent_Per_kWh{
        :>> dataType = AttributeDataType::STRING;
    }

    // co2_per_kWh: empty = field missing = 0
    #ConstAttribute def GridCo2_G_Per_kWh{ }

    #ConstAttribute def GridCo2_G_Per_kWh_Static:>GridCo2_G_Per_kWh{
        :>> dataType = AttributeDataType::DOUBLE;
    }
    #ConstAttribute def GridCo2_G_Per_kWh_TimeSeries_Array:>GridCo2_G_Per_kWh{
        :>> dataType = AttributeDataType::VECTOR_DOUBLE;
    }
    #ConstAttribute def GridCo2_G_Per_kWh_TimeSeries_Path:>GridCo2_G_Per_kWh{
        :>> dataType = AttributeDataType::STRING;
    }
    // true: Electricity Maps, zone AT, needs ELECTRICITY_MAPS_API_TOKEN; the builder writes true, no value needed
    #ConstAttribute def GridCo2_G_Per_kWh_RealTime:>GridCo2_G_Per_kWh{
        :>> dataType = AttributeDataType::STRING;
    }

    // peak_shaving_penalty_in_EUR: empty = field missing
    #ConstAttribute def PeakShavingPenalty{ }

    #ConstAttribute def PeakShavingPenalty_Value:>PeakShavingPenalty{
        :>> dataType = AttributeDataType::DOUBLE;
    }
}

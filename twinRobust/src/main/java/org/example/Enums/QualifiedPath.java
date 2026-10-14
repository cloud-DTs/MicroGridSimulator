package org.example.Enums;

// qualified names of the definitions and their features in the simulator model;
// features use DEFINITION__FEATURE
public enum QualifiedPath {

	// BatterySimulation
	BATTERY_SIMULATION("BatterySimulation::BatterySimulation"),
	BATTERY_SIMULATION_CONFIG("BatterySimulation::BatterySimulation_Config"),
	BATTERY_SIMULATION_CONFIG__INIT_SOC("BatterySimulation::BatterySimulation_Config::init_soc"),

	// BatteryTestBed
	BATTERY_TEST_BED("BatteryTestBed::BatteryTestBed"),
	BATTERY_TEST_BED_CONFIG("BatteryTestBed::BatteryTestBed_Config"),
	BATTERY_TEST_BED_CONFIG__MIN_CAPACITY("BatteryTestBed::BatteryTestBed_Config::min_capacity"),
	BATTERY_TEST_BED_CONFIG__MAX_CAPACITY("BatteryTestBed::BatteryTestBed_Config::max_capacity"),
	BATTERY_TEST_BED_CONFIG__MAX_CHARGE("BatteryTestBed::BatteryTestBed_Config::max_charge"),
	BATTERY_TEST_BED_CONFIG__MAX_DISCHARGE("BatteryTestBed::BatteryTestBed_Config::max_discharge"),
	BATTERY_TEST_BED_CONFIG__DISCHARGE_EFFICIENCY("BatteryTestBed::BatteryTestBed_Config::discharge_efficiency"),
	BATTERY_TEST_BED_CONFIG__CHARGE_EFFICIENCY("BatteryTestBed::BatteryTestBed_Config::charge_efficiency"),
	BATTERY_TEST_BED_CONFIG__OPERATING_TEMPERATURE("BatteryTestBed::BatteryTestBed_Config::operating_temperature"),
	BATTERY_TEST_BED_CONFIG__CONTROL("BatteryTestBed::BatteryTestBed_Config::control"),
	OPERATING_TEMPERATURE("BatteryTestBed::OperatingTemperature"),
	OPERATING_TEMPERATURE_VALUE("BatteryTestBed::OperatingTemperature_Value"),
	BATTERY_CONTROL("BatteryTestBed::BatteryControl"),
	BATTERY_CONTROL_GRID_FRIENDLY("BatteryTestBed::BatteryControl_GridFriendly"),
	BATTERY_CONTROL_GRID_FRIENDLY__CONTROL_ID("BatteryTestBed::BatteryControl_GridFriendly::control_ID"),

	// ChargingEventSimulation
	CHARGING_EVENT_SIMULATION("ChargingEventSimulation::ChargingEventSimulation"),
	CHARGING_EVENT_SIMULATION__NAME("ChargingEventSimulation::ChargingEventSimulation::name"),
	CHARGING_EVENT_SIMULATION__CAPACITY("ChargingEventSimulation::ChargingEventSimulation::capacity"),
	CHARGING_EVENT_SIMULATION__MAX_CHARGE("ChargingEventSimulation::ChargingEventSimulation::max_charge"),
	CHARGING_EVENT_SIMULATION__SOC("ChargingEventSimulation::ChargingEventSimulation::soc"),
	CHARGING_EVENT_SIMULATION__ARRIVAL_DATE("ChargingEventSimulation::ChargingEventSimulation::arrival_date"),
	CHARGING_EVENT_SIMULATION__DEPARTURE_DATE("ChargingEventSimulation::ChargingEventSimulation::departure_date"),
	CHARGING_EVENT_SIMULATION__PREFERENCE_WALLBOX("ChargingEventSimulation::ChargingEventSimulation::preference_wallbox"),

	// GridSimulation
	GRID_SIMULATION("GridSimulation::GridSimulation"),
	GRID_SIMULATION_CONFIG("GridSimulation::GridSimulation_Config"),
	GRID_SIMULATION_CONFIG__IMPORT_PRICE_MODE("GridSimulation::GridSimulation_Config::import_price_mode"),
	GRID_SIMULATION_CONFIG__IMPORT_PRICE_VALUE("GridSimulation::GridSimulation_Config::import_price_value"),
	GRID_SIMULATION_CONFIG__IMPORT_PRICE_ARRAY("GridSimulation::GridSimulation_Config::import_price_array"),
	GRID_SIMULATION_CONFIG__IMPORT_PRICE_PATH("GridSimulation::GridSimulation_Config::import_price_path"),
	GRID_SIMULATION_CONFIG__EXPORT_PRICE_MODE("GridSimulation::GridSimulation_Config::export_price_mode"),
	GRID_SIMULATION_CONFIG__EXPORT_PRICE_VALUE("GridSimulation::GridSimulation_Config::export_price_value"),
	GRID_SIMULATION_CONFIG__EXPORT_PRICE_ARRAY("GridSimulation::GridSimulation_Config::export_price_array"),
	GRID_SIMULATION_CONFIG__EXPORT_PRICE_PATH("GridSimulation::GridSimulation_Config::export_price_path"),
	GRID_SIMULATION_CONFIG__CO2_MODE("GridSimulation::GridSimulation_Config::co2_mode"),
	GRID_SIMULATION_CONFIG__CO2_VALUE("GridSimulation::GridSimulation_Config::co2_value"),
	GRID_SIMULATION_CONFIG__CO2_ARRAY("GridSimulation::GridSimulation_Config::co2_array"),
	GRID_SIMULATION_CONFIG__CO2_PATH("GridSimulation::GridSimulation_Config::co2_path"),
	GRID_SIMULATION_CONFIG__GRID_LIMIT("GridSimulation::GridSimulation_Config::grid_limit"),
	GRID_SIMULATION_CONFIG__PEAK_SHAVING_PENALTY_IN_EUR("GridSimulation::GridSimulation_Config::peak_shaving_penalty_in_EUR"),
	PEAK_SHAVING_PENALTY("GridSimulation::PeakShavingPenalty"),
	PEAK_SHAVING_PENALTY_VALUE("GridSimulation::PeakShavingPenalty_Value"),

	// GridTestBed
	GRID_TEST_BED("GridTestBed::GridTestBed"),
	GRID_TEST_BED_CONFIG("GridTestBed::GridTestBed_Config"),
	GRID_TEST_BED_CONFIG__GRID_STATUS("GridTestBed::GridTestBed_Config::grid_status"),
	GRID_TEST_BED_CONFIG__MAX_IMPORT("GridTestBed::GridTestBed_Config::max_import"),
	GRID_TEST_BED_CONFIG__MAX_EXPORT("GridTestBed::GridTestBed_Config::max_export"),

	// LoadSimulation
	LOAD_SIMULATION("LoadSimulation::LoadSimulation"),
	LOAD_CONSUMER("LoadSimulation::LoadConsumer"),
	LOAD_CONSUMER__LOAD_CONSUMPTION("LoadSimulation::LoadConsumer::load_consumption"),

	// MicroGridSimulation
	MICRO_GRID_SIMULATION("MicroGridSimulation::MicroGridSimulation"),
	MICRO_GRID_SIMULATION_CONFIG("MicroGridSimulation::MicroGridSimulation_Config"),
	MICRO_GRID_SIMULATION_CONFIG__UNBALANCED_MODULE("MicroGridSimulation::MicroGridSimulation_Config::unbalanced_module"),
	MICRO_GRID_SIMULATION_CONFIG__GENERATE_CHARGING_EVENTS("MicroGridSimulation::MicroGridSimulation_Config::generate_charging_events"),
	MICRO_GRID_SIMULATION_CONFIG__CHARGING_EVENTS("MicroGridSimulation::MicroGridSimulation_Config::charging_events"),
	CHARGING_EVENTS("MicroGridSimulation::ChargingEvents"),
	CHARGING_EVENTS_CONFIG("MicroGridSimulation::ChargingEvents_Config"),
	CHARGING_EVENTS_CONFIG__CAR("MicroGridSimulation::ChargingEvents_Config::car"),
	GENERATE_CHARGING_EVENTS("MicroGridSimulation::GenerateChargingEvents"),
	GENERATE_CHARGING_EVENTS_CONFIG("MicroGridSimulation::GenerateChargingEvents_Config"),
	GENERATE_CHARGING_EVENTS_CONFIG__NUMBER_CHARGING_EVENTS("MicroGridSimulation::GenerateChargingEvents_Config::number_charging_events"),
	GENERATE_CHARGING_EVENTS_CONFIG__MAX_CAPACITY("MicroGridSimulation::GenerateChargingEvents_Config::max_capacity"),
	GENERATE_CHARGING_EVENTS_CONFIG__MIN_CAPACITY("MicroGridSimulation::GenerateChargingEvents_Config::min_capacity"),
	GENERATE_CHARGING_EVENTS_CONFIG__MAX_CHARGE("MicroGridSimulation::GenerateChargingEvents_Config::max_charge"),
	GENERATE_CHARGING_EVENTS_CONFIG__MIN_CHARGE("MicroGridSimulation::GenerateChargingEvents_Config::min_charge"),
	GENERATE_CHARGING_EVENTS_CONFIG__MIN_SOC("MicroGridSimulation::GenerateChargingEvents_Config::min_soc"),
	GENERATE_CHARGING_EVENTS_CONFIG__MAX_SOC("MicroGridSimulation::GenerateChargingEvents_Config::max_soc"),
	GENERATE_CHARGING_EVENTS_CONFIG__EARLIEST_ARRIVAL("MicroGridSimulation::GenerateChargingEvents_Config::earliest_arrival"),
	GENERATE_CHARGING_EVENTS_CONFIG__LATEST_DEPARTURE("MicroGridSimulation::GenerateChargingEvents_Config::latest_departure"),
	GENERATE_CHARGING_EVENTS_CONFIG__MIN_CHARGING_LENGTH("MicroGridSimulation::GenerateChargingEvents_Config::min_charging_length"),
	GENERATE_CHARGING_EVENTS_CONFIG__MAX_CHARGING_LENGTH("MicroGridSimulation::GenerateChargingEvents_Config::max_charging_length"),
	GENERATE_CHARGING_EVENTS_CONFIG__WEEKEND_FREE("MicroGridSimulation::GenerateChargingEvents_Config::weekend_free"),

	// Model
	ST_TWIN("Model::ST_Twin"),
	ST_TWIN__TESTBED("Model::ST_Twin::testbed"),
	ST_TWIN__SIMULATION("Model::ST_Twin::simulation"),

	// PVSimulation
	PV_SIMULATION("PVSimulation::PVSimulation"),

	// PVTestBed
	PV_TEST_BED("PVTestBed::PVTestBed"),
	PV_TEST_BED_CONFIG("PVTestBed::PVTestBed_Config"),
	PV_TEST_BED_CONFIG__MAX_OUTPUT("PVTestBed::PVTestBed_Config::max_output"),
	PV_TEST_BED_CONFIG__MIN_OUTPUT("PVTestBed::PVTestBed_Config::min_output"),

	// Simulation
	SIMULATION("Simulation::Simulation"),
	SIMULATION__BATTERY("Simulation::Simulation::battery"),
	SIMULATION__PV("Simulation::Simulation::pv"),
	SIMULATION__GRID("Simulation::Simulation::grid"),
	SIMULATION__MICROGRID("Simulation::Simulation::microgrid"),
	SIMULATION__LOAD("Simulation::Simulation::load"),
	SIMULATION__INITIAL_VALUES("Simulation::Simulation::initial_values"),

	// SimulationMain
	SIMULATION_MAIN("SimulationMain::SimulationMain"),
	SIMULATION_MAIN__SIMULATION_NAME("SimulationMain::SimulationMain::simulationName"),
	SIMULATION_MAIN__START_TIME("SimulationMain::SimulationMain::start_time"),
	SIMULATION_MAIN__END_TIME("SimulationMain::SimulationMain::end_time"),
	SIMULATION_MAIN__TIMESTEP("SimulationMain::SimulationMain::timestep"),
	SIMULATION_MAIN__RANDOM_SEED("SimulationMain::SimulationMain::random_seed"),
	RANDOM_SEED("SimulationMain::RandomSeed"),
	RANDOM_SEED_VALUE("SimulationMain::RandomSeed_Value"),

	// TestBed
	TEST_BED("TestBed::TestBed"),
	TEST_BED__BATTERY("TestBed::TestBed::battery"),
	TEST_BED__PV("TestBed::TestBed::pv"),
	TEST_BED__GRID("TestBed::TestBed::grid"),
	TEST_BED__WALLBOXES("TestBed::TestBed::wallboxes"),

	// WallBoxTestBed
	WALL_BOXES("WallBoxTestBed::WallBoxes"),
	WALL_BOXES_CONFIG("WallBoxTestBed::WallBoxes_Config"),
	WALL_BOXES_CONFIG__WALLBOX("WallBoxTestBed::WallBoxes_Config::wallbox"),
	WALL_BOX_TEST_BED("WallBoxTestBed::WallBoxTestBed"),
	WALL_BOX_TEST_BED__NAME("WallBoxTestBed::WallBoxTestBed::name"),
	WALL_BOX_TEST_BED__MAX_POWER_OUTPUT("WallBoxTestBed::WallBoxTestBed::max_power_output"),
	PV_SIMULATION_CONFIG("PVSimulation::PVSimulation_Config"),
	PV_SIMULATION_CONFIG__PV_MODE("PVSimulation::PVSimulation_Config::pv_mode"),
	PV_SIMULATION_CONFIG__PV_VALUE("PVSimulation::PVSimulation_Config::pv_value"),
	PV_SIMULATION_CONFIG__PV_ARRAY("PVSimulation::PVSimulation_Config::pv_array"),
	PV_SIMULATION_CONFIG__PV_PATH("PVSimulation::PVSimulation_Config::pv_path"),
	PV_SIMULATION_CONFIG__PV_START_TIME("PVSimulation::PVSimulation_Config::pv_start_time"),
	PV_SIMULATION_CONFIG__PV_END_TIME("PVSimulation::PVSimulation_Config::pv_end_time"),
	PV_SIMULATION_CONFIG__NOISE_LEVEL("PVSimulation::PVSimulation_Config::noise_level"),
	LOAD_SIMULATION_CONFIG("LoadSimulation::LoadSimulation_Config"),
	LOAD_SIMULATION_CONFIG__LOAD_MODE("LoadSimulation::LoadSimulation_Config::load_mode"),
	LOAD_SIMULATION_CONFIG__LOAD_ARRAY("LoadSimulation::LoadSimulation_Config::load_array"),
	LOAD_SIMULATION_CONFIG__LOAD_PATH("LoadSimulation::LoadSimulation_Config::load_path"),
	LOAD_SIMULATION_CONFIG__CONSUMER("LoadSimulation::LoadSimulation_Config::consumer"),
	BATTERY_CONTROL_GRID_FRIENDLY__THRESHOLDS_MODE("BatteryTestBed::BatteryControl_GridFriendly::thresholds_mode"),
	BATTERY_CONTROL_GRID_FRIENDLY__THRESHOLDS_ARRAY("BatteryTestBed::BatteryControl_GridFriendly::thresholds_array"),
	BATTERY_CONTROL_GRID_FRIENDLY__THRESHOLDS_PATH("BatteryTestBed::BatteryControl_GridFriendly::thresholds_path"),
	BATTERY_CONTROL_GRID_FRIENDLY__THRESHOLD_STRATEGY("BatteryTestBed::BatteryControl_GridFriendly::threshold_strategy"),
	BATTERY_CONTROL_GRID_FRIENDLY__CO2_REWARD_MODE("BatteryTestBed::BatteryControl_GridFriendly::co2_reward_mode"),
	BATTERY_CONTROL_GRID_FRIENDLY__CO2_REWARD_ARRAY("BatteryTestBed::BatteryControl_GridFriendly::co2_reward_array"),
	BATTERY_CONTROL_GRID_FRIENDLY__CO2_REWARD_PATH("BatteryTestBed::BatteryControl_GridFriendly::co2_reward_path"),
	WALL_BOX_TEST_BED__CONTROL_MODE("WallBoxTestBed::WallBoxTestBed::control_mode"),
	WALL_BOX_TEST_BED__PV_THRESHOLD("WallBoxTestBed::WallBoxTestBed::pv_threshold"),
	WALL_BOX_TEST_BED__STRATEGY_MODE("WallBoxTestBed::WallBoxTestBed::strategy_mode");

	private final String qualifiedName;

	QualifiedPath(String qualifiedName) {
		this.qualifiedName = qualifiedName;
	}

	public String getQualifiedName() {
		return qualifiedName;
	}

	// last segment of the qualified name, as returned by getName() of the parser
	public String getName() {
		return qualifiedName.substring(qualifiedName.lastIndexOf("::") + 2);
	}
}
package org.example.SimulatorModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.Enums.QualifiedPath;
import org.example.Model.Attribute.ConstAttributeUsage;
import org.example.Model.Entity.EntityUsage;

// "simulation.grid"; a price variant left on its empty base is left out (simulator default 0)
public class SimulationGrid extends Abstract<EntityUsage> {

	// number, list of numbers or true (aWATTar)
	@JsonProperty("import_price")
	private Object importPrice;

	// number or list of numbers
	@JsonProperty("export_price")
	private Object exportPrice;


	@JsonProperty("co2_per_kWh")
	private Object co2PerKwh;

	@JsonProperty("grid_limit")
	private Long gridLimit;

	@JsonProperty("peak_shaving_penalty_in_EUR")
	private Double peakShavingPenalty;

	private final int steps;

	public SimulationGrid(ExperimentMapper experimentMapper, int steps) {
		super(experimentMapper);
		this.steps = steps;
	}

	@Override
	protected QualifiedPath getQualifiedPath() {
		return QualifiedPath.GRID_SIMULATION_CONFIG;
	}

	@Override
	protected Abstract<EntityUsage> map(EntityUsage element) {
		importPrice = value(attribute(element, QualifiedPath.GRID_SIMULATION_CONFIG__IMPORT_PRICE_CENT_PER_KWH),
				QualifiedPath.GRID_IMPORT_PRICE_CENT_PER_KWH_STATIC, QualifiedPath.GRID_IMPORT_PRICE_CENT_PER_KWH_TIME_SERIES_ARRAY,
				QualifiedPath.GRID_IMPORT_PRICE_CENT_PER_KWH_TIME_SERIES_PATH, QualifiedPath.GRID_IMPORT_PRICE_CENT_PER_KWH_REAL_TIME);
		exportPrice = value(attribute(element, QualifiedPath.GRID_SIMULATION_CONFIG__EXPORT_PRICE_CENT_PER_KWH),
				QualifiedPath.GRID_EXPORT_PRICE_CENT_PER_KWH_STATIC, QualifiedPath.GRID_EXPORT_PRICE_CENT_PER_KWH_TIME_SERIES_ARRAY,
				QualifiedPath.GRID_EXPORT_PRICE_CENT_PER_KWH_TIME_SERIES_PATH, null);
		co2PerKwh = value(attribute(element, QualifiedPath.GRID_SIMULATION_CONFIG__CO2_G_PER_KWH),
				QualifiedPath.GRID_CO2_G_PER_KWH_STATIC, QualifiedPath.GRID_CO2_G_PER_KWH_TIME_SERIES_ARRAY,
				QualifiedPath.GRID_CO2_G_PER_KWH_TIME_SERIES_PATH, QualifiedPath.GRID_CO2_G_PER_KWH_REAL_TIME);
		gridLimit = values.integer(attribute(element, QualifiedPath.GRID_SIMULATION_CONFIG__GRID_LIMIT));

		ConstAttributeUsage penalty = attribute(element, QualifiedPath.GRID_SIMULATION_CONFIG__PEAK_SHAVING_PENALTY_IN_EUR);
		if (is(penalty, QualifiedPath.PEAK_SHAVING_PENALTY_VALUE)) {
			peakShavingPenalty = values.decimal(penalty);
		}
		return this;
	}

	private Object value(ConstAttributeUsage attribute, QualifiedPath constant, QualifiedPath array, QualifiedPath path, QualifiedPath realTime) {
		if (is(attribute, constant)) return values.decimal(attribute);
		if (is(attribute, array)) return values.decimals(attribute);
		if (is(attribute, path)) return values.series(attribute, steps);
		if (realTime != null && is(attribute, realTime)) return Boolean.TRUE;
		return null;
	}
}

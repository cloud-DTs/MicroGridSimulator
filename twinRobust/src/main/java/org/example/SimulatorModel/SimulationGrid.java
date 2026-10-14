package org.example.SimulatorModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.Enums.QualifiedPath;
import org.example.Model.Attribute.ConstAttributeUsage;
import org.example.Model.Entity.EntityUsage;

public class SimulationGrid extends Abstract<EntityUsage> {

	@JsonProperty("import_price")
	private Object importPrice;

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
		importPrice = value(element, true, QualifiedPath.GRID_SIMULATION_CONFIG__IMPORT_PRICE_MODE,
				QualifiedPath.GRID_SIMULATION_CONFIG__IMPORT_PRICE_VALUE, QualifiedPath.GRID_SIMULATION_CONFIG__IMPORT_PRICE_ARRAY,
				QualifiedPath.GRID_SIMULATION_CONFIG__IMPORT_PRICE_PATH);
		exportPrice = value(element, false, QualifiedPath.GRID_SIMULATION_CONFIG__EXPORT_PRICE_MODE,
				QualifiedPath.GRID_SIMULATION_CONFIG__EXPORT_PRICE_VALUE, QualifiedPath.GRID_SIMULATION_CONFIG__EXPORT_PRICE_ARRAY,
				QualifiedPath.GRID_SIMULATION_CONFIG__EXPORT_PRICE_PATH);
		co2PerKwh = value(element, true, QualifiedPath.GRID_SIMULATION_CONFIG__CO2_MODE,
				QualifiedPath.GRID_SIMULATION_CONFIG__CO2_VALUE, QualifiedPath.GRID_SIMULATION_CONFIG__CO2_ARRAY,
				QualifiedPath.GRID_SIMULATION_CONFIG__CO2_PATH);
		gridLimit = values.integer(attribute(element, QualifiedPath.GRID_SIMULATION_CONFIG__GRID_LIMIT));

		ConstAttributeUsage penalty = attribute(element, QualifiedPath.GRID_SIMULATION_CONFIG__PEAK_SHAVING_PENALTY_IN_EUR);
		if (is(penalty, QualifiedPath.PEAK_SHAVING_PENALTY_VALUE)) {
			peakShavingPenalty = values.decimal(penalty);
		}
		return this;
	}

	private Object value(EntityUsage element, boolean realTimeAllowed, QualifiedPath mode,
			QualifiedPath constant, QualifiedPath array, QualifiedPath path) {
		String selected = realTimeAllowed ? mode(element, mode, "STATIC", "ARRAY", "PATH", "REALTIME") : mode(element, mode, "STATIC", "ARRAY", "PATH");
		return switch (selected) {
			case "STATIC" -> values.decimal(required(element, mode, selected, constant));
			case "ARRAY" -> values.decimals(required(element, mode, selected, array));
			case "PATH" -> values.series(required(element, mode, selected, path), steps);
			default -> Boolean.TRUE;
		};
	}
}

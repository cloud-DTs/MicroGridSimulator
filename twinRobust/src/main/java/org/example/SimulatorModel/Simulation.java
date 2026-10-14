package org.example.SimulatorModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.Enums.QualifiedPath;
import org.example.Model.Entity.EntityUsage;

import java.util.List;

public class Simulation extends Abstract<EntityUsage> {

	@JsonProperty("battery")
	private SimulationBattery battery;

	@JsonProperty("pv")
	private Object pv;

	@JsonProperty("grid")
	private SimulationGrid grid;

	@JsonProperty("load")
	private Object load;

	@JsonProperty("microgrid")
	private Microgrid microgrid;

	@JsonProperty("initial_values")
	private InitialValues initialValues;

	private final int steps;

	public Simulation(ExperimentMapper experimentMapper, int steps) {
		super(experimentMapper);
		this.steps = steps;
	}

	public boolean hasPvGenerator() {
		return PvGenerator.class.isInstance(pv);
	}

	@Override
	protected QualifiedPath getQualifiedPath() {
		return QualifiedPath.SIMULATION;
	}

	@Override
	protected Abstract<EntityUsage> map(EntityUsage element) {
		List<EntityUsage> entities = element.getEntities();
		battery = new SimulationBattery(experimentMapper).parse(entity(entities, QualifiedPath.SIMULATION__BATTERY)).map(SimulationBattery.class::cast).orElse(null);
		pv = pv(entity(entities, QualifiedPath.SIMULATION__PV));
		grid = new SimulationGrid(experimentMapper, steps).parse(entity(entities, QualifiedPath.SIMULATION__GRID)).map(SimulationGrid.class::cast).orElse(null);
		load = load(entity(entities, QualifiedPath.SIMULATION__LOAD));
		microgrid = new Microgrid(experimentMapper).parse(entity(entities, QualifiedPath.SIMULATION__MICROGRID)).map(Microgrid.class::cast).orElse(null);
		initialValues = new InitialValues(experimentMapper).parse(entity(entities, QualifiedPath.SIMULATION__INITIAL_VALUES)).map(InitialValues.class::cast).orElseThrow();
		return this;
	}

	private Object pv(EntityUsage entity) {
		if (isLibrary(entity) || !is(entity, QualifiedPath.PV_SIMULATION_CONFIG)) {
			return null;
		}
		QualifiedPath mode = QualifiedPath.PV_SIMULATION_CONFIG__PV_MODE;
		String selected = mode(entity, mode, "STATIC", "ARRAY", "PATH", "GENERATOR");
		return switch (selected) {
			case "STATIC" -> values.decimal(required(entity, mode, selected, QualifiedPath.PV_SIMULATION_CONFIG__PV_VALUE));
			case "ARRAY" -> values.decimals(required(entity, mode, selected, QualifiedPath.PV_SIMULATION_CONFIG__PV_ARRAY));
			case "PATH" -> values.series(required(entity, mode, selected, QualifiedPath.PV_SIMULATION_CONFIG__PV_PATH), steps);
			default -> new PvGenerator(experimentMapper).parse(entity).orElseThrow();
		};
	}

	private Object load(EntityUsage entity) {
		if (isLibrary(entity) || !is(entity, QualifiedPath.LOAD_SIMULATION_CONFIG)) {
			return null;
		}
		QualifiedPath mode = QualifiedPath.LOAD_SIMULATION_CONFIG__LOAD_MODE;
		String selected = mode(entity, mode, "STATIC", "ARRAY", "PATH");
		return switch (selected) {
			case "ARRAY" -> values.decimals(required(entity, mode, selected, QualifiedPath.LOAD_SIMULATION_CONFIG__LOAD_ARRAY));
			case "PATH" -> values.series(required(entity, mode, selected, QualifiedPath.LOAD_SIMULATION_CONFIG__LOAD_PATH), steps);
			default -> entries(entity, QualifiedPath.LOAD_SIMULATION_CONFIG__CONSUMER).stream()
					.map(consumer -> new LoadConsumer(experimentMapper).parse(consumer).map(LoadConsumer.class::cast).orElseThrow())
					.toList();
		};
	}
}

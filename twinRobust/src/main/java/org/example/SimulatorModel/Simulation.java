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

	// empty PVSimulation: no "pv"
	private Object pv(EntityUsage entity) {
		if (is(entity, QualifiedPath.PV_SIMULATION_STATIC)) {
			return values.decimal(attribute(entity, QualifiedPath.PV_SIMULATION_STATIC__PV_VALUE));
		}
		if (is(entity, QualifiedPath.PV_SIMULATION_TIME_SERIES_ARRAY)) {
			return values.decimals(attribute(entity, QualifiedPath.PV_SIMULATION_TIME_SERIES_ARRAY__PV_TIMESERIES));
		}
		if (is(entity, QualifiedPath.PV_SIMULATION_TIME_SERIES_PATH)) {
			return values.series(attribute(entity, QualifiedPath.PV_SIMULATION_TIME_SERIES_PATH__PV_TIMESERIES_PATH), steps);
		}
		return new PvGenerator(experimentMapper).parse(entity).orElse(null);
	}

	// empty LoadSimulation: no "load"
	private Object load(EntityUsage entity) {
		if (is(entity, QualifiedPath.LOAD_SIMULATION_TIME_SERIES_ARRAY)) {
			return values.decimals(attribute(entity, QualifiedPath.LOAD_SIMULATION_TIME_SERIES_ARRAY__LOAD_TIMESERIES));
		}
		if (is(entity, QualifiedPath.LOAD_SIMULATION_TIME_SERIES_PATH)) {
			return values.series(attribute(entity, QualifiedPath.LOAD_SIMULATION_TIME_SERIES_PATH__LOAD_TIMESERIES_PATH), steps);
		}
		if (is(entity, QualifiedPath.LOAD_SIMULATION_STATIC)) {
			return entries(entity, QualifiedPath.LOAD_SIMULATION_STATIC__CONSUMER).stream()
					.map(consumer -> new LoadConsumer(experimentMapper).parse(consumer).map(LoadConsumer.class::cast).orElseThrow())
					.toList();
		}
		return null;
	}
}

package org.example.SimulatorModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.Enums.QualifiedPath;
import org.example.Model.Entity.EntityUsage;
import org.example.Model.Twin.TwinDefinition;

// the simulator payload of one twin: {"testbed": .., "simulation": ..}
public class Model extends Abstract<TwinDefinition> {

	@JsonProperty("testbed")
	private TestBed testbed;

	@JsonProperty("simulation")
	private Simulation simulation;

	private TwinDefinition twin;

	public Model(ExperimentMapper experimentMapper) {
		super(experimentMapper);
	}

	public TwinDefinition getTwin() {
		return twin;
	}

	@Override
	protected QualifiedPath getQualifiedPath() {
		return QualifiedPath.ST_TWIN;
	}

	@Override
	protected Abstract<TwinDefinition> map(TwinDefinition element) {
		twin = element;
		EntityUsage testbedEntity = entity(element.getEntities(), QualifiedPath.ST_TWIN__TESTBED);
		EntityUsage simulationEntity = entity(element.getEntities(), QualifiedPath.ST_TWIN__SIMULATION);


		int steps = new InitialValues(experimentMapper)
				.parse(entity(simulationEntity.getEntities(), QualifiedPath.SIMULATION__INITIAL_VALUES))
				.map(InitialValues.class::cast).orElseThrow(() -> new IllegalStateException("Twin '" + element.getName() + "' has no initial_values"))
				.steps();

		testbed = new TestBed(experimentMapper, steps).parse(testbedEntity).map(TestBed.class::cast).orElseThrow();
		simulation = new Simulation(experimentMapper, steps).parse(simulationEntity).map(Simulation.class::cast).orElseThrow();

		if (testbed.hasPv() != simulation.hasPvGenerator()) {
			throw new IllegalArgumentException("Twin '" + element.getName() + "': testbed.pv requires simulation.pv to be PVSimulation_Generator and the other way round");
		}
		return this;
	}
}

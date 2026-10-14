package org.example.SimulatorModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.Enums.QualifiedPath;
import org.example.Model.Entity.EntityUsage;


public class TestbedPv extends Abstract<EntityUsage> {

	@JsonProperty("max_output")
	private Long maxOutput;

	@JsonProperty("min_output")
	private Long minOutput;

	public TestbedPv(ExperimentMapper experimentMapper) {
		super(experimentMapper);
	}

	@Override
	protected QualifiedPath getQualifiedPath() {
		return QualifiedPath.PV_TEST_BED_CONFIG;
	}

	@Override
	protected Abstract<EntityUsage> map(EntityUsage element) {
		maxOutput = values.integer(attribute(element, QualifiedPath.PV_TEST_BED_CONFIG__MAX_OUTPUT));
		minOutput = values.integer(attribute(element, QualifiedPath.PV_TEST_BED_CONFIG__MIN_OUTPUT));
		return this;
	}
}

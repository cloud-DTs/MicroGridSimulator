package org.example.SimulatorModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.Enums.QualifiedPath;
import org.example.Model.Entity.EntityUsage;


public class TestbedWallbox extends Abstract<EntityUsage> {

	@JsonProperty("name")
	private String name;

	@JsonProperty("max_power_output")
	private Long maxPowerOutput;

	@JsonProperty("control")
	private WallboxControl control;

	public TestbedWallbox(ExperimentMapper experimentMapper) {
		super(experimentMapper);
	}

	@Override
	protected QualifiedPath getQualifiedPath() {
		return QualifiedPath.WALL_BOX_TEST_BED;
	}

	@Override
	protected Abstract<EntityUsage> map(EntityUsage element) {
		name = values.text(attribute(element, QualifiedPath.WALL_BOX_TEST_BED__NAME));
		maxPowerOutput = values.integer(attribute(element, QualifiedPath.WALL_BOX_TEST_BED__MAX_POWER_OUTPUT));
		control = new WallboxControl(experimentMapper).parse(element).map(WallboxControl.class::cast).orElse(null);
		return this;
	}
}

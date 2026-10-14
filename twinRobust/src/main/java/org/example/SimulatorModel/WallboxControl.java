package org.example.SimulatorModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import Model.AbstractType;
import org.example.Enums.QualifiedPath;
import org.example.Model.Entity.EntityUsage;

public class WallboxControl extends Abstract<EntityUsage> {

	@JsonProperty("ID")
	private String id;

	@JsonProperty("pv_threshold")
	private Long pvThreshold;

	@JsonProperty("strategy_mode")
	private String strategyMode;

	public WallboxControl(ExperimentMapper experimentMapper) {
		super(experimentMapper);
	}

	@Override
	protected QualifiedPath getQualifiedPath() {
		return QualifiedPath.WALL_BOX_TEST_BED;
	}

	@Override
	public boolean isConstructable(AbstractType<?, ?> element) {
		return super.isConstructable(element) && EntityUsage.class.isInstance(element)
				&& !mode(EntityUsage.class.cast(element), QualifiedPath.WALL_BOX_TEST_BED__CONTROL_MODE, "NONE", "CONSTANT", "ADAPTIVE_PROPORTIONAL_UNIFORM").equals("NONE");
	}

	@Override
	protected Abstract<EntityUsage> map(EntityUsage element) {
		id = switch (mode(element, QualifiedPath.WALL_BOX_TEST_BED__CONTROL_MODE, "CONSTANT", "ADAPTIVE_PROPORTIONAL_UNIFORM")) {
			case "CONSTANT" -> "constant-wallbox-strategy";
			default -> "adaptive-proportional-uniform-wallbox-strategy";
		};
		pvThreshold = values.integer(attribute(element, QualifiedPath.WALL_BOX_TEST_BED__PV_THRESHOLD));
		strategyMode = values.text(attribute(element, QualifiedPath.WALL_BOX_TEST_BED__STRATEGY_MODE));
		return this;
	}
}

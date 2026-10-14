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
		return QualifiedPath.WALL_BOX_CONTROL;
	}

	@Override
	public boolean isConstructable(AbstractType<?, ?> element) {
		return !isLibrary(element) && (is(element, QualifiedPath.WALL_BOX_CONTROL_CONSTANT) || is(element, QualifiedPath.WALL_BOX_CONTROL_ADAPTIVE_PROPORTIONAL_UNIFORM));
	}

	@Override
	protected Abstract<EntityUsage> map(EntityUsage element) {
		if (is(element, QualifiedPath.WALL_BOX_CONTROL_CONSTANT)) {
			id = values.text(attribute(element, QualifiedPath.WALL_BOX_CONTROL_CONSTANT__CONTROL_ID));
			pvThreshold = values.integer(attribute(element, QualifiedPath.WALL_BOX_CONTROL_CONSTANT__PV_THRESHOLD));
			strategyMode = values.text(attribute(element, QualifiedPath.WALL_BOX_CONTROL_CONSTANT__STRATEGY_MODE));
		} else {
			id = values.text(attribute(element, QualifiedPath.WALL_BOX_CONTROL_ADAPTIVE_PROPORTIONAL_UNIFORM__CONTROL_ID));
			pvThreshold = values.integer(attribute(element, QualifiedPath.WALL_BOX_CONTROL_ADAPTIVE_PROPORTIONAL_UNIFORM__PV_THRESHOLD));
			strategyMode = values.text(attribute(element, QualifiedPath.WALL_BOX_CONTROL_ADAPTIVE_PROPORTIONAL_UNIFORM__STRATEGY_MODE));
		}
		return this;
	}
}

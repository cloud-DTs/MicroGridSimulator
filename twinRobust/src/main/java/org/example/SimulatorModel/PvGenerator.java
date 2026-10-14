package org.example.SimulatorModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.Enums.QualifiedPath;
import org.example.Model.Entity.EntityUsage;


public class PvGenerator extends Abstract<EntityUsage> {

	@JsonProperty("pv_start_time")
	private String pvStartTime;

	@JsonProperty("pv_end_time")
	private String pvEndTime;

	@JsonProperty("noise_level")
	private Double noiseLevel;

	public PvGenerator(ExperimentMapper experimentMapper) {
		super(experimentMapper);
	}

	@Override
	protected QualifiedPath getQualifiedPath() {
		return QualifiedPath.PV_SIMULATION_GENERATOR;
	}

	@Override
	protected Abstract<EntityUsage> map(EntityUsage element) {
		pvStartTime = values.text(attribute(element, QualifiedPath.PV_SIMULATION_GENERATOR__PV_START_TIME));
		pvEndTime = values.text(attribute(element, QualifiedPath.PV_SIMULATION_GENERATOR__PV_END_TIME));
		noiseLevel = values.decimal(attribute(element, QualifiedPath.PV_SIMULATION_GENERATOR__NOISE_LEVEL));
		return this;
	}
}

package org.example.SimulatorModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.Enums.QualifiedPath;
import org.example.Model.Entity.EntityUsage;


public class SimulationBattery extends Abstract<EntityUsage> {


	@JsonProperty("init_soc")
	private Double initSoc;

	public SimulationBattery(ExperimentMapper experimentMapper) {
		super(experimentMapper);
	}

	@Override
	protected QualifiedPath getQualifiedPath() {
		return QualifiedPath.BATTERY_SIMULATION_CONFIG;
	}

	@Override
	protected Abstract<EntityUsage> map(EntityUsage element) {
		initSoc = values.decimal(attribute(element, QualifiedPath.BATTERY_SIMULATION_CONFIG__INIT_SOC));
		return this;
	}
}

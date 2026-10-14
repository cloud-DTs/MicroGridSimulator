package org.example.SimulatorModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.Enums.QualifiedPath;
import org.example.Model.Entity.EntityUsage;


public class LoadConsumer extends Abstract<EntityUsage> {

	@JsonProperty("load_consumption")
	private Double loadConsumption;

	public LoadConsumer(ExperimentMapper experimentMapper) {
		super(experimentMapper);
	}

	@Override
	protected QualifiedPath getQualifiedPath() {
		return QualifiedPath.LOAD_CONSUMER;
	}

	@Override
	protected Abstract<EntityUsage> map(EntityUsage element) {
		loadConsumption = values.decimal(attribute(element, QualifiedPath.LOAD_CONSUMER__LOAD_CONSUMPTION));
		return this;
	}
}

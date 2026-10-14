package org.example.SimulatorModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.Enums.QualifiedPath;
import org.example.Model.Entity.EntityUsage;

import java.util.List;

public class ChargingEvent extends Abstract<EntityUsage> {

	@JsonProperty("capacity")
	private Long capacity;

	@JsonProperty("max_charge")
	private Long maxCharge;

	@JsonProperty("soc")
	private Double soc;

	@JsonProperty("arrival_date")
	private String arrivalDate;

	@JsonProperty("departure_date")
	private String departureDate;

	@JsonProperty("name")
	private String name;

	@JsonProperty("preference_wallbox")
	private List<String> preferenceWallbox;

	public ChargingEvent(ExperimentMapper experimentMapper) {
		super(experimentMapper);
	}

	@Override
	protected QualifiedPath getQualifiedPath() {
		return QualifiedPath.CHARGING_EVENT_SIMULATION;
	}

	@Override
	protected Abstract<EntityUsage> map(EntityUsage element) {
		capacity = values.integer(attribute(element, QualifiedPath.CHARGING_EVENT_SIMULATION__CAPACITY));
		maxCharge = values.integer(attribute(element, QualifiedPath.CHARGING_EVENT_SIMULATION__MAX_CHARGE));
		soc = values.decimal(attribute(element, QualifiedPath.CHARGING_EVENT_SIMULATION__SOC));
		arrivalDate = values.text(attribute(element, QualifiedPath.CHARGING_EVENT_SIMULATION__ARRIVAL_DATE));
		departureDate = values.text(attribute(element, QualifiedPath.CHARGING_EVENT_SIMULATION__DEPARTURE_DATE));
		name = values.text(attribute(element, QualifiedPath.CHARGING_EVENT_SIMULATION__NAME));
		preferenceWallbox = values.texts(attribute(element, QualifiedPath.CHARGING_EVENT_SIMULATION__PREFERENCE_WALLBOX));
		return this;
	}
}

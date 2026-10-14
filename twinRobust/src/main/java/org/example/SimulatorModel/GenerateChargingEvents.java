package org.example.SimulatorModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.Enums.QualifiedPath;
import org.example.Model.Entity.EntityUsage;


public class GenerateChargingEvents extends Abstract<EntityUsage> {

	@JsonProperty("number_charging_events")
	private Long numberChargingEvents;

	@JsonProperty("max_capacity")
	private Long maxCapacity;

	@JsonProperty("min_capacity")
	private Long minCapacity;

	@JsonProperty("max_charge")
	private Long maxCharge;

	@JsonProperty("min_charge")
	private Long minCharge;

	@JsonProperty("min_soc")
	private Double minSoc;

	@JsonProperty("max_soc")
	private Double maxSoc;

	@JsonProperty("earliest_arrival")
	private String earliestArrival;

	@JsonProperty("latest_departure")
	private String latestDeparture;

	@JsonProperty("min_charging_length")
	private String minChargingLength;

	@JsonProperty("max_charging_length")
	private String maxChargingLength;

	@JsonProperty("weekend_free")
	private Boolean weekendFree;

	public GenerateChargingEvents(ExperimentMapper experimentMapper) {
		super(experimentMapper);
	}

	@Override
	protected QualifiedPath getQualifiedPath() {
		return QualifiedPath.GENERATE_CHARGING_EVENTS_CONFIG;
	}

	@Override
	protected Abstract<EntityUsage> map(EntityUsage element) {
		numberChargingEvents = values.integer(attribute(element, QualifiedPath.GENERATE_CHARGING_EVENTS_CONFIG__NUMBER_CHARGING_EVENTS));
		maxCapacity = values.integer(attribute(element, QualifiedPath.GENERATE_CHARGING_EVENTS_CONFIG__MAX_CAPACITY));
		minCapacity = values.integer(attribute(element, QualifiedPath.GENERATE_CHARGING_EVENTS_CONFIG__MIN_CAPACITY));
		maxCharge = values.integer(attribute(element, QualifiedPath.GENERATE_CHARGING_EVENTS_CONFIG__MAX_CHARGE));
		minCharge = values.integer(attribute(element, QualifiedPath.GENERATE_CHARGING_EVENTS_CONFIG__MIN_CHARGE));
		minSoc = values.decimal(attribute(element, QualifiedPath.GENERATE_CHARGING_EVENTS_CONFIG__MIN_SOC));
		maxSoc = values.decimal(attribute(element, QualifiedPath.GENERATE_CHARGING_EVENTS_CONFIG__MAX_SOC));
		earliestArrival = values.text(attribute(element, QualifiedPath.GENERATE_CHARGING_EVENTS_CONFIG__EARLIEST_ARRIVAL));
		latestDeparture = values.text(attribute(element, QualifiedPath.GENERATE_CHARGING_EVENTS_CONFIG__LATEST_DEPARTURE));
		minChargingLength = values.text(attribute(element, QualifiedPath.GENERATE_CHARGING_EVENTS_CONFIG__MIN_CHARGING_LENGTH));
		maxChargingLength = values.text(attribute(element, QualifiedPath.GENERATE_CHARGING_EVENTS_CONFIG__MAX_CHARGING_LENGTH));
		weekendFree = values.bool(attribute(element, QualifiedPath.GENERATE_CHARGING_EVENTS_CONFIG__WEEKEND_FREE));
		return this;
	}
}

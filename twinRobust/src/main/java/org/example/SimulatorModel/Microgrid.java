package org.example.SimulatorModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.Enums.QualifiedPath;
import org.example.Model.Entity.EntityUsage;

import java.util.List;


public class Microgrid extends Abstract<EntityUsage> {

	@JsonProperty("unbalanced_module")
	private Boolean unbalancedModule;

	@JsonProperty("generate_charging_events")
	private GenerateChargingEvents generateChargingEvents;

	@JsonProperty("charging_events")
	private List<ChargingEvent> chargingEvents;

	public Microgrid(ExperimentMapper experimentMapper) {
		super(experimentMapper);
	}

	@Override
	protected QualifiedPath getQualifiedPath() {
		return QualifiedPath.MICRO_GRID_SIMULATION_CONFIG;
	}

	@Override
	protected Abstract<EntityUsage> map(EntityUsage element) {
		unbalancedModule = values.bool(attribute(element, QualifiedPath.MICRO_GRID_SIMULATION_CONFIG__UNBALANCED_MODULE));
		generateChargingEvents = new GenerateChargingEvents(experimentMapper)
				.parse(entity(element.getEntities(), QualifiedPath.MICRO_GRID_SIMULATION_CONFIG__GENERATE_CHARGING_EVENTS))
				.map(GenerateChargingEvents.class::cast).orElse(null);

		EntityUsage events = entity(element.getEntities(), QualifiedPath.MICRO_GRID_SIMULATION_CONFIG__CHARGING_EVENTS);
		if (is(events, QualifiedPath.CHARGING_EVENTS_CONFIG)) {
			chargingEvents = entries(events, QualifiedPath.CHARGING_EVENTS_CONFIG__CAR).stream()
					.map(car -> new ChargingEvent(experimentMapper).parse(car).map(ChargingEvent.class::cast).orElseThrow())
					.toList();
		}
		return this;
	}
}

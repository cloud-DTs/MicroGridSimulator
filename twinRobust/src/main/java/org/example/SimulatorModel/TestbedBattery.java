package org.example.SimulatorModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.Enums.QualifiedPath;
import org.example.Model.Attribute.ConstAttributeUsage;
import org.example.Model.Entity.EntityUsage;

import java.util.List;


public class TestbedBattery extends Abstract<EntityUsage> {

	@JsonProperty("min_capacity")
	private Long minCapacity;

	@JsonProperty("max_capacity")
	private Long maxCapacity;

	@JsonProperty("max_charge")
	private Long maxCharge;

	@JsonProperty("max_discharge")
	private Long maxDischarge;

	@JsonProperty("discharge_efficiency")
	private Double dischargeEfficiency;

	@JsonProperty("charge_efficiency")
	private Double chargeEfficiency;

	@JsonProperty("operating_temperature")
	private List<Long> operatingTemperature;

	@JsonProperty("control")
	private BatteryControl control;

	private final int steps;

	public TestbedBattery(ExperimentMapper experimentMapper, int steps) {
		super(experimentMapper);
		this.steps = steps;
	}

	@Override
	protected QualifiedPath getQualifiedPath() {
		return QualifiedPath.BATTERY_TEST_BED_CONFIG;
	}

	@Override
	protected Abstract<EntityUsage> map(EntityUsage element) {
		minCapacity = values.integer(attribute(element, QualifiedPath.BATTERY_TEST_BED_CONFIG__MIN_CAPACITY));
		maxCapacity = values.integer(attribute(element, QualifiedPath.BATTERY_TEST_BED_CONFIG__MAX_CAPACITY));
		maxCharge = values.integer(attribute(element, QualifiedPath.BATTERY_TEST_BED_CONFIG__MAX_CHARGE));
		maxDischarge = values.integer(attribute(element, QualifiedPath.BATTERY_TEST_BED_CONFIG__MAX_DISCHARGE));
		dischargeEfficiency = values.decimal(attribute(element, QualifiedPath.BATTERY_TEST_BED_CONFIG__DISCHARGE_EFFICIENCY));
		chargeEfficiency = values.decimal(attribute(element, QualifiedPath.BATTERY_TEST_BED_CONFIG__CHARGE_EFFICIENCY));

		ConstAttributeUsage temperature = attribute(element, QualifiedPath.BATTERY_TEST_BED_CONFIG__OPERATING_TEMPERATURE);
		if (is(temperature, QualifiedPath.OPERATING_TEMPERATURE_VALUE)) {
			operatingTemperature = values.integers(temperature);
		}
		control = new BatteryControl(experimentMapper, steps).parse(entity(element.getEntities(), QualifiedPath.BATTERY_TEST_BED_CONFIG__CONTROL))
				.map(BatteryControl.class::cast).orElse(null);
		return this;
	}
}

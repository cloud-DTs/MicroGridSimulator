package org.example.SimulatorModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.Enums.QualifiedPath;
import org.example.Model.Attribute.ConstAttributeUsage;
import org.example.Model.Entity.EntityUsage;

import java.util.List;


public class BatteryControl extends Abstract<EntityUsage> {

	@JsonProperty("ID")
	private String id;

	@JsonProperty("thresholds")
	private List<Long> thresholds;

	@JsonProperty("dynamic_threshold_strategy")
	private String dynamicThresholdStrategy;

	@JsonProperty("co2_reward")
	private List<Double> co2Reward;

	private final int steps;

	public BatteryControl(ExperimentMapper experimentMapper, int steps) {
		super(experimentMapper);
		this.steps = steps;
	}

	@Override
	protected QualifiedPath getQualifiedPath() {
		return QualifiedPath.BATTERY_CONTROL_GRID_FRIENDLY;
	}

	@Override
	protected Abstract<EntityUsage> map(EntityUsage element) {
		id = values.text(attribute(element, QualifiedPath.BATTERY_CONTROL_GRID_FRIENDLY__CONTROL_ID));

		ConstAttributeUsage thresholdAttribute = attribute(element, QualifiedPath.BATTERY_CONTROL_GRID_FRIENDLY__THRESHOLDS);
		if (is(thresholdAttribute, QualifiedPath.THRESHOLDS_VALUE)) {
			thresholds = values.integers(thresholdAttribute);
		} else if (is(thresholdAttribute, QualifiedPath.THRESHOLDS_PATH)) {
			thresholds = values.thresholdsFile(thresholdAttribute);
		}

		EntityUsage strategy = entity(element.getEntities(), QualifiedPath.BATTERY_CONTROL_GRID_FRIENDLY__THRESHOLD_STRATEGY);
		if (is(strategy, QualifiedPath.THRESHOLD_STRATEGY_TIME_BASED)) {
			dynamicThresholdStrategy = "time_based";
		} else if (is(strategy, QualifiedPath.THRESHOLD_STRATEGY_CO2_BASED_TIME_SERIES_ARRAY)) {
			dynamicThresholdStrategy = "co2_based";
			co2Reward = values.decimals(attribute(strategy, QualifiedPath.THRESHOLD_STRATEGY_CO2_BASED_TIME_SERIES_ARRAY__CO2_REWARD));
		} else if (is(strategy, QualifiedPath.THRESHOLD_STRATEGY_CO2_BASED_TIME_SERIES_PATH)) {
			dynamicThresholdStrategy = "co2_based";
			co2Reward = values.series(attribute(strategy, QualifiedPath.THRESHOLD_STRATEGY_CO2_BASED_TIME_SERIES_PATH__CO2_REWARD_PATH), steps);
		}
		return this;
	}
}

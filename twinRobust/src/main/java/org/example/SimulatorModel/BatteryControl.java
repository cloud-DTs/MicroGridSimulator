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

		QualifiedPath thresholdsMode = QualifiedPath.BATTERY_CONTROL_GRID_FRIENDLY__THRESHOLDS_MODE;
		String thresholdsSelected = mode(element, thresholdsMode, "ARRAY", "PATH");
		thresholds = thresholdsSelected.equals("ARRAY")
				? values.integers(required(element, thresholdsMode, thresholdsSelected, QualifiedPath.BATTERY_CONTROL_GRID_FRIENDLY__THRESHOLDS_ARRAY))
				: values.thresholdsFile(required(element, thresholdsMode, thresholdsSelected, QualifiedPath.BATTERY_CONTROL_GRID_FRIENDLY__THRESHOLDS_PATH));

		// NONE: static thresholds, the key is left out (same as null for the simulator)
		String strategy = mode(element, QualifiedPath.BATTERY_CONTROL_GRID_FRIENDLY__THRESHOLD_STRATEGY, "NONE", "TIME_BASED", "CO2_BASED");
		if (strategy.equals("TIME_BASED")) {
			dynamicThresholdStrategy = "time_based";
		} else if (strategy.equals("CO2_BASED")) {
			dynamicThresholdStrategy = "co2_based";
			QualifiedPath rewardMode = QualifiedPath.BATTERY_CONTROL_GRID_FRIENDLY__CO2_REWARD_MODE;
			String rewardSelected = mode(element, rewardMode, "ARRAY", "PATH");
			co2Reward = rewardSelected.equals("ARRAY")
					? values.decimals(required(element, rewardMode, rewardSelected, QualifiedPath.BATTERY_CONTROL_GRID_FRIENDLY__CO2_REWARD_ARRAY))
					: values.series(required(element, rewardMode, rewardSelected, QualifiedPath.BATTERY_CONTROL_GRID_FRIENDLY__CO2_REWARD_PATH), steps);
		}
		return this;
	}
}

package org.example.SimulatorModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.Enums.QualifiedPath;
import org.example.Model.Attribute.ConstAttributeUsage;
import org.example.Model.Entity.EntityUsage;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class InitialValues extends Abstract<EntityUsage> {


	@JsonProperty("TIMESTEP")
	private Long timestep;

	@JsonProperty("SIMULATION_START_TIME")
	private String start;

	@JsonProperty("SIMULATION_END_TIME")
	private String end;

	@JsonProperty("random_seed")
	private Long randomSeed;

	private int steps;

	public InitialValues(ExperimentMapper experimentMapper) {
		super(experimentMapper);
	}

	public int steps() {
		return steps;
	}

	@Override
	protected QualifiedPath getQualifiedPath() {
		return QualifiedPath.SIMULATION_MAIN;
	}

	@Override
	protected Abstract<EntityUsage> map(EntityUsage element) {
		timestep = values.integer(attribute(element, QualifiedPath.SIMULATION_MAIN__TIMESTEP));
		start = values.text(attribute(element, QualifiedPath.SIMULATION_MAIN__START_TIME));
		end = values.text(attribute(element, QualifiedPath.SIMULATION_MAIN__END_TIME));

		ConstAttributeUsage seed = attribute(element, QualifiedPath.SIMULATION_MAIN__RANDOM_SEED);
		if (is(seed, QualifiedPath.RANDOM_SEED_VALUE)) {
			randomSeed = values.integer(seed);
		}

		try {
			long seconds = Duration.between(LocalDateTime.parse(start), LocalDateTime.parse(end)).getSeconds();
			if (timestep <= 0 || seconds <= 0) throw new IllegalArgumentException("'" + element.getName() + "': end_time has to be after start_time and timestep positive");
			steps = Math.toIntExact(seconds / timestep);
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException("'" + element.getName() + "': start_time and end_time need the format YYYY-MM-DDTHH:MM", e);
		}
		return this;
	}
}

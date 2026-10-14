package org.example.SimulatorModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.Enums.QualifiedPath;
import org.example.Model.Entity.EntityUsage;

import java.util.List;

public class TestBed extends Abstract<EntityUsage> {

	@JsonProperty("battery")
	private TestbedBattery battery;

	@JsonProperty("pv")
	private TestbedPv pv;

	@JsonProperty("wallbox")
	private List<TestbedWallbox> wallbox;

	@JsonProperty("grid")
	private TestbedGrid grid;

	private final int steps;

	public TestBed(ExperimentMapper experimentMapper, int steps) {
		super(experimentMapper);
		this.steps = steps;
	}

	public void removePv() {
		pv = null;
	}

	public boolean hasPv() {
		return pv != null;
	}

	@Override
	protected QualifiedPath getQualifiedPath() {
		return QualifiedPath.TEST_BED;
	}

	@Override
	protected Abstract<EntityUsage> map(EntityUsage element) {
		List<EntityUsage> entities = element.getEntities();
		battery = new TestbedBattery(experimentMapper, steps).parse(entity(entities, QualifiedPath.TEST_BED__BATTERY)).map(TestbedBattery.class::cast).orElse(null);
		pv = new TestbedPv(experimentMapper).parse(entity(entities, QualifiedPath.TEST_BED__PV)).map(TestbedPv.class::cast).orElse(null);
		grid = new TestbedGrid(experimentMapper).parse(entity(entities, QualifiedPath.TEST_BED__GRID)).map(TestbedGrid.class::cast).orElse(null);

		EntityUsage wallboxes = entity(entities, QualifiedPath.TEST_BED__WALLBOXES);
		if (is(wallboxes, QualifiedPath.WALL_BOXES_CONFIG)) {
			wallbox = entries(wallboxes, QualifiedPath.WALL_BOXES_CONFIG__WALLBOX).stream()
					.map(entry -> new TestbedWallbox(experimentMapper).parse(entry).map(TestbedWallbox.class::cast).orElseThrow())
					.toList();
		}
		return this;
	}
}

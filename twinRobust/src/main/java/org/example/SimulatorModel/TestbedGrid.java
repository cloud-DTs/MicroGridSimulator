package org.example.SimulatorModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.Enums.QualifiedPath;
import org.example.Model.Entity.EntityUsage;


public class TestbedGrid extends Abstract<EntityUsage> {

	@JsonProperty("grid_status")
	private Long gridStatus;

	@JsonProperty("max_import")
	private Long maxImport;

	@JsonProperty("max_export")
	private Long maxExport;

	public TestbedGrid(ExperimentMapper experimentMapper) {
		super(experimentMapper);
	}

	@Override
	protected QualifiedPath getQualifiedPath() {
		return QualifiedPath.GRID_TEST_BED_CONFIG;
	}

	@Override
	protected Abstract<EntityUsage> map(EntityUsage element) {
		gridStatus = values.integer(attribute(element, QualifiedPath.GRID_TEST_BED_CONFIG__GRID_STATUS));
		maxImport = values.integer(attribute(element, QualifiedPath.GRID_TEST_BED_CONFIG__MAX_IMPORT));
		maxExport = values.integer(attribute(element, QualifiedPath.GRID_TEST_BED_CONFIG__MAX_EXPORT));
		return this;
	}
}

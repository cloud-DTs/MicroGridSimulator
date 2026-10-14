package org.example.SimulatorModel;

import Main.ResultConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Enums.QualifiedPath;
import Model.AbstractType;
import org.example.Model.Twin.TwinDefinition;
import Model.Definition;
import org.omg.sysml.util.TypeUtil;

import java.util.*;
import java.util.stream.Collectors;

public class ExperimentMapper {

	// top level twins (direct children of ST_Twin) and their direct children
	private final Map<TwinDefinition, List<TwinDefinition>> twinToSubtwins = new LinkedHashMap<>();

	private final Map<QualifiedPath,AbstractType<?,?>> qualifiedPathToElement = new EnumMap<>(QualifiedPath.class);

	private final ValueReader valueReader;

	public ExperimentMapper(ResultConverter resultConverter, ObjectMapper json) {
		this.valueReader = new ValueReader(json);

		Map<String, QualifiedPath> byName = Arrays.stream(QualifiedPath.values())
				.collect(Collectors.toMap(QualifiedPath::getQualifiedName, path -> path));

		resultConverter.getAll().stream()
				.filter(element -> !element.isInherited())
				.filter(element -> byName.containsKey(element.getSysmlElement().getQualifiedName()))
				.forEach(element -> qualifiedPathToElement.put(byName.get(element.getSysmlElement().getQualifiedName()), element));
		Set<QualifiedPath> missing = EnumSet.allOf(QualifiedPath.class);
		missing.removeAll(qualifiedPathToElement.keySet());
		if (!missing.isEmpty()) {
			throw new IllegalStateException("Simulator model incomplete, missing: " + missing.stream().map(QualifiedPath::getQualifiedName).toList());
		}

		List<TwinDefinition> twins = resultConverter.getByType(TwinDefinition.class).stream()
				.filter(twin -> !twin.isLibrary() && twin.getParent().isEmpty())
				.toList();

		for (TwinDefinition twin : twins) {
			if (twin.getSuperDefinitions().stream().anyMatch(this::isStTwin)) {
				twinToSubtwins.put(twin, new ArrayList<>());
			}
		}
		for (TwinDefinition twin : twins) {
			for (Definition<?, ?> superDef : twin.getSuperDefinitions()) {
				if (TwinDefinition.class.isInstance(superDef) && twinToSubtwins.containsKey(TwinDefinition.class.cast(superDef))) {
					twinToSubtwins.get(TwinDefinition.class.cast(superDef)).add(twin);
				}
			}
		}
	}

	private boolean isStTwin(Definition<?, ?> definition) {
		return QualifiedPath.ST_TWIN.getQualifiedName().equals(definition.getSysmlElement().getQualifiedName());
	}

	public boolean specialices(AbstractType<?, ?> element, QualifiedPath qualifiedPath) {
		return TypeUtil.specializes(element.getSysmlElement(),qualifiedPathToElement.get(qualifiedPath).getSysmlElement());
	}

	public Map<TwinDefinition, List<TwinDefinition>> getTwinToSubtwins() {
		return twinToSubtwins;
	}

	public ValueReader getValueReader() {
		return valueReader;
	}
}

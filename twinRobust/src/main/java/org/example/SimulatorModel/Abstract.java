package org.example.SimulatorModel;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.example.Enums.QualifiedPath;
import org.example.Model.Attribute.ConstAttributeUsage;
import org.example.Model.Entity.EntityUsage;

import java.util.List;
import java.util.Optional;
import Model.AbstractType;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class Abstract<T extends AbstractType<?,?>> {

	protected final ExperimentMapper experimentMapper;
	protected final ValueReader values;

	public Abstract(ExperimentMapper experimentMapper) {
		this.experimentMapper = experimentMapper;
		this.values = experimentMapper.getValueReader();
	}

	public Optional<? extends Abstract<T>> parse(T element){
		if(!isConstructable(element)){
			return Optional.empty();
		}
		return Optional.of(map(element));
	}

	public boolean isConstructable(AbstractType<?,?> element){
		if(isLibrary(element)){
			return false;
		}
		return experimentMapper.specialices(element, getQualifiedPath());
	}

	protected abstract QualifiedPath getQualifiedPath();


	protected abstract Abstract<T> map(T element);

	protected boolean isLibrary(AbstractType<?,?> element){
		return element.isLibrary() || element.getSysmlElement().getQualifiedName().equals(getQualifiedPath().getQualifiedName());
	}

	protected EntityUsage entity(List<EntityUsage> entities, QualifiedPath slot) {
		return entities.stream().filter(entity -> experimentMapper.specialices(entity, slot)).findFirst()
				.orElseThrow(() -> new IllegalStateException("Slot " + slot.getQualifiedName() + " not found"));
	}


	protected List<EntityUsage> entries(EntityUsage owner, QualifiedPath slot) {
		return owner.getEntities().stream()
				.filter(entity -> experimentMapper.specialices(entity, slot))
				.filter(entity -> !entity.getSysmlElement().getQualifiedName().equals(slot.getQualifiedName()))
				.toList();
	}


	protected ConstAttributeUsage attribute(EntityUsage owner, QualifiedPath feature) {
		return owner.getConstAttributes().stream().filter(attribute -> experimentMapper.specialices(attribute, feature)).findFirst()
				.orElseThrow(() -> new IllegalStateException("Attribute " + feature.getQualifiedName() + " not found in '" + owner.getName() + "'"));
	}

	protected boolean is(AbstractType<?,?> element, QualifiedPath variant) {
		return experimentMapper.specialices(element, variant);
	}
}

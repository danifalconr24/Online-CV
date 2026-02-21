package es.danifalconr.infrastructure.persistence.mapper;

import es.danifalconr.domain.model.WorkExperience;
import es.danifalconr.infrastructure.persistence.entity.WorkExperienceEntity;

public final class WorkExperiencePersistenceMapper {

    private WorkExperiencePersistenceMapper() {
    }

    public static WorkExperience toDomain(WorkExperienceEntity entity) {
        WorkExperience model = new WorkExperience();
        model.setId(entity.id);
        model.setStartDate(entity.startDate);
        model.setEndDate(entity.endDate);
        model.setCurrent(entity.current);
        model.setCompany(entity.company);
        model.setDescription(entity.description);
        return model;
    }

    public static WorkExperienceEntity toEntity(WorkExperience model) {
        WorkExperienceEntity entity = new WorkExperienceEntity();
        entity.id = model.getId();
        entity.startDate = model.getStartDate();
        entity.endDate = model.getEndDate();
        entity.current = model.getCurrent();
        entity.company = model.getCompany();
        entity.description = model.getDescription();
        return entity;
    }
}

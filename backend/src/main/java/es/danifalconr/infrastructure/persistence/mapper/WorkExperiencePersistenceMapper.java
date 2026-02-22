package es.danifalconr.infrastructure.persistence.mapper;

import es.danifalconr.domain.model.WorkExperience;
import es.danifalconr.infrastructure.persistence.entity.WorkExperienceEntity;

public final class WorkExperiencePersistenceMapper {

    private WorkExperiencePersistenceMapper() {
    }

    public static WorkExperience toDomain(WorkExperienceEntity entity) {
        return new WorkExperience(
                entity.id,
                entity.startDate,
                entity.endDate,
                entity.current,
                entity.company,
                entity.description,
                entity.companyLogo
        );
    }

    public static WorkExperienceEntity toEntity(WorkExperience model) {
        WorkExperienceEntity entity = new WorkExperienceEntity();
        entity.id = model.id();
        entity.startDate = model.startDate();
        entity.endDate = model.endDate();
        entity.current = model.current();
        entity.company = model.company();
        entity.description = model.description();
        entity.companyLogo = model.companyLogo();
        return entity;
    }
}

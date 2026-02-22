package es.danifalconr.infrastructure.persistence.mapper;

import es.danifalconr.domain.model.AcademicStudy;
import es.danifalconr.infrastructure.persistence.entity.AcademicStudyEntity;

public final class AcademicStudyPersistenceMapper {

    private AcademicStudyPersistenceMapper() {
    }

    public static AcademicStudy toDomain(AcademicStudyEntity entity) {
        return new AcademicStudy(entity.id, entity.schoolName, entity.titleName);
    }

    public static AcademicStudyEntity toEntity(AcademicStudy model) {
        AcademicStudyEntity entity = new AcademicStudyEntity();
        entity.id = model.id();
        entity.schoolName = model.schoolName();
        entity.titleName = model.titleName();
        return entity;
    }
}

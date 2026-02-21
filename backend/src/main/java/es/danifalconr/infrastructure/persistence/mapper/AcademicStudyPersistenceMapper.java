package es.danifalconr.infrastructure.persistence.mapper;

import es.danifalconr.domain.model.AcademicStudy;
import es.danifalconr.infrastructure.persistence.entity.AcademicStudyEntity;

public final class AcademicStudyPersistenceMapper {

    private AcademicStudyPersistenceMapper() {
    }

    public static AcademicStudy toDomain(AcademicStudyEntity entity) {
        AcademicStudy model = new AcademicStudy();
        model.setId(entity.id);
        model.setSchoolName(entity.schoolName);
        model.setTitleName(entity.titleName);
        return model;
    }

    public static AcademicStudyEntity toEntity(AcademicStudy model) {
        AcademicStudyEntity entity = new AcademicStudyEntity();
        entity.id = model.getId();
        entity.schoolName = model.getSchoolName();
        entity.titleName = model.getTitleName();
        return entity;
    }
}

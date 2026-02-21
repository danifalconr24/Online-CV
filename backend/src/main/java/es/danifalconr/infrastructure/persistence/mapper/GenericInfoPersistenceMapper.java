package es.danifalconr.infrastructure.persistence.mapper;

import es.danifalconr.domain.model.GenericInfo;
import es.danifalconr.infrastructure.persistence.entity.GenericInfoEntity;

public final class GenericInfoPersistenceMapper {

    private GenericInfoPersistenceMapper() {
    }

    public static GenericInfo toDomain(GenericInfoEntity entity) {
        GenericInfo model = new GenericInfo();
        model.setId(entity.id);
        model.setAboutMe(entity.aboutMe);
        return model;
    }

    public static GenericInfoEntity toEntity(GenericInfo model) {
        GenericInfoEntity entity = new GenericInfoEntity();
        entity.id = model.getId();
        entity.aboutMe = model.getAboutMe();
        return entity;
    }
}

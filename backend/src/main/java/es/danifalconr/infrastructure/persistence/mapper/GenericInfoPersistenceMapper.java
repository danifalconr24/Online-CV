package es.danifalconr.infrastructure.persistence.mapper;

import es.danifalconr.domain.model.GenericInfo;
import es.danifalconr.infrastructure.persistence.entity.GenericInfoEntity;

public final class GenericInfoPersistenceMapper {

    private GenericInfoPersistenceMapper() {
    }

    public static GenericInfo toDomain(GenericInfoEntity entity) {
        return new GenericInfo(entity.id, entity.aboutMe);
    }

    public static GenericInfoEntity toEntity(GenericInfo model) {
        GenericInfoEntity entity = new GenericInfoEntity();
        entity.id = model.id();
        entity.aboutMe = model.aboutMe();
        return entity;
    }
}

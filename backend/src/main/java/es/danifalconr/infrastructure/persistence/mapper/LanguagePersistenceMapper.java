package es.danifalconr.infrastructure.persistence.mapper;

import es.danifalconr.domain.model.Language;
import es.danifalconr.infrastructure.persistence.entity.LanguageEntity;

public final class LanguagePersistenceMapper {

    private LanguagePersistenceMapper() {
    }

    public static Language toDomain(LanguageEntity entity) {
        return new Language(entity.id, entity.name, entity.level);
    }

    public static LanguageEntity toEntity(Language model) {
        LanguageEntity entity = new LanguageEntity();
        entity.id = model.id();
        entity.name = model.name();
        entity.level = model.level();
        return entity;
    }
}

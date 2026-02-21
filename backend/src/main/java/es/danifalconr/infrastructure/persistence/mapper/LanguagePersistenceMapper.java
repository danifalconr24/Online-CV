package es.danifalconr.infrastructure.persistence.mapper;

import es.danifalconr.domain.model.Language;
import es.danifalconr.infrastructure.persistence.entity.LanguageEntity;

public final class LanguagePersistenceMapper {

    private LanguagePersistenceMapper() {
    }

    public static Language toDomain(LanguageEntity entity) {
        Language model = new Language();
        model.setId(entity.id);
        model.setName(entity.name);
        if (entity.level != null) {
            model.setLevel(Language.Level.valueOf(entity.level.name()));
        }
        return model;
    }

    public static LanguageEntity toEntity(Language model) {
        LanguageEntity entity = new LanguageEntity();
        entity.id = model.getId();
        entity.name = model.getName();
        if (model.getLevel() != null) {
            entity.level = LanguageEntity.Level.valueOf(model.getLevel().name());
        }
        return entity;
    }
}

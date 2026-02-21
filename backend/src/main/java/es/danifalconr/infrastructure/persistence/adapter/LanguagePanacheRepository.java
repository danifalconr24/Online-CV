package es.danifalconr.infrastructure.persistence.adapter;

import es.danifalconr.domain.model.Language;
import es.danifalconr.domain.port.out.LanguageRepository;
import es.danifalconr.infrastructure.persistence.entity.LanguageEntity;
import es.danifalconr.infrastructure.persistence.mapper.LanguagePersistenceMapper;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class LanguagePanacheRepository implements LanguageRepository, PanacheRepository<LanguageEntity> {

    @Override
    public Language save(Language language) {
        LanguageEntity entity = LanguagePersistenceMapper.toEntity(language);
        persist(entity);
        return LanguagePersistenceMapper.toDomain(entity);
    }

    @Override
    public List<Language> findAllLanguages() {
        return listAll()
                .stream()
                .map(LanguagePersistenceMapper::toDomain)
                .toList();
    }
}

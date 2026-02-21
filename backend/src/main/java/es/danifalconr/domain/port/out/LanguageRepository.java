package es.danifalconr.domain.port.out;

import es.danifalconr.domain.model.Language;

import java.util.List;

public interface LanguageRepository {

    Language save(Language language);

    List<Language> findAllLanguages();
}

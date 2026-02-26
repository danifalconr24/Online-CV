package es.danifalconr.domain.port.out;

import es.danifalconr.domain.model.GenericInfo;

import java.util.Optional;

public interface GenericInfoRepository {

    GenericInfo save(GenericInfo genericInfo);

    GenericInfo update(Long id, GenericInfo genericInfo);

    Optional<GenericInfo> findLatest();

    Optional<GenericInfo> getById(Long id);
}

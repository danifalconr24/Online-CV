package es.danifalconr.domain.port.out;

import es.danifalconr.domain.model.GenericInfo;

public interface GenericInfoRepository {

    GenericInfo save(GenericInfo genericInfo);

    GenericInfo update(Long id, GenericInfo genericInfo);

    GenericInfo findLatest();
}

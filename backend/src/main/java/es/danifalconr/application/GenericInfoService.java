package es.danifalconr.application;

import es.danifalconr.domain.model.GenericInfo;
import es.danifalconr.domain.port.out.GenericInfoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class GenericInfoService {

    private final GenericInfoRepository genericInfoRepository;

    public GenericInfoService(GenericInfoRepository genericInfoRepository) {
        this.genericInfoRepository = genericInfoRepository;
    }

    @Transactional
    public GenericInfo getLatest() {
        return genericInfoRepository.findLatest();
    }

    @Transactional
    public GenericInfo save(GenericInfo genericInfo) {
        return genericInfoRepository.save(genericInfo);
    }
}

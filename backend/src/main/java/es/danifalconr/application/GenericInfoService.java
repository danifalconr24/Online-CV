package es.danifalconr.application;

import es.danifalconr.domain.exception.EntityNotFoundException;
import es.danifalconr.domain.model.GenericInfo;
import es.danifalconr.domain.port.out.GenericInfoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.Base64;

@ApplicationScoped
public class GenericInfoService {

    private final GenericInfoRepository genericInfoRepository;

    public GenericInfoService(GenericInfoRepository genericInfoRepository) {
        this.genericInfoRepository = genericInfoRepository;
    }

    @Transactional
    public GenericInfo getLatest() {
        return genericInfoRepository.findLatest()
                .orElseThrow(() -> new EntityNotFoundException("GenericInfo not found"));
    }

    @Transactional
    public GenericInfo save(GenericInfo genericInfo) {
        return genericInfoRepository.save(genericInfo);
    }

    @Transactional
    public GenericInfo update(Long id, GenericInfo genericInfo) {
        return genericInfoRepository.update(id, genericInfo);
    }

    @Transactional
    public GenericInfo updateImage(Long id, byte[] imageBytes) {
        GenericInfo current = genericInfoRepository.getById(id)
                .orElseThrow(() -> new EntityNotFoundException("GenericInfo not found with id: " + id));
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        GenericInfo updated = new GenericInfo(current.id(), current.aboutMe(), base64Image);
        return genericInfoRepository.update(id, updated);
    }
}

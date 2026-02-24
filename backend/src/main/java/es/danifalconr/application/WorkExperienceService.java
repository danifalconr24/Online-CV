package es.danifalconr.application;

import es.danifalconr.domain.exception.EntityNotFoundException;
import es.danifalconr.domain.model.WorkExperience;
import es.danifalconr.domain.port.out.WorkExperienceRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.Base64;
import java.util.List;

@ApplicationScoped
public class WorkExperienceService {

    private final WorkExperienceRepository workExperienceRepository;

    public WorkExperienceService(WorkExperienceRepository workExperienceRepository) {
        this.workExperienceRepository = workExperienceRepository;
    }

    @Transactional
    public WorkExperience create(WorkExperience workExperience) {
        return workExperienceRepository.save(workExperience);
    }

    @Transactional
    public WorkExperience update(Long id, WorkExperience workExperience) {
        return workExperienceRepository.update(id, workExperience);
    }

    @Transactional
    public void delete(Long id) {
        workExperienceRepository.remove(id);
    }

    @Transactional
    public List<WorkExperience> getAll() {
        return workExperienceRepository.findAllOrderByCreatedAtDesc();
    }

    @Transactional
    public WorkExperience updateLogo(Long id, byte[] imageBytes) {
        String base64Logo = Base64.getEncoder().encodeToString(imageBytes);
        WorkExperience current = workExperienceRepository.getById(id)
                .orElseThrow(() -> new EntityNotFoundException("WorkExperience not found with id: " + id));
        WorkExperience updated = new WorkExperience(
                current.id(), current.startDate(), current.endDate(),
                current.current(), current.company(), current.description(), base64Logo);
        return workExperienceRepository.update(id, updated);
    }
}

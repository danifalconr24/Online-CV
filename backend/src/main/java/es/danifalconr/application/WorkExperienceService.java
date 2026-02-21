package es.danifalconr.application;

import es.danifalconr.domain.model.WorkExperience;
import es.danifalconr.domain.port.out.WorkExperienceRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

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
    public List<WorkExperience> getAll() {
        return workExperienceRepository.findAllOrderByCreatedAtDesc();
    }
}

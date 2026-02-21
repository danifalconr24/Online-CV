package es.danifalconr.domain.port.out;

import es.danifalconr.domain.model.WorkExperience;

import java.util.List;

public interface WorkExperienceRepository {

    WorkExperience save(WorkExperience workExperience);

    WorkExperience update(Long id, WorkExperience workExperience);

    void remove(Long id);

    List<WorkExperience> findAllOrderByCreatedAtDesc();
}

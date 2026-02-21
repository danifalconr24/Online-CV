package es.danifalconr.domain.port.out;

import es.danifalconr.domain.model.WorkExperience;

import java.util.List;

public interface WorkExperienceRepository {

    WorkExperience save(WorkExperience workExperience);

    List<WorkExperience> findAllOrderByCreatedAtDesc();
}

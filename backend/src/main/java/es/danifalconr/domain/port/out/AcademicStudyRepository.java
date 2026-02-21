package es.danifalconr.domain.port.out;

import es.danifalconr.domain.model.AcademicStudy;

import java.util.List;

public interface AcademicStudyRepository {

    AcademicStudy save(AcademicStudy academicStudy);

    List<AcademicStudy> findAllOrderByCreatedAtDesc();
}

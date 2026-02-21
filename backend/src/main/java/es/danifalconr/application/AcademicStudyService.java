package es.danifalconr.application;

import es.danifalconr.domain.model.AcademicStudy;
import es.danifalconr.domain.port.out.AcademicStudyRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class AcademicStudyService {

    private final AcademicStudyRepository academicStudyRepository;

    public AcademicStudyService(AcademicStudyRepository academicStudyRepository) {
        this.academicStudyRepository = academicStudyRepository;
    }

    @Transactional
    public AcademicStudy create(AcademicStudy academicStudy) {
        return academicStudyRepository.save(academicStudy);
    }

    @Transactional
    public List<AcademicStudy> getAll() {
        return academicStudyRepository.findAllOrderByCreatedAtDesc();
    }
}

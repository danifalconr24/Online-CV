package es.danifalconr.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class CurriculumVitae extends PanacheEntity {

    @OneToMany
    public List<WorkExperience> workExperiences;

    @OneToMany
    public List<AcademicStudies> academicStudies;


}

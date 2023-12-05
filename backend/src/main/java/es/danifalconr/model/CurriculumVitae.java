package es.danifalconr.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.List;

@Entity
public class CurriculumVitae extends PanacheEntity {

    @OneToMany
    @Cascade(value = {CascadeType.PERSIST, CascadeType.REMOVE})
    public List<WorkExperience> workExperiences;

    @OneToMany
    @Cascade(value = {CascadeType.PERSIST, CascadeType.REMOVE})
    public List<AcademicStudies> academicStudies;

    @UpdateTimestamp
    Instant updatedAt;

    @CreationTimestamp
    Instant createdAt;

}

package es.danifalconr.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
public class AcademicStudies extends PanacheEntity {

    public String schoolName;

    public String titleName;

    @UpdateTimestamp
    Instant updatedAt;

    @CreationTimestamp
    Instant createdAt;

}

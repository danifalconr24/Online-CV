package es.danifalconr.infrastructure.persistence.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "AcademicStudy")
public class AcademicStudyEntity extends PanacheEntity {

    @Column(nullable = false)
    public String schoolName;

    @Column(nullable = false)
    public String titleName;

    @UpdateTimestamp
    public Instant updatedAt;

    @CreationTimestamp
    public Instant createdAt;
}

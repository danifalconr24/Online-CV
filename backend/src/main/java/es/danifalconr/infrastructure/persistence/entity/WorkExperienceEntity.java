package es.danifalconr.infrastructure.persistence.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "WorkExperience")
public class WorkExperienceEntity extends PanacheEntity {

    @Column(nullable = false)
    public String startDate;

    public String endDate;

    public Boolean current;

    @Column(nullable = false)
    public String company;

    @Column(columnDefinition = "TEXT", nullable = false)
    public String description;

    @UpdateTimestamp
    public Instant updatedAt;

    @CreationTimestamp
    public Instant createdAt;
}

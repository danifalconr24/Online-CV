package es.danifalconr.model;


import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
public class WorkExperience extends PanacheEntity {

    @Column(nullable = false)
    public String startDate;

    public String endDate;

    public Boolean current;

    @Column(nullable = false)
    public String company;

    @Column(columnDefinition="TEXT", nullable = false)
    public String description;

    @UpdateTimestamp
    Instant updatedAt;

    @CreationTimestamp
    Instant createdAt;

}

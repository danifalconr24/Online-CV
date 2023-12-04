package es.danifalconr.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class AcademicStudies extends PanacheEntity {

    public String schoolName;

    public String titleName;

}

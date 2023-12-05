package es.danifalconr.model;


import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class WorkExperience extends PanacheEntity {

    public String startDate;

    public String endDate;

    public Boolean current;

    public String company;

    public String description;

}

package es.danifalconr.model;


import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class LaboralExperience extends PanacheEntity {

    public String From;

    public String to;

    public String company;

    public String description;

}

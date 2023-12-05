package es.danifalconr.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Languages extends PanacheEntity {

    public String name;

    public Level level;

    public enum Level {

        LOW,

        MEDIUM,

        HIGH,

        NATIVE

    }

}

package es.danifalconr.infrastructure.persistence.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "Languages")
public class LanguageEntity extends PanacheEntity {

    public String name;

    @Enumerated(EnumType.STRING)
    public Level level;

    public enum Level {
        LOW,
        MEDIUM,
        HIGH,
        NATIVE
    }
}

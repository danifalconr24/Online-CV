package es.danifalconr.infrastructure.persistence.entity;

import es.danifalconr.domain.model.Language;
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
    public Language.Level level;
}

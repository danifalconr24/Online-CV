package es.danifalconr.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.panache.common.Sort;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Entity
public class GenericInfo extends PanacheEntity {

    //TODO: add contact info (mail, phone, LinkedIn, birth date)

    @Column(columnDefinition="TEXT", nullable = false)
    public String aboutMe;

    @UpdateTimestamp
    Instant updatedAt;

    @CreationTimestamp
    Instant createdAt;

    public static GenericInfo getLatest() {
        List<GenericInfo> allGenericInfos = listAll(Sort.by("updatedAt"));
        return allGenericInfos.getFirst();
    }

}

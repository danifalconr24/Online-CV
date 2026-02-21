package es.danifalconr.infrastructure.persistence.adapter;

import es.danifalconr.domain.model.WorkExperience;
import es.danifalconr.domain.port.out.WorkExperienceRepository;
import es.danifalconr.infrastructure.persistence.entity.WorkExperienceEntity;
import es.danifalconr.infrastructure.persistence.mapper.WorkExperiencePersistenceMapper;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class WorkExperiencePanacheRepository implements WorkExperienceRepository, PanacheRepository<WorkExperienceEntity> {

    @Override
    public WorkExperience save(WorkExperience workExperience) {
        WorkExperienceEntity entity = WorkExperiencePersistenceMapper.toEntity(workExperience);
        persist(entity);
        return WorkExperiencePersistenceMapper.toDomain(entity);
    }

    @Override
    public List<WorkExperience> findAllOrderByCreatedAtDesc() {
        return listAll(Sort.by("createdAt").descending())
                .stream()
                .map(WorkExperiencePersistenceMapper::toDomain)
                .toList();
    }
}

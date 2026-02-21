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
    public WorkExperience update(Long id, WorkExperience workExperience) {
        WorkExperienceEntity entity = findById(id);
        if (entity == null) {
            throw new jakarta.ws.rs.NotFoundException("WorkExperience not found with id: " + id);
        }
        entity.startDate = workExperience.getStartDate();
        entity.endDate = workExperience.getEndDate();
        entity.current = workExperience.getCurrent();
        entity.company = workExperience.getCompany();
        entity.description = workExperience.getDescription();
        return WorkExperiencePersistenceMapper.toDomain(entity);
    }

    @Override
    public void remove(Long id) {
        WorkExperienceEntity entity = findById(id);
        if (entity == null) {
            throw new jakarta.ws.rs.NotFoundException("WorkExperience not found with id: " + id);
        }
        delete(entity);
    }

    @Override
    public List<WorkExperience> findAllOrderByCreatedAtDesc() {
        return listAll(Sort.by("createdAt").descending())
                .stream()
                .map(WorkExperiencePersistenceMapper::toDomain)
                .toList();
    }
}

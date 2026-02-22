package es.danifalconr.infrastructure.persistence.adapter;

import es.danifalconr.domain.exception.EntityNotFoundException;
import es.danifalconr.domain.model.AcademicStudy;
import es.danifalconr.domain.port.out.AcademicStudyRepository;
import es.danifalconr.infrastructure.persistence.entity.AcademicStudyEntity;
import es.danifalconr.infrastructure.persistence.mapper.AcademicStudyPersistenceMapper;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class AcademicStudyPanacheRepository implements AcademicStudyRepository, PanacheRepository<AcademicStudyEntity> {

    @Override
    public AcademicStudy save(AcademicStudy academicStudy) {
        AcademicStudyEntity entity = AcademicStudyPersistenceMapper.toEntity(academicStudy);
        persist(entity);
        return AcademicStudyPersistenceMapper.toDomain(entity);
    }

    @Override
    public AcademicStudy update(Long id, AcademicStudy academicStudy) {
        AcademicStudyEntity entity = findById(id);
        if (entity == null) {
            throw new EntityNotFoundException("AcademicStudy not found with id: " + id);
        }
        entity.schoolName = academicStudy.schoolName();
        entity.titleName = academicStudy.titleName();
        return AcademicStudyPersistenceMapper.toDomain(entity);
    }

    @Override
    public void remove(Long id) {
        AcademicStudyEntity entity = findById(id);
        if (entity == null) {
            throw new EntityNotFoundException("AcademicStudy not found with id: " + id);
        }
        delete(entity);
    }

    @Override
    public List<AcademicStudy> findAllOrderByCreatedAtDesc() {
        return listAll(Sort.by("createdAt").descending())
                .stream()
                .map(AcademicStudyPersistenceMapper::toDomain)
                .toList();
    }
}

package es.danifalconr.infrastructure.persistence.adapter;

import es.danifalconr.domain.exception.EntityNotFoundException;
import es.danifalconr.domain.model.GenericInfo;
import es.danifalconr.domain.port.out.GenericInfoRepository;
import es.danifalconr.infrastructure.persistence.entity.GenericInfoEntity;
import es.danifalconr.infrastructure.persistence.mapper.GenericInfoPersistenceMapper;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class GenericInfoPanacheRepository implements GenericInfoRepository, PanacheRepository<GenericInfoEntity> {

    @Override
    public GenericInfo save(GenericInfo genericInfo) {
        GenericInfoEntity entity = GenericInfoPersistenceMapper.toEntity(genericInfo);
        persist(entity);
        return GenericInfoPersistenceMapper.toDomain(entity);
    }

    @Override
    public GenericInfo update(Long id, GenericInfo genericInfo) {
        GenericInfoEntity entity = findById(id);
        if (entity == null) {
            throw new EntityNotFoundException("GenericInfo not found with id: " + id);
        }
        entity.aboutMe = genericInfo.aboutMe();
        return GenericInfoPersistenceMapper.toDomain(entity);
    }

    @Override
    public GenericInfo findLatest() {
        List<GenericInfoEntity> all = listAll(Sort.by("updatedAt"));
        return all.isEmpty() ? null : GenericInfoPersistenceMapper.toDomain(all.getFirst());
    }
}

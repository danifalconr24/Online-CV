package es.danifalconr.infrastructure.rest.dto;

import es.danifalconr.domain.model.GenericInfo;

public record GenericInfoResponse(Long id, String aboutMe) {

    public static GenericInfoResponse fromDomain(GenericInfo model) {
        return new GenericInfoResponse(model.getId(), model.getAboutMe());
    }
}

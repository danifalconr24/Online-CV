package es.danifalconr.infrastructure.rest;

import es.danifalconr.application.GenericInfoService;
import es.danifalconr.domain.model.GenericInfo;
import es.danifalconr.infrastructure.rest.dto.GenericInfoRequest;
import es.danifalconr.infrastructure.rest.dto.GenericInfoResponse;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.jboss.resteasy.reactive.RestForm;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/v1/curriculum-vitae/generic-info")
public class GenericInfoResource {

    private final GenericInfoService genericInfoService;

    public GenericInfoResource(GenericInfoService genericInfoService) {
        this.genericInfoService = genericInfoService;
    }

    @GET
    @PermitAll
    public GenericInfoResponse getGenericInfo() {
        return GenericInfoResponse.fromDomain(genericInfoService.getLatest());
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("admin")
    public GenericInfoResponse updateGenericInfo(@PathParam("id") Long id, @NotNull @Valid GenericInfoRequest request) {
        return GenericInfoResponse.fromDomain(genericInfoService.update(id, request.toDomain()));
    }

    @PUT
    @Path("/{id}/image")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @RolesAllowed("admin")
    public GenericInfoResponse uploadImage(@PathParam("id") Long id, @RestForm("file") FileUpload file) throws IOException {
        byte[] bytes = Files.readAllBytes(file.uploadedFile());
        String base64Image = Base64.getEncoder().encodeToString(bytes);
        GenericInfo current = genericInfoService.getLatest();
        GenericInfo updated = new GenericInfo(current.id(), current.aboutMe(), base64Image);
        return GenericInfoResponse.fromDomain(genericInfoService.update(id, updated));
    }
}

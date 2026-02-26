package es.danifalconr.infrastructure.rest;

import es.danifalconr.application.GenericInfoService;
import es.danifalconr.infrastructure.rest.dto.GenericInfoRequest;
import es.danifalconr.infrastructure.rest.dto.GenericInfoResponse;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.jboss.resteasy.reactive.RestForm;

import java.io.IOException;
import java.nio.file.Files;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/v1/curriculum-vitae/generic-info")
@Tag(name = "Generic Info")
public class GenericInfoResource {

    private final GenericInfoService genericInfoService;

    public GenericInfoResource(GenericInfoService genericInfoService) {
        this.genericInfoService = genericInfoService;
    }

    @GET
    @PermitAll
    @Operation(summary = "Get generic info")
    @APIResponse(responseCode = "200", description = "Generic info retrieved")
    public GenericInfoResponse getGenericInfo() {
        return GenericInfoResponse.fromDomain(genericInfoService.getLatest());
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("admin")
    @Operation(summary = "Update generic info")
    @APIResponse(responseCode = "200", description = "Generic info updated")
    @APIResponse(responseCode = "401", description = "Unauthorized")
    @APIResponse(responseCode = "404", description = "Not found")
    public GenericInfoResponse updateGenericInfo(@PathParam("id") Long id, @NotNull @Valid GenericInfoRequest request) {
        return GenericInfoResponse.fromDomain(genericInfoService.update(id, request.toDomain()));
    }

    @PUT
    @Path("/{id}/image")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @RolesAllowed("admin")
    @Operation(summary = "Upload profile image")
    @APIResponse(responseCode = "200", description = "Image uploaded")
    @APIResponse(responseCode = "401", description = "Unauthorized")
    @APIResponse(responseCode = "404", description = "Not found")
    public GenericInfoResponse uploadImage(@PathParam("id") Long id, @RestForm("file") FileUpload file) throws IOException {
        byte[] bytes = Files.readAllBytes(file.uploadedFile());
        return GenericInfoResponse.fromDomain(genericInfoService.updateImage(id, bytes));
    }
}

package es.danifalconr.api;

import es.danifalconr.model.GenericInfo;
import es.danifalconr.model.WorkExperience;
import io.quarkus.panache.common.Sort;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/v1/curriculum-vitae/generic-info")
public class GenericInfoResource {

    @GET
    @Transactional
    public GenericInfo getGenericInfo() {
        return GenericInfo.getLatest();
    }

}

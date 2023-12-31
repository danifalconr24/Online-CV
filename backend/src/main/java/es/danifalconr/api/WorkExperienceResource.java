package es.danifalconr.api;

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
@Path("/v1/curriculum-vitae/work-experiences")
public class WorkExperienceResource {

    @POST
    @Transactional
    public WorkExperience createWorkExperience(@NotNull @Valid WorkExperience workExperience) {
        workExperience.persist();
        return workExperience;
    }

    @GET
    @Transactional
    public List<WorkExperience> getWorkExperiences() {
        return WorkExperience.listAll(Sort.by("createdAt").descending());
    }

}

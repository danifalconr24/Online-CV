package es.danifalconr.api;

import es.danifalconr.model.CurriculumVitae;
import es.danifalconr.model.WorkExperience;
import io.quarkus.panache.common.Sort;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/v1/curriculum-vitae")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CurriculumVitaeResource {

    @POST
    @Transactional
    public CurriculumVitae createCurriculumVitae(CurriculumVitae curriculumVitae) {
        curriculumVitae.persist();
        return curriculumVitae;
    }

    @GET
    @Path("/latest")
    public List<WorkExperience> getCurriculumData() {
        return CurriculumVitae.listAll(Sort.by("createdAt").descending());
    }

}

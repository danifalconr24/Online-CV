package es.danifalconr.api;

import es.danifalconr.model.AcademicStudies;
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
    @Path("/work-experience")
    @Transactional
    public WorkExperience createWorkExperience(WorkExperience workExperience) {
        workExperience.persist();
        return workExperience;
    }

    @POST
    @Path("/academic-studies")
    @Transactional
    public AcademicStudies createAcademicStudy(AcademicStudies academicStudies) {
        academicStudies.persist();
        return academicStudies;
    }

    @GET
    @Transactional
    @Path("/work-experiences")
    public List<WorkExperience> getWorkExperiences() {
        return WorkExperience.listAll(Sort.by("createdAt").descending());
    }

    @GET
    @Transactional
    @Path("/academic-studies")
    public List<AcademicStudies> getAcademicStudies() {
        return AcademicStudies.listAll(Sort.by("createdAt").descending());
    }

}

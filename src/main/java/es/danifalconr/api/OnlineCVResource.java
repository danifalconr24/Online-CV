package es.danifalconr.api;

import es.danifalconr.model.AcademicStudies;
import es.danifalconr.model.WorkExperience;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import java.util.List;

@Path("/v1")
public class OnlineCVResource {

    @GET
    @Path("/work-experiences")
    public List<WorkExperience> getWorkExperiences() {
        return WorkExperience.listAll();
    }

    @GET
    @Path("/academic-studies")
    public List<AcademicStudies> getAcademicStudies() {
        return AcademicStudies.listAll();
    }

}

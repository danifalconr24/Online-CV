package es.danifalconr.api;

import es.danifalconr.model.AcademicStudy;
import io.quarkus.panache.common.Sort;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/v1/curriculum-vitae/academic-studies")
public class AcademicStudyResource {

    @POST
    @Transactional
    public AcademicStudy createAcademicStudy(AcademicStudy academicStudy) {
        academicStudy.persist();
        return academicStudy;
    }

    @GET
    @Transactional
    public List<AcademicStudy> getAcademicStudies() {
        return AcademicStudy.listAll(Sort.by("createdAt").descending());
    }

}

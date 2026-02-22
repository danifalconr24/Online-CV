package es.danifalconr.infrastructure.config;

import es.danifalconr.application.AcademicStudyService;
import es.danifalconr.application.GenericInfoService;
import es.danifalconr.application.WorkExperienceService;
import es.danifalconr.domain.model.AcademicStudy;
import es.danifalconr.domain.model.GenericInfo;
import es.danifalconr.domain.model.WorkExperience;
import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class Bootstrap {

    private final GenericInfoService genericInfoService;
    private final WorkExperienceService workExperienceService;
    private final AcademicStudyService academicStudyService;

    public Bootstrap(GenericInfoService genericInfoService,
                     WorkExperienceService workExperienceService,
                     AcademicStudyService academicStudyService) {
        this.genericInfoService = genericInfoService;
        this.workExperienceService = workExperienceService;
        this.academicStudyService = academicStudyService;
    }

    @Startup
    void init() {
        createAndPersistGenericInfo();
        createAndPersistWorkExperiences();
        createAndPersistAcademicStudies();
    }

    private void createAndPersistGenericInfo() {
        GenericInfo genericInfo = new GenericInfo(null, """
                Java Software Engineer

                Experienced in designing and implementing large, scalable microservices architectures \
                using Quarkus and Spring. Strong focus on clean code, best practices, and thoroughly \
                tested functionalities. Skilled in modern cloud-native tooling, leveraging Docker and \
                Kubernetes for deployment, AWS for cloud infrastructure, and GitHub Actions for robust \
                CI/CD pipelines.""", null);
        genericInfoService.save(genericInfo);
    }

    private void createAndPersistWorkExperiences() {
        List.of(
                new WorkExperience(null, "01/11/2019", "01/05/2020", null, "Everis", """
                        Worked as Full-Stack developer in Spring Boot REST API that will provide and handle \
                        frontend requests for an insurance company project. Also worked in frontend side with \
                        Javascript and Vue.js.""", null),
                new WorkExperience(null, "01/05/2020", "31/07/2023", null, "BitingBit / Volkswagen", """
                        Developing big user base applications, for an automotive company, using Quarkus native \
                        compilation support for providing really low memory and CPU consumption API's that \
                        performs for a big amount of users.
                        Maintaining existing Spring Boot microservices optimising performance and providing \
                        REST API's deployed in Kubernetes with reactive programming.""", null),
                new WorkExperience(null, "01/08/2023", "12/02/2024", null, "Vermont Solutions / Santander Bank", """
                        As a Java Backend Developer, I spearheaded the migration of microservices from Spring \
                        Boot to Quarkus and Java 17, leveraging my expertise to optimise performance and enhance \
                        development efficiency. I played a key role in supporting the team, serving as the go-to \
                        Quarkus expert and ensuring a smooth transition.""", null),
                new WorkExperience(null, "13/02/2024", null, true, "Voiping US / Amadeus", """
                        Worked in the migration of a legacy monolithic application to a microservices \
                        architecture, utilizing Java (17, 21) with Quarkus and Spring Boot, Kafka for \
                        inter-service messaging, and Redis as the non-relational database.""", null)
        ).forEach(workExperienceService::create);
    }

    private void createAndPersistAcademicStudies() {
        List.of(
                new AcademicStudy(null, "CIFP Villa de Agüimes", "Microcomputer Systems and Networks"),
                new AcademicStudy(null, "CIFP Villa de Agüimes", "Web Development")
        ).forEach(academicStudyService::create);
    }
}

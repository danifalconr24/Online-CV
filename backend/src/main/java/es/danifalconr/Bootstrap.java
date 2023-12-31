package es.danifalconr;

import es.danifalconr.model.GenericInfo;
import es.danifalconr.model.WorkExperience;
import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class Bootstrap {

    @Startup
    @Transactional
    void init() {
        // setup minimum app data
        createAndPersistGenericInfo();
        createAndPersistWorkExperiences();
    }

    private void createAndPersistGenericInfo() {
        GenericInfo genericInfo = new GenericInfo();
        genericInfo.aboutMe = "Java Quarkus Developer. Utilised Quarkus for crafting micro-services, employed AWS " +
                "for cloud scalability, integrated Apache Kafka for real-time event streaming, and relied on Postgres " +
                "for secure data storage. Docker and GitHub Actions have been instrumental in maintaining a seamless " +
                "development workflow. Embracing open source collaboration, I've encountered challenges, gained valuable " +
                "insights designing robust APIs, efficiently.";
        genericInfo.persist();
    }

    private void createAndPersistWorkExperiences() {
        // Everis experience
        WorkExperience everisExpererience = new WorkExperience();
        everisExpererience.company = "Everis";
        everisExpererience.description = "Project in the insurance sector, with Java (Spring boot) and Javascript " +
                "with VueJS for the frontend.";
        everisExpererience.startDate = "01-11-2019";
        everisExpererience.endDate = "01-05-2020";

        // BitingBit Campus experience
        WorkExperience bitingBitExperience = new WorkExperience();
        bitingBitExperience.company = "BitingBit Campus SLU";
        bitingBitExperience.description = "Developing big user base applications, for an automotive company, using " +
                "Quarkus native for providing really low memory and CPU consumption API’s that performs for a " +
                "big amount of users.";
        bitingBitExperience.startDate = "01-05-2020";
        bitingBitExperience.endDate = "31-07-2023";

        // Vermont Solutions experience
        WorkExperience vermontExperience = new WorkExperience();
        vermontExperience.company = "Vermont Solutions";
        vermontExperience.description = "As a Java backend developer, I spearheaded the migration of microservices" +
                " from Spring to Quarkus, leveraging my expertise to optimize performance and enhance development " +
                "efficiency. I played a key role in supporting the team, serving as the go-to Quarkus expert and " +
                "ensuring a smooth transition.";
        vermontExperience.startDate = "01-08-2023";
        vermontExperience.endDate = "Current";

        List<WorkExperience> workExperiences = new ArrayList<>();
        workExperiences.add(everisExpererience);
        workExperiences.add(bitingBitExperience);
        workExperiences.add(vermontExperience);

        WorkExperience.persist(workExperiences);
    }

    private void createAndPersistAcademicStudies() {

    }

    private void createAndPersisLanguages() {

    }

}

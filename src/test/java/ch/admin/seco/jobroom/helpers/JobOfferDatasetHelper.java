package ch.admin.seco.jobroom.helpers;

import ch.admin.seco.jobroom.model.*;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.sql.Date;
import java.util.Arrays;

/**
 * Provides a configurable set of data in order to do tests.
 * Job offers can either be of type JobOffer or JsonObject.
 */
public final class JobOfferDatasetHelper {

    private static final String DEFAULT_PUBLICATION_START_DATE = "2100-01-01";

    // ****************************************
    // ***** type JobOffer
    // ****************************************

    public static JobOffer get() {

        return getWithPublicationStartDate(DEFAULT_PUBLICATION_START_DATE);
    }

    public static JobOffer getWithPublicationStartDate(String publicationStartDate) {
        return new JobOffer(
                null,
                null,
                Date.valueOf(publicationStartDate),
                Date.valueOf("2101-02-02"),
                "ref-284956",
                "https://www.seco.admin.ch/jobs/284956",
                new Job(
                        "Software engineer",
                        "Development of eGov applications",
                        80,
                        100,
                        Date.valueOf("2100-03-01"),
                        Date.valueOf("2102-04-02"),
                        new Location(
                                "CH",
                                "Bern",
                                "3010",
                                "All day in an office"
                        ),
                        Arrays.asList(
                                new LanguageSkill("1", LanguageSkill.Level.very_good, LanguageSkill.Level.good),
                                new LanguageSkill("2", LanguageSkill.Level.basic_knowledge, LanguageSkill.Level.no_knowledge)
                        )
                ),
                new Company(
                        "SECO",
                        "CH",
                        "Finkenhubelweg",
                        "12",
                        "Bern",
                        "3001",
                        "0581234567",
                        "info@seco.admin.ch",
                        "www.seco.admin.ch",
                        new Postbox(
                                "3002",
                                "Bern",
                                "3001"
                        )
                ),
                new Contact(
                        Contact.Title.madam,
                        "Alizée",
                        "Dupont",
                        "0791234567",
                        "alizee.dupont@seco.admin.ch"
                ),
                new Application(
                        0,
                        1,
                        1,
                        "Please apply online",
                        "https://www.seco.admin.ch/jobs/284956/apply"
                ),
                null,
                null,
                null,
                null,
                null
        );
    }

    // ****************************************
    // ***** type JsonObject
    // ****************************************

    public static JsonObject getJson() {

        return createJobOfferBuilder().build();
    }

    public static JsonObject getJsonWithPublicationStartDate(String publicationStartDate) {
        JsonObjectBuilder jobOfferBuilder = createJobOfferBuilder();
        jobOfferBuilder.add("publicationStartDate", publicationStartDate);
        return jobOfferBuilder.build();
    }

    public static JsonObject getJsonWithPublicationStartEndDate(String publicationStartDate, String publicationEndDate) {
        JsonObjectBuilder jobOfferBuilder = createJobOfferBuilder();
        jobOfferBuilder.add("publicationStartDate", publicationStartDate);
        jobOfferBuilder.add("publicationEndDate", publicationEndDate);
        return jobOfferBuilder.build();
    }

    public static JsonObject getJsonWithJobDescription(String jobDescription) {
        JsonObjectBuilder jobBuilder = createJobBuilder();
        jobBuilder.add("description", jobDescription);

        JsonObjectBuilder jobOfferBuilder = createJobOfferBuilder(jobBuilder, createContactBuilder(), createApplicationBuilder());
        return jobOfferBuilder.build();
    }

    public static JsonObject getJsonWithWorkingTimePercentage(int from, int to) {
        JsonObjectBuilder jobBuilder = createJobBuilder()
                .add("workingTimePercentageFrom", from)
                .add("workingTimePercentageTo", to);

        JsonObjectBuilder jobOfferBuilder = createJobOfferBuilder(jobBuilder, createContactBuilder(), createApplicationBuilder());
        return jobOfferBuilder.build();
    }

    public static JsonObject getJsonWithJobStartEndDate(String jobStartDate, String jobEndDate) {
        JsonObjectBuilder jobBuilder = createJobBuilder()
                .add("startDate", jobStartDate)
                .add("endDate", jobEndDate);

        JsonObjectBuilder jobOfferBuilder = createJobOfferBuilder(jobBuilder, createContactBuilder(), createApplicationBuilder());
        return jobOfferBuilder.build();
    }

    public static JsonObject getJsonWithLanguageSkills(int n) {
        JsonArrayBuilder languageSkills = Json.createArrayBuilder();
        for (int i = 0; i < n; i ++) {
            languageSkills.add(Json.createObjectBuilder()
                    .add("language", "2")
                    .add("spokenLevel", "good")
                    .add("writtenLevel", "good"));
        }

        return createJobOfferBuilder(createJobBuilder(languageSkills), createContactBuilder(), createApplicationBuilder()).build();
    }

    public static JsonObject getJsonWithLanguageSkills(String language, String spokenLevel, String writtenLevel) {
        JsonArrayBuilder languageSkills = Json.createArrayBuilder()
                .add(Json.createObjectBuilder()
                        .add("language", language)
                        .add("spokenLevel", spokenLevel)
                        .add("writtenLevel", writtenLevel));
        return createJobOfferBuilder(createJobBuilder(languageSkills), createContactBuilder(), createApplicationBuilder()).build();
    }

    public static JsonObject getJsonWithContactTitle(String title) {
        JsonObjectBuilder contact = createContactBuilder();
        contact.add("title", title);

        JsonObject jobOffer = createJobOfferBuilder(createJobBuilder(), contact, createApplicationBuilder()).build();
        return jobOffer;
    }

    public static JsonObject getJsonWithApplication(int telephonic, int written, int electronic) {
        JsonObjectBuilder application = Json.createObjectBuilder()
                .add("telephonic", telephonic)
                .add("written", written)
                .add("electronic", electronic);
        JsonObject jobOffer = createJobOfferBuilder(createJobBuilder(), createContactBuilder(), application).build();
        return jobOffer;
    }

    public static JsonObject getJsonIncomplete() {

        JsonObject jobOffer = Json.createObjectBuilder()
                .add("publicationStartDate", "2100-01-01")
                .build();

        return jobOffer;
    }

    public static JsonObject getJsonPartial() {

        JsonObject jobOffer = Json.createObjectBuilder()
                .add("contact", Json.createObjectBuilder()
                        .add("title", "madam")
                        .add("firstName", "Alizée")
                        .add("lastName", "Dupont")
                        .add("phoneNumber", "0791234567")
                        .add("email", "alizee.dupont@admin.ch"))
                .build();

        return jobOffer;
    }

    public static JsonObject getJsonInvalidLanguageSkill() {
        JsonObject jobOffer = createJobOfferBuilder(
                createJobBuilder(Json.createArrayBuilder()
                        .add(Json.createObjectBuilder()
                                .add("language", "fr")
                                .add("spokenLevel", "good")
                                .add("writtenLevel", "good"))),
                createContactBuilder(),
                createApplicationBuilder()).build();

        return jobOffer;
    }

    // ****************************************
    // ***** builders
    // ****************************************

    private static JsonObjectBuilder createJobOfferBuilder() {
        return createJobOfferBuilder(createJobBuilder(), createContactBuilder(), createApplicationBuilder());
    }

    private static JsonObjectBuilder createJobOfferBuilder(
            JsonObjectBuilder jobBuilder,
            JsonObjectBuilder contactBuilder,
            JsonObjectBuilder applicationBuilder) {
        JsonObjectBuilder jobOffer = Json.createObjectBuilder()
                .add("publicationStartDate", "2116-02-15")
                .add("publicationEndDate", "2116-04-01")
                .add("reference", "REF-847265")
                .add("url", "https://www.missionrealty.ch/jobs/financial-manager/preview")
                .add("job", jobBuilder)
                .add("company", Json.createObjectBuilder()
                        .add("name", "Mission Realty")
                        .add("countryCode", "CH")
                        .add("street", "Glennerstrasse")
                        .add("houseNumber", "62")
                        .add("locality", "Genolier")
                        .add("postalCode", "1272")
                        .add("phoneNumber", "0225176355")
                        .add("email", "info@missionrealty.ch")
                        .add("website", "www.missionrealty.ch")
                        .add("postbox", Json.createObjectBuilder()
                                .add("number", "1234")
                                .add("locality", "Genolier")
                                .add("postalCode", "1272")))
                .add("contact", contactBuilder)
                .add("application", applicationBuilder);
        return jobOffer;
    }

    private static JsonObjectBuilder createJobBuilder() {
        return createJobBuilder(createLanguageSkillsBuilder());
    }

    private static JsonObjectBuilder createJobBuilder(JsonArrayBuilder languageSkillsBuilder) {
        JsonObjectBuilder job = Json.createObjectBuilder()
                .add("title", "Financial manager")
                .add("description", "You will have to assist executives in making decisions that affect the organization")
                .add("workingTimePercentageFrom", 60)
                .add("workingTimePercentageTo", 90)
                .add("startDate", "2116-03-01")
                .add("endDate", "2118-05-31")
                .add("location", Json.createObjectBuilder()
                        .add("countryCode", "CH")
                        .add("locality", "Lausanne")
                        .add("postalCode", "1017")
                        .add("additionalDetails", "Possibility of having interesting bonus"))
                .add("languageSkills", languageSkillsBuilder);
        return job;
    }

    private static JsonArrayBuilder createLanguageSkillsBuilder() {
        JsonArrayBuilder languageSkills = Json.createArrayBuilder()
                .add(Json.createObjectBuilder()
                        .add("language", "2")
                        .add("spokenLevel", "very_good")
                        .add("writtenLevel", "very_good"))
                .add(Json.createObjectBuilder()
                        .add("language", "5")
                        .add("spokenLevel", "very_good")
                        .add("writtenLevel", "good"));

        return languageSkills;
    }

    private static JsonObjectBuilder createContactBuilder() {
        JsonObjectBuilder contact = Json.createObjectBuilder()
                .add("title", "mister")
                .add("firstName", "Bertrand")
                .add("lastName", "Boulé")
                .add("phoneNumber", "0795721186")
                .add("email", "bertrand.boule@missionrealty.ch");
        return contact;
    }

    private static JsonObjectBuilder createApplicationBuilder() {
        JsonObjectBuilder application = Json.createObjectBuilder()
                .add("telephonic", 1)
                .add("written", 0)
                .add("electronic", 1)
                .add("additionalDetails", "Please apply online")
                .add("url", "https://www.missionrealty.ch/jobs/financial-manager/apply");
        return application;
    }
}

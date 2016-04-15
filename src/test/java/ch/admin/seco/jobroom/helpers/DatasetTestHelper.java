package ch.admin.seco.jobroom.helpers;

import ch.admin.seco.jobroom.model.*;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.sql.Date;
import java.util.Arrays;

public final class DatasetTestHelper {

    private static final String DEFAULT_PUBLICATION_START_DATE = "2100-01-01";

    private static JsonObjectBuilder createCompleteJobOfferBuilder() {
        JsonObjectBuilder jobOffer = Json.createObjectBuilder()
                .add("publicationStartDate", "2116-02-15")
                .add("publicationEndDate", "2116-04-01")
                .add("job", Json.createObjectBuilder()
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
                        .add("languageSkills", Json.createArrayBuilder()
                                .add(Json.createObjectBuilder()
                                        .add("language", "fr")
                                        .add("spokenLevel", "very_good")
                                        .add("writtenLevel", "very_good"))
                                .add(Json.createObjectBuilder()
                                        .add("language", "en")
                                        .add("spokenLevel", "very_good")
                                        .add("writtenLevel", "good"))))
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
                .add("contact", Json.createObjectBuilder()
                        .add("title", "mister")
                        .add("firstName", "Bertrand")
                        .add("lastName", "Boulé")
                        .add("phoneNumber", "0795721186")
                        .add("email", "bertrand.boule@missionrealty.ch"))
                .add("application", Json.createObjectBuilder()
                        .add("telephonic", 1)
                        .add("written", 0)
                        .add("electronic", 1));
        return jobOffer;
    }

    public static JobOffer getCompleteJobOffer(String publicationStartDate) {
        return new JobOffer(
                null,
                null,
                Date.valueOf(publicationStartDate),
                Date.valueOf("2101-02-02"),
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
                                new LanguageSkill("de", LanguageSkill.Level.very_good, LanguageSkill.Level.good),
                                new LanguageSkill("fr", LanguageSkill.Level.average, LanguageSkill.Level.average)
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
                        1
                ),
                null
        );
    }

    public static JobOffer getCompleteJobOffer() {

        return getCompleteJobOffer(DEFAULT_PUBLICATION_START_DATE);
    }

    public static JsonObject getCompleteJobOfferJson() {

        return createCompleteJobOfferBuilder().build();
    }

    public static JsonObject getCompleteJobOfferJson(String publicationStartDate) {
        JsonObjectBuilder jobOfferBuilder = createCompleteJobOfferBuilder();
        jobOfferBuilder.add("publicationStartDate", publicationStartDate);
        return jobOfferBuilder.build();
    }

    public static JsonObject getIncompleteJobOfferJson() {

        JsonObject jobOffer = Json.createObjectBuilder()
                .add("publicationStartDate", "2000-01-01")
                .build();

        return jobOffer;
    }

    public static JsonObject getPartialJobOfferJson() {

        JsonObject jobOffer = Json.createObjectBuilder()
                .add("publicationStartDate", "2100-01-20")
                .add("contact", Json.createObjectBuilder()
                        .add("title", "madam"))
                .build();

        return jobOffer;
    }

    public static JsonObject getInvalidApplicationJobOfferJson() {
        JsonObjectBuilder jobOfferBuilder = createCompleteJobOfferBuilder();
        jobOfferBuilder.add("application", Json.createObjectBuilder()
                .add("telephonic", 1)
                .add("written", 0)
                .add("electronic", 2));
        return jobOfferBuilder.build();
    }

}

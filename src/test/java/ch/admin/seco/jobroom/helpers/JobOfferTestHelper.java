package ch.admin.seco.jobroom.helpers;

import ch.admin.seco.jobroom.model.*;

import javax.json.Json;
import javax.json.JsonObject;
import java.sql.Date;
import java.util.Arrays;

public final class JobOfferTestHelper {

    public static JobOffer getCompleteJobOffer() {

        return new JobOffer(
                null,
                null,
                Date.valueOf("2000-01-01"),
                Date.valueOf("2001-02-02"),
                new Job(
                        "Ingénieur",
                        "Software development",
                        80,
                        100,
                        Date.valueOf("2000-03-01"),
                        Date.valueOf("2002-04-02"),
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
                        Contact.Title.mister,
                        "Jean",
                        "Dupont",
                        "0791234567",
                        "jean.dupont@seco.admin.ch"
                ),
                new Application(
                        false,
                        true,
                        true
                )
        );
    }

    public static JsonObject getCompleteJobOfferJson() {

        JsonObject jobOffer = Json.createObjectBuilder()
                .add("publicationStartDate", "2000-01-01")
                .add("publicationEndDate", "2001-02-02")
                .add("job", Json.createObjectBuilder()
                    .add("title", "Ingénieur")
                    .add("description", "Software development")
                    .add("workingTimePercentageFrom", 80)
                    .add("workingTimePercentageTo", 100)
                    .add("startDate", "2000-03-01")
                    .add("endDate", "2002-04-02")
                    .add("location", Json.createObjectBuilder()
                        .add("countryCode", "CH")
                        .add("locality", "Bern")
                        .add("postalCode", "3010")
                        .add("additionalDetails", "All day in an office"))
                    .add("languageSkills", Json.createArrayBuilder()
                        .add(Json.createObjectBuilder()
                            .add("language", "de")
                            .add("spokenLevel", "very_good")
                            .add("writtenLevel", "good"))
                        .add(Json.createObjectBuilder()
                            .add("language", "fr")
                            .add("spokenLevel", "average")
                            .add("writtenLevel", "average"))))
                .add("company", Json.createObjectBuilder()
                    .add("name", "SECO")
                    .add("countryCode", "CH")
                    .add("street", "Finkenhubelweg")
                    .add("houseNumber", "12")
                    .add("locality", "Bern")
                    .add("postalCode", "3001")
                    .add("phoneNumber", "0581234567")
                    .add("email", "info@seco.admin.ch")
                    .add("website", "www.seco.admin.ch")
                    .add("postbox", Json.createObjectBuilder()
                        .add("number", "3002")
                        .add("locality", "Bern")
                        .add("postalCode", "3001")))
                .add("contact", Json.createObjectBuilder()
                    .add("title", "mister")
                    .add("firstName", "Jean")
                    .add("lastName", "Dupont")
                    .add("phoneNumber", "0791234567")
                    .add("email", "jean.dupont@seco.admin.ch"))
                .add("application", Json.createObjectBuilder()
                    .add("telephonic", false)
                    .add("written", true)
                    .add("electronic", true))
                .build();

        return jobOffer;
    }

    public static JsonObject getIncompleteJobOfferJson() {

        JsonObject jobOffer = Json.createObjectBuilder()
                .add("publicationStartDate", "2000-01-01")
                .build();

        return jobOffer;
    }

}

package ch.admin.seco.jobroom.helpers;

import ch.admin.seco.jobroom.model.*;
import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Provides default datasets used in tests (type JobOffer or JSONObject)
 */
public final class DatasetHelper {

    public static JobOffer get() {

        return new JobOffer(
                null,
                null,
                LocalDate.parse("2100-01-01"),
                LocalDate.parse("2101-02-02"),
                "ref-284956",
                "https://www.seco.admin.ch/jobs/284956",
                new Job(
                        "Software engineer",
                        "Development of eGov applications",
                        80,
                        100,
                        LocalDate.parse("2100-03-01"),
                        LocalDate.parse("2102-04-02"),
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
                        false,
                        true,
                        true,
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

    public static JSONObject getJson() {
        return new JSONObject()
                .put("publicationStartDate", "2116-02-15")
                .put("publicationEndDate", "2116-04-01")
                .put("reference", "REF-847265")
                .put("url", "https://www.missionrealty.ch/jobs/financial-manager/preview")
                .put("job", new JSONObject()
                        .put("title", "Financial manager")
                        .put("description", "You will have to assist executives in making decisions that affect the organization")
                        .put("workingTimePercentageFrom", 60)
                        .put("workingTimePercentageTo", 90)
                        .put("startDate", "2116-03-01")
                        .put("endDate", "2118-05-31")
                        .put("location", new JSONObject()
                                .put("countryCode", "CH")
                                .put("locality", "Lausanne")
                                .put("postalCode", "1017")
                                .put("additionalDetails", "Possibility of having interesting bonus"))
                        .put("languageSkills", new JSONArray()
                                .put(new JSONObject()
                                        .put("language", "2")
                                        .put("spokenLevel", "very_good")
                                        .put("writtenLevel", "very_good"))
                                .put(new JSONObject()
                                        .put("language", "5")
                                        .put("spokenLevel", "very_good")
                                        .put("writtenLevel", "good"))))
                .put("company", new JSONObject()
                        .put("name", "Mission Realty")
                        .put("countryCode", "CH")
                        .put("street", "Glennerstrasse")
                        .put("houseNumber", "62")
                        .put("locality", "Genolier")
                        .put("postalCode", "1272")
                        .put("phoneNumber", "0225176355")
                        .put("email", "info@missionrealty.ch")
                        .put("website", "www.missionrealty.ch")
                        .put("postbox", new JSONObject()
                                .put("number", "1234")
                                .put("locality", "Genolier")
                                .put("postalCode", "1272")))
                .put("contact", new JSONObject()
                        .put("title", "mister")
                        .put("firstName", "Bertrand")
                        .put("lastName", "Boulé")
                        .put("phoneNumber", "0795721186")
                        .put("email", "bertrand.boule@missionrealty.ch"))
                .put("application", new JSONObject()
                        .put("telephonic", true)
                        .put("written", false)
                        .put("electronic", true)
                        .put("additionalDetails", "Please apply online")
                        .put("url", "https://www.missionrealty.ch/jobs/financial-manager/apply"));
    }

    // ****************************************
    // ***** Methods used in several classes
    // ****************************************

    public static JSONObject getJsonWithLanguageSkills(String language, String spokenLevel, String writtenLevel) {

        JSONObject jobOffer = getJson();
        jobOffer.getJSONObject("job").put("languageSkills", new JSONArray().put(new JSONObject()
                .put("language", language)
                .put("spokenLevel", spokenLevel)
                .put("writtenLevel", writtenLevel))).toString();

        return jobOffer;
    }

    public static JSONObject getJsonPartial() {

        return getJson().getJSONObject("contact");
    }
}

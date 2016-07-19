package ch.admin.seco.jobroom.doc;

import ch.admin.seco.jobroom.ApiApplication;
import ch.admin.seco.jobroom.helpers.ApiTestHelper;
import ch.admin.seco.jobroom.helpers.JobOfferDatasetHelper;
import ch.admin.seco.jobroom.repository.JobOfferRepository;
import ch.admin.seco.jobroom.repository.RestAccessKeyRepository;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.restdocs.RestDocumentation;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApiApplication.class)
@WebAppConfiguration
/**
 * This class generates doc snippets to be included in Asciidoc documentation
 */
public class ApiDocCreate {

    private MockMvc mockMvc;

    @Rule
    public final RestDocumentation restDocumentation = new RestDocumentation("build/generated-snippets");

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    JobOfferRepository jobOfferRepository;

    @Autowired
    RestAccessKeyRepository restAccessKeyRepository;

    @Autowired
    ApiTestHelper apiTestHelper;

    @Before
    public void setup() throws Exception {

        this.mockMvc = webAppContextSetup(webApplicationContext)
                .apply(apiTestHelper.getDocumentationConfiguration(restDocumentation))
                .apply(springSecurity())
                .build();

        restAccessKeyRepository.save(apiTestHelper.getDefaultRestAccessKey());
    }

    @After
    public void cleanup() {
        apiTestHelper.authenticateDefault();
        jobOfferRepository.deleteAll();
        restAccessKeyRepository.deleteAll();
        apiTestHelper.unAuthenticate();
    }

    @Test
    public void createJobOffer() throws Exception {

        // TODO add constraints in documentation (should be included using REST Docs mechanism : ConstraintDescriptions (JPA / SQL))

        String jobOfferJson = JobOfferDatasetHelper.getJson().toString();

        this.mockMvc.perform(post("/joboffers") // FIXME design the "versioning approach" (externally?)
                .with(apiTestHelper.getDefaultHttpBasic())
                .contentType(apiTestHelper.getContentType())
                .content(jobOfferJson))
                .andExpect(status().isCreated())

                .andDo(document("{method-name}", apiTestHelper.getPreprocessRequest(), apiTestHelper.getPreprocessResponse(),
                        requestFields(
                                fieldWithPath("publicationStartDate")
                                        .description("Date from which the job offer is visible on the publication medias. " +
                                                "Job offer is considered published once that publicationStartDate is smaller than current date.")
                                        .attributes(key("constraints").value(
                                                "* Not null.\n" +
                                                "* Cannot be smaller than current date.\n" +
                                                "* Cannot be modified once that job offer has been published.")),
                                fieldWithPath("publicationEndDate")
                                        .description("After this date, the job offer is removed from the publication medias.")
                                        .attributes(key("constraints").value("* Must be greater than publicationStartDate.")),
                                fieldWithPath("reference")
                                        .description("External reference (e.g. can be used to link API's jobs to another id system).")
                                        .attributes(key("constraints").value("")),
                                fieldWithPath("job")
                                        .description("Description of the job\n\n(further information below)")
                                        .attributes(key("constraints").value("* Not null.")),
                                fieldWithPath("company")
                                        .description("The company that offers the job\n\n(further information below)")
                                        .attributes(key("constraints").value("* Not null.")),
                                fieldWithPath("contact")
                                        .description("Contact person about the job offer\n\n(further information below)")
                                        .attributes(key("constraints").value("* Not null.")),
                                fieldWithPath("application")
                                        .description("Support for submitting the job application\n\n(further information below")
                                        .attributes(key("constraints").value("* Not null"))
                        )))
                .andDo(document("{method-name}-job", apiTestHelper.getPreprocessRequest(), apiTestHelper.getPreprocessResponse(),
                        requestFields(
                                fieldWithPath("publicationStartDate").ignored(),
                                fieldWithPath("publicationEndDate").ignored(),
                                fieldWithPath("reference").ignored(),
                                fieldWithPath("job").ignored(),
                                fieldWithPath("job.title")
                                        .description("")
                                        .attributes(key("constraints").value("* Not null.")),
                                fieldWithPath("job.description")
                                        .description("")
                                        .attributes(key("constraints").value("* Not null.\n* Length max 10000 characters.")),
                                fieldWithPath("job.workingTimePercentageFrom")
                                        .description("")
                                        .attributes(key("constraints").value("* Not null.\n* Must be greater than 0.")),
                                fieldWithPath("job.workingTimePercentageTo")
                                        .description("")
                                        .attributes(key("constraints").value(
                                                "* Not null.\n" +
                                                "* Must be greater or equal than workingTimePercentageFrom.\n" +
                                                "* Must be less or equal than 100.")),
                                fieldWithPath("job.startDate")
                                        .description("When null, the job is supposed to start immediately.")
                                        .attributes(key("constraints").value("")),
                                fieldWithPath("job.endDate")
                                        .description("When null, the job is considered as permanent.")
                                        .attributes(key("constraints").value("* Must be greater than startDate")),
                                fieldWithPath("job.location.countryCode")
                                        .description("")
                                        .attributes(key("constraints").value(
                                                "* Not null.\n" +
                                                "* Must be one of authorized country codes (see section <<Country codes>>)")),
                                fieldWithPath("job.location.locality")
                                        .description("")
                                        .attributes(key("constraints").value("* Not null.")),
                                fieldWithPath("job.location.postalCode")
                                        .description("")
                                        .attributes(key("constraints").value("* Not null.")),
                                fieldWithPath("job.location.additionalDetails")
                                        .description("More information can be added as free text.")
                                        .attributes(key("constraints").value("")),
                                fieldWithPath("job.languageSkills")
                                        .description("It is possible to define up to 5 language skills required by the job.")
                                        .attributes(key("constraints").value("* Not null.\n* Size must be between 0 and 5.")),
                                fieldWithPath("job.languageSkills[].language")
                                        .description("Required language's code.")
                                        .attributes(key("constraints").value(
                                                "* Not null.\n" +
                                                "* Must be one of authorized language codes (see section <<Language codes>>)")),
                                fieldWithPath("job.languageSkills[].spokenLevel")
                                        .description("Required spoken level.")
                                        .attributes(key("constraints").value(
                                                "* Not null.\n" +
                                                "* Must be in ('no_knowledge', 'basic_knowledge', 'good', 'very_good').")),
                                fieldWithPath("job.languageSkills[].writtenLevel")
                                        .description("Required written level.")
                                        .attributes(key("constraints").value(
                                                "* Not null.\n" +
                                                "* Must be in ('no_knowledge', 'basic_knowledge', 'good', 'very_good').")),
                                fieldWithPath("company").ignored(),
                                fieldWithPath("contact").ignored(),
                                fieldWithPath("application").ignored()
                )))
                .andDo(document("{method-name}-company", apiTestHelper.getPreprocessRequest(), apiTestHelper.getPreprocessResponse(),
                        requestFields(
                                fieldWithPath("publicationStartDate").ignored(),
                                fieldWithPath("publicationEndDate").ignored(),
                                fieldWithPath("reference").ignored(),
                                fieldWithPath("job").ignored(),
                                fieldWithPath("company").ignored(),
                                fieldWithPath("company.name")
                                        .description("")
                                        .attributes(key("constraints").value("* Not null.")),
                                fieldWithPath("company.countryCode")
                                        .description("")
                                        .attributes(key("constraints").value(
                                                "* Not null.\n" +
                                                "* Must be one of authorized language codes (see section <<Language codes>>)")),
                                fieldWithPath("company.street")
                                        .description("")
                                        .attributes(key("constraints").value("* Not null.")),
                                fieldWithPath("company.houseNumber")
                                        .description("")
                                        .attributes(key("constraints").value("* Not null.")),
                                fieldWithPath("company.locality")
                                        .description("")
                                        .attributes(key("constraints").value("* Not null.")),
                                fieldWithPath("company.postalCode")
                                        .description("")
                                        .attributes(key("constraints").value("* Not null.")),
                                fieldWithPath("company.phoneNumber")
                                        .description("")
                                        .attributes(key("constraints").value("")),
                                fieldWithPath("company.email")
                                        .description("")
                                        .attributes(key("constraints").value("")),
                                fieldWithPath("company.website")
                                        .description("")
                                        .attributes(key("constraints").value("")),
                                fieldWithPath("company.postbox.number")
                                        .description("")
                                        .attributes(key("constraints").value("")),
                                fieldWithPath("company.postbox.locality")
                                        .description("")
                                        .attributes(key("constraints").value("")),
                                fieldWithPath("company.postbox.postalCode")
                                        .description("")
                                        .attributes(key("constraints").value("")),
                                fieldWithPath("contact").ignored(),
                                fieldWithPath("application").ignored()
                        )))
                .andDo(document("{method-name}-contact", apiTestHelper.getPreprocessRequest(), apiTestHelper.getPreprocessResponse(),
                        requestFields(
                                fieldWithPath("publicationStartDate").ignored(),
                                fieldWithPath("publicationEndDate").ignored(),
                                fieldWithPath("reference").ignored(),
                                fieldWithPath("job").ignored(),
                                fieldWithPath("company").ignored(),
                                fieldWithPath("contact").ignored(),
                                fieldWithPath("contact.title")
                                        .description("")
                                        .attributes(key("constraints").value("* Not null.\n* Must be in ('mister', 'madam').")),
                                fieldWithPath("contact.firstName")
                                        .description("")
                                        .attributes(key("constraints").value("* Not null.")),
                                fieldWithPath("contact.lastName")
                                        .description("")
                                        .attributes(key("constraints").value("* Not null.")),
                                fieldWithPath("contact.phoneNumber")
                                        .description("")
                                        .attributes(key("constraints").value("* Not null.")),
                                fieldWithPath("contact.email")
                                        .description("")
                                        .attributes(key("constraints").value("* Not null.")),
                                fieldWithPath("application").ignored()
                        )))
                .andDo(document("{method-name}-application", apiTestHelper.getPreprocessRequest(), apiTestHelper.getPreprocessResponse(),
                        requestFields(
                                fieldWithPath("publicationStartDate").ignored(),
                                fieldWithPath("publicationEndDate").ignored(),
                                fieldWithPath("reference").ignored(),
                                fieldWithPath("job").ignored(),
                                fieldWithPath("company").ignored(),
                                fieldWithPath("contact").ignored(),
                                fieldWithPath("application").ignored(),
                                fieldWithPath("application.telephonic")
                                        .description("It is tolerated to apply by telephonic way.")
                                        .attributes(key("constraints").value("* Not null.\n* Must be in (0, 1).")),
                                fieldWithPath("application.written")
                                        .description("It is tolerated to apply by written way.")
                                        .attributes(key("constraints").value("* Not null.\n* Must be in (0, 1).")),
                                fieldWithPath("application.electronic")
                                        .description("It is tolerated to apply by electronic way.")
                                        .attributes(key("constraints").value("* Not null.\n* Must be in (0, 1).")),
                                fieldWithPath("application.additionalDetails")
                                        .description("More information can be added as free text.")
                                        .attributes(key("constraints").value(""))
                        )));

        this.mockMvc.perform(get("/joboffers").with(apiTestHelper.getDefaultHttpBasic()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.jobOffers", Matchers.hasSize(1)));
    }
}

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
                                        .description("todo")
                                        .attributes(key("constraints").value(
                                                "* Not null.\n" +
                                                "* Cannot be smaller than current date.\n" +
                                                "* Cannot be modified once that job offer has been published.")),
                                fieldWithPath("publicationEndDate")
                                        .description("todo")
                                        .attributes(key("constraints").value("* Must be greater than publicationStartDate.")),
                                fieldWithPath("job")
                                        .description("Description of the job\n\n(further information bellow)")
                                        .attributes(key("constraints").value("* Not null.")),
                                fieldWithPath("company")
                                        .description("The company that offers the job\n\n(further information bellow)")
                                        .attributes(key("constraints").value("* Not null.")),
                                fieldWithPath("contact")
                                        .description("Contact person about the job offer\n\n(further information bellow)")
                                        .attributes(key("constraints").value("* Not null.")),
                                fieldWithPath("application")
                                        .description("Support for submitting the job application\n\n(further information bellow")
                                        .attributes(key("constraints").value("* Not null"))
                        )))
                .andDo(document("{method-name}-job", apiTestHelper.getPreprocessRequest(), apiTestHelper.getPreprocessResponse(),
                        requestFields(
                                fieldWithPath("publicationStartDate").ignored(),
                                fieldWithPath("publicationEndDate").ignored(),
                                fieldWithPath("job").ignored(),
                                fieldWithPath("job.title")
                                        .description("todo")
                                        .attributes(key("constraints").value("* Not null.")),
                                fieldWithPath("job.description")
                                        .description("todo")
                                        .attributes(key("constraints").value("* Not null.\n* Length max 10000 characters.")),
                                fieldWithPath("job.workingTimePercentageFrom")
                                        .description("todo")
                                        .attributes(key("constraints").value("* Not null.\n* Must be greater than 0.")),
                                fieldWithPath("job.workingTimePercentageTo")
                                        .description("todo")
                                        .attributes(key("constraints").value(
                                                "* Not null.\n" +
                                                "* Must be greater or equal than workingTimePercentageFrom.\n" +
                                                "* Must be less or equal than 100.")),
                                fieldWithPath("job.startDate")
                                        .description("todo")
                                        .attributes(key("constraints").value("")),
                                fieldWithPath("job.endDate")
                                        .description("todo")
                                        .attributes(key("constraints").value("* Must be greater than startDate")),
                                fieldWithPath("job.location.countryCode")
                                        .description("todo")
                                        .attributes(key("constraints").value("* Not null.")),
                                fieldWithPath("job.location.locality")
                                        .description("todo")
                                        .attributes(key("constraints").value("* Not null.")),
                                fieldWithPath("job.location.postalCode")
                                        .description("todo")
                                        .attributes(key("constraints").value("* Not null.")),
                                fieldWithPath("job.location.additionalDetails")
                                        .description("todo")
                                        .attributes(key("constraints").value("")),
                                fieldWithPath("job.languageSkills")
                                        .description("todo")
                                        .attributes(key("constraints").value("* Not null.\n* Size must be between 2 and 5.")),
                                fieldWithPath("job.languageSkills[].language")
                                        .description("todo")
                                        .attributes(key("constraints").value(
                                                "* Not null.\n" +
                                                "* Must be one of authorized language codes (see section <<Language codes>>)")),
                                fieldWithPath("job.languageSkills[].spokenLevel")
                                        .description("todo")
                                        .attributes(key("constraints").value("* Not null.\n* Must be in ('average', 'good', 'very_good').")),
                                fieldWithPath("job.languageSkills[].writtenLevel")
                                        .description("todo")
                                        .attributes(key("constraints").value("* Not null.\n* Must be in ('average', 'good', 'very_good').")),
                                fieldWithPath("company").ignored(),
                                fieldWithPath("contact").ignored(),
                                fieldWithPath("application").ignored()
                )))
                .andDo(document("{method-name}-company", apiTestHelper.getPreprocessRequest(), apiTestHelper.getPreprocessResponse(),
                        requestFields(
                                fieldWithPath("publicationStartDate").ignored(),
                                fieldWithPath("publicationEndDate").ignored(),
                                fieldWithPath("job").ignored(),
                                fieldWithPath("company").ignored(),
                                fieldWithPath("company.name")
                                        .description("todo")
                                        .attributes(key("constraints").value("* Not null.")),
                                fieldWithPath("company.countryCode")
                                        .description("todo")
                                        .attributes(key("constraints").value("* Not null.")),
                                fieldWithPath("company.street")
                                        .description("todo")
                                        .attributes(key("constraints").value("* Not null.")),
                                fieldWithPath("company.houseNumber")
                                        .description("todo")
                                        .attributes(key("constraints").value("* Not null.")),
                                fieldWithPath("company.locality")
                                        .description("todo")
                                        .attributes(key("constraints").value("* Not null.")),
                                fieldWithPath("company.postalCode")
                                        .description("todo")
                                        .attributes(key("constraints").value("* Not null.")),
                                fieldWithPath("company.phoneNumber")
                                        .description("todo")
                                        .attributes(key("constraints").value("* Not null.")),
                                fieldWithPath("company.email")
                                        .description("todo")
                                        .attributes(key("constraints").value("* Not null.")),
                                fieldWithPath("company.website")
                                        .description("todo")
                                        .attributes(key("constraints").value("* Not null.")),
                                fieldWithPath("company.postbox.number")
                                        .description("todo")
                                        .attributes(key("constraints").value("* Not null.")),
                                fieldWithPath("company.postbox.locality")
                                        .description("todo")
                                        .attributes(key("constraints").value("* Not null.")),
                                fieldWithPath("company.postbox.postalCode")
                                        .description("todo")
                                        .attributes(key("constraints").value("* Not null.")),
                                fieldWithPath("contact").ignored(),
                                fieldWithPath("application").ignored()
                        )))
                .andDo(document("{method-name}-contact", apiTestHelper.getPreprocessRequest(), apiTestHelper.getPreprocessResponse(),
                        requestFields(
                                fieldWithPath("publicationStartDate").ignored(),
                                fieldWithPath("publicationEndDate").ignored(),
                                fieldWithPath("job").ignored(),
                                fieldWithPath("company").ignored(),
                                fieldWithPath("contact").ignored(),
                                fieldWithPath("contact.title")
                                        .description("todo")
                                        .attributes(key("constraints").value("* Not null.\n* Must be in ('mister', 'madam').")),
                                fieldWithPath("contact.firstName")
                                        .description("todo")
                                        .attributes(key("constraints").value("* Not null.")),
                                fieldWithPath("contact.lastName")
                                        .description("todo")
                                        .attributes(key("constraints").value("* Not null.")),
                                fieldWithPath("contact.phoneNumber")
                                        .description("todo")
                                        .attributes(key("constraints").value("* Not null.")),
                                fieldWithPath("contact.email")
                                        .description("todo")
                                        .attributes(key("constraints").value("* Not null.")),
                                fieldWithPath("application").ignored()
                        )))
                .andDo(document("{method-name}-application", apiTestHelper.getPreprocessRequest(), apiTestHelper.getPreprocessResponse(),
                        requestFields(
                                fieldWithPath("publicationStartDate").ignored(),
                                fieldWithPath("publicationEndDate").ignored(),
                                fieldWithPath("job").ignored(),
                                fieldWithPath("company").ignored(),
                                fieldWithPath("contact").ignored(),
                                fieldWithPath("application").ignored(),
                                fieldWithPath("application.telephonic")
                                        .description("todo")
                                        .attributes(key("constraints").value("* Not null.\n* Must be in (0, 1).")),
                                fieldWithPath("application.written")
                                        .description("todo")
                                        .attributes(key("constraints").value("* Not null.\n* Must be in (0, 1).")),
                                fieldWithPath("application.electronic")
                                        .description("todo")
                                        .attributes(key("constraints").value("* Not null.\n* Must be in (0, 1)."))
                        )));

        this.mockMvc.perform(get("/joboffers").with(apiTestHelper.getDefaultHttpBasic()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.jobOffers", Matchers.hasSize(1)));
    }
}

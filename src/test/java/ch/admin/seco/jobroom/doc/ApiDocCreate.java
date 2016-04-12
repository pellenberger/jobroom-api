package ch.admin.seco.jobroom.doc;

import ch.admin.seco.jobroom.ApiApplication;
import ch.admin.seco.jobroom.helpers.ApiTestHelper;
import ch.admin.seco.jobroom.helpers.DatasetTestHelper;
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
    }

    @Test
    public void createJobOffer() throws Exception {

        // TODO add constraints in documentation (should be included using REST Docs mechanism : ConstraintDescriptions (JPA / SQL))
        // TODO only describe subobjects here (not GET request anymore) ??

        String jobOfferJson = DatasetTestHelper.getCompleteJobOfferJson().toString();

        this.mockMvc.perform(post("/joboffers") // FIXME design the "versioning approach" (externally?)
                .with(apiTestHelper.getDefaultHttpBasic())
                .contentType(apiTestHelper.getContentType())
                .content(jobOfferJson))
                .andExpect(status().isCreated())

                .andDo(document("{method-name}", apiTestHelper.getPreprocessRequest(), apiTestHelper.getPreprocessResponse(),
                        requestFields(
                                fieldWithPath("publicationStartDate").description("todo"),
                                fieldWithPath("publicationEndDate").description("todo"),
                                fieldWithPath("job").description("Description of the job (further information bellow)"),
                                fieldWithPath("company").description("The company that offers the job (further information bellow)"),
                                fieldWithPath("contact").description("Contact person about the job offer (further information bellow)"),
                                fieldWithPath("application").description("Support for submitting the job application (further information bellow")
                        )))
                .andDo(document("{method-name}-job", apiTestHelper.getPreprocessRequest(), apiTestHelper.getPreprocessResponse(),
                        requestFields(
                                fieldWithPath("publicationStartDate").ignored(),
                                fieldWithPath("publicationEndDate").ignored(),
                                fieldWithPath("job").ignored(),
                                fieldWithPath("job.title").description("todo"),
                                fieldWithPath("job.description").description("todo"),
                                fieldWithPath("job.workingTimePercentageFrom").description("todo"),
                                fieldWithPath("job.workingTimePercentageTo").description("todo"),
                                fieldWithPath("job.startDate").description("todo"),
                                fieldWithPath("job.endDate").description("todo"),
                                fieldWithPath("job.location.countryCode").description("todo"),
                                fieldWithPath("job.location.locality").description("todo"),
                                fieldWithPath("job.location.postalCode").description("todo"),
                                fieldWithPath("job.location.additionalDetails").description("todo"),
                                fieldWithPath("job.languageSkills").description("todo"),
                                fieldWithPath("job.languageSkills[].language").description("todo"),
                                fieldWithPath("job.languageSkills[].spokenLevel").description("todo"),
                                fieldWithPath("job.languageSkills[].writtenLevel").description("todo"),
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
                                fieldWithPath("company.name").description("todo"),
                                fieldWithPath("company.countryCode").description("todo"),
                                fieldWithPath("company.street").description("todo"),
                                fieldWithPath("company.houseNumber").description("todo"),
                                fieldWithPath("company.locality").description("todo"),
                                fieldWithPath("company.postalCode").description("todo"),
                                fieldWithPath("company.phoneNumber").description("todo"),
                                fieldWithPath("company.email").description("todo"),
                                fieldWithPath("company.website").description("todo"),
                                fieldWithPath("company.postbox.number").description("todo"),
                                fieldWithPath("company.postbox.locality").description("todo"),
                                fieldWithPath("company.postbox.postalCode").description("todo"),
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
                                fieldWithPath("contact.title").description("todo"),
                                fieldWithPath("contact.firstName").description("todo"),
                                fieldWithPath("contact.lastName").description("todo"),
                                fieldWithPath("contact.phoneNumber").description("todo"),
                                fieldWithPath("contact.email").description("todo"),
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
                                fieldWithPath("application.telephonic").description("todo"),
                                fieldWithPath("application.written").description("todo"),
                                fieldWithPath("application.electronic").description("todo")
                        )));

        this.mockMvc.perform(get("/joboffers").with(apiTestHelper.getDefaultHttpBasic()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.jobOffers", Matchers.hasSize(1)));
    }
}

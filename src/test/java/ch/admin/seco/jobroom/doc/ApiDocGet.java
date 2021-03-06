package ch.admin.seco.jobroom.doc;

import ch.admin.seco.jobroom.ApiApplication;
import ch.admin.seco.jobroom.helpers.TestHelper;
import ch.admin.seco.jobroom.helpers.DatasetHelper;
import ch.admin.seco.jobroom.model.JobOffer;
import ch.admin.seco.jobroom.model.RestAccessKey;
import ch.admin.seco.jobroom.repository.JobOfferRepository;
import ch.admin.seco.jobroom.repository.RestAccessKeyRepository;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@WebAppConfiguration
/**
 * This class generates doc snippets to be included in Asciidoc documentation
 */
public class ApiDocGet {

    private MockMvc mockMvc;

    private int idNewJobOffer;

    @Rule
    public JUnitRestDocumentation restDocumentation =
            new JUnitRestDocumentation("build/generated-snippets");

    @Value("${api.basePath}")
    private String basePath;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    JobOfferRepository jobOfferRepository;

    @Autowired
    RestAccessKeyRepository restAccessKeyRepository;

    @Autowired
    TestHelper testHelper;

    @Before
    public void setup() throws Exception {

        this.mockMvc = webAppContextSetup(webApplicationContext)
                .apply(testHelper.getDocumentationConfiguration(restDocumentation))
                .apply(springSecurity())
                .build();

        RestAccessKey restAccessKey = testHelper.getDefaultRestAccessKey();
        restAccessKeyRepository.save(restAccessKey);

        testHelper.authenticateDefault();

        JobOffer jobOffer = DatasetHelper.get();
        jobOffer.setOwner(restAccessKey);
        idNewJobOffer = jobOfferRepository.save(jobOffer).getId();

        testHelper.unAuthenticate();
    }

    @After
    public void cleanup() {
        testHelper.authenticate("user", "password");
        jobOfferRepository.deleteAll();
        restAccessKeyRepository.deleteAll();
        testHelper.unAuthenticate();
    }

    @Test
    public void getJobOffer() throws Exception {

        this.mockMvc.perform(get(basePath + "/joboffers/" + idNewJobOffer)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .with(testHelper.getDefaultHttpBasic())
                .contextPath(basePath))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.publicationStartDate", Matchers.is("2100-01-01")))
                .andExpect(jsonPath("$.publicationEndDate", Matchers.is("2101-02-02")))
                .andExpect(jsonPath("$.reference", Matchers.is("ref-284956")))
                .andExpect(jsonPath("$.url", Matchers.is("https://www.seco.admin.ch/jobs/284956")))
                .andExpect(jsonPath("$.job.title", Matchers.is("Software engineer")))
                .andExpect(jsonPath("$.job.description", Matchers.is("Development of eGov applications")))
                .andExpect(jsonPath("$.job.workingTimePercentageFrom", Matchers.is(80)))
                .andExpect(jsonPath("$.job.workingTimePercentageTo", Matchers.is(100)))
                .andExpect(jsonPath("$.job.startDate", Matchers.is("2100-03-01")))
                .andExpect(jsonPath("$.job.endDate", Matchers.is("2102-04-02")))
                .andExpect(jsonPath("$.job.location.countryCode", Matchers.is("CH")))
                .andExpect(jsonPath("$.job.location.locality", Matchers.is("Bern")))
                .andExpect(jsonPath("$.job.location.postalCode", Matchers.is("3010")))
                .andExpect(jsonPath("$.job.location.additionalDetails", Matchers.is("All day in an office")))
                .andExpect(jsonPath("$.job.languageSkills", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.job.languageSkills[0].language", Matchers.is("1")))
                .andExpect(jsonPath("$.job.languageSkills[0].spokenLevel", Matchers.is("very_good")))
                .andExpect(jsonPath("$.job.languageSkills[0].writtenLevel", Matchers.is("good")))
                .andExpect(jsonPath("$.job.languageSkills[1].language", Matchers.is("2")))
                .andExpect(jsonPath("$.job.languageSkills[1].spokenLevel", Matchers.is("basic_knowledge")))
                .andExpect(jsonPath("$.job.languageSkills[1].writtenLevel", Matchers.is("no_knowledge")))
                .andExpect(jsonPath("$.company.name", Matchers.is("SECO")))
                .andExpect(jsonPath("$.company.countryCode", Matchers.is("CH")))
                .andExpect(jsonPath("$.company.street", Matchers.is("Finkenhubelweg")))
                .andExpect(jsonPath("$.company.houseNumber", Matchers.is("12")))
                .andExpect(jsonPath("$.company.locality", Matchers.is("Bern")))
                .andExpect(jsonPath("$.company.postalCode", Matchers.is("3001")))
                .andExpect(jsonPath("$.company.phoneNumber", Matchers.is("0581234567")))
                .andExpect(jsonPath("$.company.email", Matchers.is("info@seco.admin.ch")))
                .andExpect(jsonPath("$.company.website", Matchers.is("www.seco.admin.ch")))
                .andExpect(jsonPath("$.company.postbox.number", Matchers.is("3002")))
                .andExpect(jsonPath("$.company.postbox.locality", Matchers.is("Bern")))
                .andExpect(jsonPath("$.company.postbox.postalCode", Matchers.is("3001")))
                .andExpect(jsonPath("$.contact.title", Matchers.is("madam")))
                .andExpect(jsonPath("$.contact.firstName", Matchers.is("Alizée")))
                .andExpect(jsonPath("$.contact.lastName", Matchers.is("Dupont")))
                .andExpect(jsonPath("$.contact.phoneNumber", Matchers.is("0791234567")))
                .andExpect(jsonPath("$.contact.email", Matchers.is("alizee.dupont@seco.admin.ch")))
                .andExpect(jsonPath("$.application.telephonic", Matchers.is(false)))
                .andExpect(jsonPath("$.application.written", Matchers.is(true)))
                .andExpect(jsonPath("$.application.electronic", Matchers.is(true)))
                .andExpect(jsonPath("$.application.additionalDetails", Matchers.is("Please apply online")))
                .andExpect(jsonPath("$.application.url", Matchers.is("https://www.seco.admin.ch/jobs/284956/apply")))

                .andDo(document("{method-name}", testHelper.getPreprocessRequest(), testHelper.getPreprocessResponse(),
                        responseFields(
                                fieldWithPath("publicationStartDate").ignored(),
                                fieldWithPath("publicationEndDate").ignored(),
                                fieldWithPath("reference").ignored(),
                                fieldWithPath("url").ignored(),
                                fieldWithPath("job").ignored(),
                                fieldWithPath("company").ignored(),
                                fieldWithPath("contact").ignored(),
                                fieldWithPath("application").ignored(),
                                fieldWithPath("_links").ignored()
                        )))
                .andDo(document("{method-name}-job", testHelper.getPreprocessRequest(), testHelper.getPreprocessResponse(),
                        responseFields(
                                fieldWithPath("publicationStartDate").ignored(),
                                fieldWithPath("publicationEndDate").ignored(),
                                fieldWithPath("reference").ignored(),
                                fieldWithPath("url").ignored(),
                                fieldWithPath("job").ignored(),
                                fieldWithPath("job.title").ignored(),
                                fieldWithPath("job.description").ignored(),
                                fieldWithPath("job.workingTimePercentageFrom").ignored(),
                                fieldWithPath("job.workingTimePercentageTo").ignored(),
                                fieldWithPath("job.startDate").ignored(),
                                fieldWithPath("job.endDate").ignored(),
                                fieldWithPath("job.location.countryCode").ignored(),
                                fieldWithPath("job.location.locality").ignored(),
                                fieldWithPath("job.location.postalCode").ignored(),
                                fieldWithPath("job.location.additionalDetails").ignored(),
                                fieldWithPath("job.languageSkills").ignored(),
                                fieldWithPath("job.languageSkills[].language").ignored(),
                                fieldWithPath("job.languageSkills[].spokenLevel").ignored(),
                                fieldWithPath("job.languageSkills[].writtenLevel").ignored(),
                                fieldWithPath("company").ignored(),
                                fieldWithPath("contact").ignored(),
                                fieldWithPath("application").ignored(),
                                fieldWithPath("_links").ignored()
                        )))
                .andDo(document("{method-name}-company", testHelper.getPreprocessRequest(), testHelper.getPreprocessResponse(),
                        responseFields(
                                fieldWithPath("publicationStartDate").ignored(),
                                fieldWithPath("publicationEndDate").ignored(),
                                fieldWithPath("reference").ignored(),
                                fieldWithPath("url").ignored(),
                                fieldWithPath("job").ignored(),
                                fieldWithPath("company").ignored(),
                                fieldWithPath("company.name").ignored(),
                                fieldWithPath("company.countryCode").ignored(),
                                fieldWithPath("company.street").ignored(),
                                fieldWithPath("company.houseNumber").ignored(),
                                fieldWithPath("company.locality").ignored(),
                                fieldWithPath("company.postalCode").ignored(),
                                fieldWithPath("company.phoneNumber").ignored(),
                                fieldWithPath("company.email").ignored(),
                                fieldWithPath("company.website").ignored(),
                                fieldWithPath("company.postbox.number").ignored(),
                                fieldWithPath("company.postbox.locality").ignored(),
                                fieldWithPath("company.postbox.postalCode").ignored(),
                                fieldWithPath("contact").ignored(),
                                fieldWithPath("application").ignored(),
                                fieldWithPath("_links").ignored()
                        )))
                .andDo(document("{method-name}-contact", testHelper.getPreprocessRequest(), testHelper.getPreprocessResponse(),
                        responseFields(
                                fieldWithPath("publicationStartDate").ignored(),
                                fieldWithPath("publicationEndDate").ignored(),
                                fieldWithPath("reference").ignored(),
                                fieldWithPath("url").ignored(),
                                fieldWithPath("job").ignored(),
                                fieldWithPath("company").ignored(),
                                fieldWithPath("contact").ignored(),
                                fieldWithPath("contact.title").ignored(),
                                fieldWithPath("contact.firstName").ignored(),
                                fieldWithPath("contact.lastName").ignored(),
                                fieldWithPath("contact.phoneNumber").ignored(),
                                fieldWithPath("contact.email").ignored(),
                                fieldWithPath("application").ignored(),
                                fieldWithPath("_links").ignored()
                        )))
                .andDo(document("{method-name}-application", testHelper.getPreprocessRequest(), testHelper.getPreprocessResponse(),
                        responseFields(
                                fieldWithPath("publicationStartDate").ignored(),
                                fieldWithPath("publicationEndDate").ignored(),
                                fieldWithPath("reference").ignored(),
                                fieldWithPath("url").ignored(),
                                fieldWithPath("job").ignored(),
                                fieldWithPath("company").ignored(),
                                fieldWithPath("contact").ignored(),
                                fieldWithPath("application").ignored(),
                                fieldWithPath("application.telephonic").ignored(),
                                fieldWithPath("application.written").ignored(),
                                fieldWithPath("application.electronic").ignored(),
                                fieldWithPath("application.additionalDetails").ignored(),
                                fieldWithPath("application.url").ignored(),
                                fieldWithPath("_links").ignored()
                        )));
    }

    @Test
    public void getAllJobOffers() throws Exception {

        this.mockMvc.perform(get(basePath + "/joboffers")
                .with(testHelper.getDefaultHttpBasic())
                .contextPath(basePath))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.jobOffers", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.page", Matchers.notNullValue()))

                .andDo(document("{method-name}", testHelper.getPreprocessRequest(), testHelper.getPreprocessResponse(),
                        responseFields(
                                fieldWithPath("_embedded.jobOffers").description("Contains all job offers associated to the current user."),
                                fieldWithPath("_links").ignored(),
                                fieldWithPath("page").
                                        description("Provides informations and links used to manage pagination (see chapter <<Paging>>).")
                        )));
    }
}

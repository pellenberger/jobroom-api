package ch.admin.seco.jobroom.doc;

import ch.admin.seco.jobroom.ApiApplication;
import ch.admin.seco.jobroom.helpers.ApiTestHelper;
import ch.admin.seco.jobroom.helpers.DatasetTestHelper;
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
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentation;
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
@SpringApplicationConfiguration(classes = ApiApplication.class)
@WebAppConfiguration
/**
 * This class generates doc snippets to be included in Asciidoc documentation
 */
public class ApiDocGet {

    private MockMvc mockMvc;

    private int idNewJobOffer;

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

        RestAccessKey restAccessKey = apiTestHelper.getDefaultRestAccessKey();
        restAccessKeyRepository.save(restAccessKey);

        apiTestHelper.authenticateDefault();

        JobOffer jobOffer = DatasetTestHelper.getCompleteJobOffer();
        jobOffer.setOwner(restAccessKey);
        idNewJobOffer = jobOfferRepository.save(jobOffer).getId();

        apiTestHelper.unAuthenticate();
    }

    @After
    public void cleanup() {
        apiTestHelper.authenticate("user", "password");
        jobOfferRepository.deleteAll();
        restAccessKeyRepository.deleteAll();
        apiTestHelper.unAuthenticate();
    }

    @Test
    public void getJobOffer() throws Exception {

        this.mockMvc.perform(get("/joboffers/" + idNewJobOffer).accept(MediaType.APPLICATION_JSON_UTF8).with(apiTestHelper.getDefaultHttpBasic()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.publicationStartDate", Matchers.is("2100-01-01")))
                .andExpect(jsonPath("$.publicationEndDate", Matchers.is("2101-02-02")))
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
                .andExpect(jsonPath("$.job.languageSkills[0].language", Matchers.is("de")))
                .andExpect(jsonPath("$.job.languageSkills[0].spokenLevel", Matchers.is("very_good")))
                .andExpect(jsonPath("$.job.languageSkills[0].writtenLevel", Matchers.is("good")))
                .andExpect(jsonPath("$.job.languageSkills[1].language", Matchers.is("fr")))
                .andExpect(jsonPath("$.job.languageSkills[1].spokenLevel", Matchers.is("average")))
                .andExpect(jsonPath("$.job.languageSkills[1].writtenLevel", Matchers.is("average")))
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
                .andExpect(jsonPath("$.application.telephonic", Matchers.is(0)))
                .andExpect(jsonPath("$.application.written", Matchers.is(1)))
                .andExpect(jsonPath("$.application.electronic", Matchers.is(1)))

                .andDo(document("{method-name}", apiTestHelper.getPreprocessRequest(), apiTestHelper.getPreprocessResponse(),
                        responseFields(
                                fieldWithPath("publicationStartDate").ignored(),
                                fieldWithPath("publicationEndDate").ignored(),
                                fieldWithPath("job").ignored(),
                                fieldWithPath("company").ignored(),
                                fieldWithPath("contact").ignored(),
                                fieldWithPath("application").ignored(),
                                fieldWithPath("_links").ignored()
                        )))
                .andDo(document("{method-name}-job", apiTestHelper.getPreprocessRequest(), apiTestHelper.getPreprocessResponse(),
                        responseFields(
                                fieldWithPath("publicationStartDate").ignored(),
                                fieldWithPath("publicationEndDate").ignored(),
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
                .andDo(document("{method-name}-company", apiTestHelper.getPreprocessRequest(), apiTestHelper.getPreprocessResponse(),
                        responseFields(
                                fieldWithPath("publicationStartDate").ignored(),
                                fieldWithPath("publicationEndDate").ignored(),
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
                .andDo(document("{method-name}-contact", apiTestHelper.getPreprocessRequest(), apiTestHelper.getPreprocessResponse(),
                        responseFields(
                                fieldWithPath("publicationStartDate").ignored(),
                                fieldWithPath("publicationEndDate").ignored(),
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
                .andDo(document("{method-name}-application", apiTestHelper.getPreprocessRequest(), apiTestHelper.getPreprocessResponse(),
                        responseFields(
                                fieldWithPath("publicationStartDate").ignored(),
                                fieldWithPath("publicationEndDate").ignored(),
                                fieldWithPath("job").ignored(),
                                fieldWithPath("company").ignored(),
                                fieldWithPath("contact").ignored(),
                                fieldWithPath("application").ignored(),
                                fieldWithPath("application.telephonic").ignored(),
                                fieldWithPath("application.written").ignored(),
                                fieldWithPath("application.electronic").ignored(),
                                fieldWithPath("_links").ignored()
                        )));
    }

    @Test
    public void getAllJobOffers() throws Exception {

        this.mockMvc.perform(get("/joboffers").with(apiTestHelper.getDefaultHttpBasic()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.jobOffers", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.page", Matchers.notNullValue()))

                .andDo(document("{method-name}", apiTestHelper.getPreprocessRequest(), apiTestHelper.getPreprocessResponse(),
                        responseFields(
                                fieldWithPath("_embedded.jobOffers").description("todo"),
                                fieldWithPath("_links").description("todo"),
                                fieldWithPath("page").description("todo")
                        )));
    }
}

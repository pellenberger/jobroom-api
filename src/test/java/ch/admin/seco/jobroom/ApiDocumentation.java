package ch.admin.seco.jobroom;

import ch.admin.seco.jobroom.helpers.ApiTestHelper;
import ch.admin.seco.jobroom.helpers.JobOfferTestHelper;
import ch.admin.seco.jobroom.web.JobOfferRepository;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.restdocs.RestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApiApplication.class)
@WebAppConfiguration
public class ApiDocumentation {

    private MockMvc mockMvc;

    private RestDocumentationResultHandler document;

    @Rule
    public final RestDocumentation restDocumentation = new RestDocumentation("build/generated-snippets");

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    JobOfferRepository jobOfferRepository;

    @Autowired
    ApiTestHelper apiTestHelper;

    OperationRequestPreprocessor getPreprocessRequest() {
        return preprocessRequest(prettyPrint());
    }

    OperationResponsePreprocessor getPreprocessResponse() {
        return preprocessResponse(prettyPrint());
    }

    org.springframework.test.web.servlet.request.RequestPostProcessor getHttpBasic() {
        return httpBasic("user", "password");
    }

    @Before
    public void setup() throws Exception {

        this.mockMvc = webAppContextSetup(webApplicationContext)
                .apply(
                        documentationConfiguration(this.restDocumentation).uris()
                                .withScheme("http")
                                .withHost("api.job-room.ch")
                                .withPort(80))
                .apply(springSecurity())
                .build();
    }

    @Test
    public void createJobOffer() throws Exception {

        // TODO add constraints in documentation (should be included using REST Docs mechanism : ConstraintDescriptions (JPA / SQL))
        // TODO only describe subobjects here (not GET request anymore) ??

        String jobOfferJson = JobOfferTestHelper.getCompleteJobOfferJson().toString();

        this.mockMvc.perform(post("/joboffers") // FIXME design the "versioning approach" (externally?)
                .with(getHttpBasic())
                .contentType(apiTestHelper.getContentType())
                .content(jobOfferJson))
                .andExpect(status().isCreated())

                .andDo(document("{method-name}", getPreprocessRequest(), getPreprocessResponse(),
                        requestFields(
                                fieldWithPath("publicationStartDate").description("todo"),
                                fieldWithPath("publicationEndDate").description("todo"),
                                fieldWithPath("job").description("Description of the job (further information bellow)"),
                                fieldWithPath("company").description("The company that offers the job (further information bellow)"),
                                fieldWithPath("contact").description("Contact person about the job offer (further information bellow)"),
                                fieldWithPath("application").description("Support for submitting the job application (further information bellow")
                        )))
                .andDo(document("{method-name}-job", getPreprocessRequest(), getPreprocessResponse(),
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
                .andDo(document("{method-name}-company", getPreprocessRequest(), getPreprocessResponse(),
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
                .andDo(document("{method-name}-contact", getPreprocessRequest(), getPreprocessResponse(),
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
                .andDo(document("{method-name}-application", getPreprocessRequest(), getPreprocessResponse(),
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
    }

    @Test
    public void getJobOffer() throws Exception {

        this.mockMvc.perform(get("/joboffers/1").with(getHttpBasic()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.publicationStartDate", Matchers.is("2000-01-01")))
                .andExpect(jsonPath("$.publicationEndDate", Matchers.is("2001-02-02")))
                .andExpect(jsonPath("$.job.title", Matchers.is("Software engineer")))
                .andExpect(jsonPath("$.job.description", Matchers.is("Development of eGov applications")))
                .andExpect(jsonPath("$.job.workingTimePercentageFrom", Matchers.is(80)))
                .andExpect(jsonPath("$.job.workingTimePercentageTo", Matchers.is(100)))
                .andExpect(jsonPath("$.job.startDate", Matchers.is("2000-03-01")))
                .andExpect(jsonPath("$.job.endDate", Matchers.is("2002-04-02")))
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
                .andExpect(jsonPath("$.contact.title", Matchers.is("mister")))
                // FIXME encoding issue
//                .andExpect(jsonPath("$.contact.firstName", Matchers.is("Alizée")))
                .andExpect(jsonPath("$.contact.lastName", Matchers.is("Dupont")))
                .andExpect(jsonPath("$.contact.phoneNumber", Matchers.is("0791234567")))
                .andExpect(jsonPath("$.contact.email", Matchers.is("jean.dupont@seco.admin.ch")))
                .andExpect(jsonPath("$.application.telephonic", Matchers.is(false)))
                .andExpect(jsonPath("$.application.written", Matchers.is(true)))
                .andExpect(jsonPath("$.application.electronic", Matchers.is(true)))

                .andDo(document("{method-name}", getPreprocessRequest(), getPreprocessResponse(),
                        responseFields(
                                fieldWithPath("publicationStartDate").description("todo"),
                                fieldWithPath("publicationEndDate").description("todo"),
                                fieldWithPath("job").description("Description of the job (further information bellow)"),
                                fieldWithPath("company").description("The company that offers the job (further information bellow)"),
                                fieldWithPath("contact").description("Contact person about the job offer (further information bellow)"),
                                fieldWithPath("application").description("Support for submitting the job application (further information bellow"),
                                fieldWithPath("_links").description("todo")
                        )))
                .andDo(document("{method-name}-job", getPreprocessRequest(), getPreprocessResponse(),
                        responseFields(
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
                                fieldWithPath("application").ignored(),
                                fieldWithPath("_links").ignored()
                        )))
                .andDo(document("{method-name}-company", getPreprocessRequest(), getPreprocessResponse(),
                        responseFields(
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
                                fieldWithPath("application").ignored(),
                                fieldWithPath("_links").ignored()
                        )))
                .andDo(document("{method-name}-contact", getPreprocessRequest(), getPreprocessResponse(),
                        responseFields(
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
                                fieldWithPath("application").ignored(),
                                fieldWithPath("_links").ignored()
                        )))
                .andDo(document("{method-name}-application", getPreprocessRequest(), getPreprocessResponse(),
                        responseFields(
                                fieldWithPath("publicationStartDate").ignored(),
                                fieldWithPath("publicationEndDate").ignored(),
                                fieldWithPath("job").ignored(),
                                fieldWithPath("company").ignored(),
                                fieldWithPath("contact").ignored(),
                                fieldWithPath("application").ignored(),
                                fieldWithPath("application.telephonic").description("todo"),
                                fieldWithPath("application.written").description("todo"),
                                fieldWithPath("application.electronic").description("todo"),
                                fieldWithPath("_links").ignored()
                        )));
    }

    @Test
    public void getAllJobOffers() throws Exception {

        this.mockMvc.perform(get("/joboffers").with(getHttpBasic()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.jobOffers", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.page", Matchers.notNullValue()))

                .andDo(document("{method-name}", getPreprocessRequest(), getPreprocessResponse(),
                        responseFields(
                                fieldWithPath("_embedded.jobOffers").description("todo"),
                                fieldWithPath("_links").description("todo"),
                                fieldWithPath("page").description("todo")
                        )));
    }

    @Ignore
    @Test
    public void editJobOffer() throws Exception {
        // TODO PUT /joboffers/1
    }

    @Ignore
    @Test
    public void deleteJobOffer() {
        // TODO DELETE /joboffers/1
    }

    @Test
    public void accessWithoutAuth() throws Exception {
        this.mockMvc.perform(get("/joboffers"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void accessWithBadCredentials() throws Exception {
        this.mockMvc.perform(get("/joboffers").with(httpBasic("wrong_username", "wrong_password")))
                .andExpect(status().isUnauthorized());
    }
}

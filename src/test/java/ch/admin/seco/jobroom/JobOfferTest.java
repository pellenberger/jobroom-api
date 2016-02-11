package ch.admin.seco.jobroom;

import ch.admin.seco.jobroom.web.JobOfferRepository;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApiApplication.class)
@WebAppConfiguration
public class JobOfferTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private RestDocumentationResultHandler document;

    @Rule
    public final RestDocumentation restDocumentation = new RestDocumentation("build/generated-snippets");

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    JobOfferRepository jobOfferRepository;

    @Autowired
    RestApiTestHelper testHelper;

    OperationRequestPreprocessor getPreprocessRequest() {
        return preprocessRequest(prettyPrint());
    }

    OperationResponsePreprocessor getPreprocessResponse() {
        return preprocessResponse(prettyPrint());
    }

    @Before
    public void setup() throws Exception {

        this.mockMvc = webAppContextSetup(webApplicationContext)
                .apply(
                        documentationConfiguration(this.restDocumentation).uris()
                                .withScheme("http")
                                .withHost("api.job-room.ch")
                                .withPort(80))
                .build();
    }

    @Test
    public void getJobOffer() throws Exception {

        this.mockMvc.perform(get("/joboffers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.publicationStartDate", Matchers.is("2000-01-01")))
                .andExpect(jsonPath("$.publicationEndDate", Matchers.is("2001-02-02")))
                // FIXME encoding issue
//                .andExpect(jsonPath("$.job.title", Matchers.is("Ingénieur")))
                .andExpect(jsonPath("$.job.description", Matchers.is("Software development")))
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
                .andExpect(jsonPath("$.contact.firstName", Matchers.is("Jean")))
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

    @Ignore
    @Test
    public void createJobOffer() throws Exception {

        // TODO constraints should be included using REST Docs mechanism : ConstraintDescriptions (JPA / SQL)

        this.document.snippets(requestFields(
                fieldWithPath("title").description("title desc")
                        .attributes(key("constraints").value("Must not be null. Must not be empty")),
                fieldWithPath("description").description("description desc")
                        .attributes(key("constraints").value("nothing")),
                fieldWithPath("startImmediate").description("startImmediate desc")
                        .attributes(key("constraints").value("nothing")),
                fieldWithPath("company.name").description("company name desc")
                        .attributes(key("constraints").value("nothing")),
                fieldWithPath("company.locality").description("company locality desc")
                        .attributes(key("constraints").value("nothing")),
                fieldWithPath("company").description("company subobject desc")
                        .attributes(key("constraints").value("TODO")),
                fieldWithPath("company.countryCode").description("company country code desc")
                        .attributes(key("constraints").value("nothing")),
                fieldWithPath("company.postalCode").description("company postal code desc")
                        .attributes(key("constraints").value("nothing")),
                fieldWithPath("languageSkill").description("languageSkill desc")
                        .attributes(key("constraints").value("Empty List is okay, but null is not allowed. TODO max = 5"))
        ));

        String jobOfferJson = "";


        this.mockMvc.perform(post("/joboffers") // FIXME design the "versioning approach" (externally?)
                //FIXME .header(JobPositionController.HEADER_ACCESS_KEY, "MySecretKey")
                .contentType(contentType)
                .content(jobOfferJson))
                .andExpect(status().isCreated());

        // TODO get the id back, and run the GET request
    }

    @Ignore
    @Test
    public void modifyExistingJobOffer() throws Exception {
        // TODO PUT request
    }
}

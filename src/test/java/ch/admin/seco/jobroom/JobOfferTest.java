package ch.admin.seco.jobroom;

import ch.admin.seco.jobroom.model.Company;
import ch.admin.seco.jobroom.model.JobOffer;
import ch.admin.seco.jobroom.model.LanguageSkill;
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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

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

    @Before
    public void setup() throws Exception {
        this.document = document("{method-name}",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()));

        this.mockMvc = webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(this.restDocumentation))
                .alwaysDo(this.document)
                .build();
    }

    @Test
    public void createJobOffer() throws Exception {
        List<LanguageSkill> languages = Arrays.asList(
                new LanguageSkill("fr", LanguageSkill.Level.very_good, LanguageSkill.Level.very_good));
        JobOffer jobOffer = new JobOffer(
                null,
                null,
                "Software engineer",
                "A few more words...",
                true,
                new Company(
                        "SwissPost",
                        "CH",
                        "Bahnofstrasse",
                        "11",
                        "Bern",
                        "3003",
                        null, null, null
                ),
                languages
        );
        String jobOfferJson = testHelper.json(jobOffer);

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

    @Ignore
    @Test
    public void rejectInvalidJobOffer() throws Exception {
        // TODO create a json string without any JobOffer object

        JobOffer jobOffer = new JobOffer(
                null,
                null,
                "Software engineer",
                "A few more words...",
//                "Chut!",
//                "Bernnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn",
//                "30030000",
                true,
                new Company("", "", "", "", "", "", null, null, null),
                Arrays.asList()
                );
        String jobOfferJson = testHelper.json(jobOffer);

        this.mockMvc.perform(post("/joboffers")
                // FIXME: return unauthorized on invalid access key...
                // FIXME .header(JobPositionController.HEADER_ACCESS_KEY, "MySecretKey")
                .contentType(contentType)
                .content(jobOfferJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getJobOffer() throws Exception {

        this.document.snippets(responseFields(
                fieldWithPath("title").description("title desc"),
                fieldWithPath("description").description("description desc"),
                fieldWithPath("startImmediate").description("startImmediate desc"),
                fieldWithPath("company").description("company desc, TODO subfields"),
                fieldWithPath("languageSkill").description("languageSkill desc"),
                fieldWithPath("_links").description("_links hateoas desc")
                //fieldWithPath("jobPosition").description("wtf spring-data-rest/hateoas?")
        ));

        this.mockMvc.perform(get("/joboffers/1"))
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$.id", Matchers.is(12345)))
                .andExpect(jsonPath("$.title", Matchers.is("John Doe")))
                // FIXME: char encoding problem
                //.andExpect(jsonPath("$.description", Matchers.containsString("hé!")))
                .andExpect(jsonPath("$.description", Matchers.containsString("hey!\n")))
                .andExpect(jsonPath("$.startImmediate", Matchers.is(false)))

                .andExpect(jsonPath("$.company.countryCode", Matchers.is("CA")))
                .andExpect(jsonPath("$.company.locality", Matchers.is("Montreal Nord")))
                .andExpect(jsonPath("$.company.postalCode", Matchers.is("QC H1G 2V1")))
                .andExpect(jsonPath("$.company.street", Matchers.is("Hockey Avenue")))
                .andExpect(jsonPath("$.company.houseNumber", Matchers.is("101")))

                .andExpect(jsonPath("$.languageSkill", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.languageSkill[0].language", Matchers.is("fr")))
                .andExpect(jsonPath("$.languageSkill[0].spokenLevel", Matchers.is("good")))
                .andExpect(jsonPath("$.languageSkill[0].writtenLevel", Matchers.is("average")))
                .andExpect(jsonPath("$.languageSkill[1].language", Matchers.is("en")))
                .andExpect(jsonPath("$.languageSkill[1].spokenLevel", Matchers.is("very_good")))
                .andExpect(jsonPath("$.languageSkill[1].writtenLevel", Matchers.is("very_good")))
        ;
    }

}

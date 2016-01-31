package ch.admin.seco.jobroom;

import ch.admin.seco.jobroom.model.JobPosition;
import ch.admin.seco.jobroom.model.LanguageSkill;
import ch.admin.seco.jobroom.web.JobPositionRepository;
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
public class JobPositionControllerTest {

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
    JobPositionRepository jobPositionRepository;

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
    public void createJobPosition() throws Exception {
        // FIXME: this input is actually invalid! The javax.validation.constraints are not effective at the moment!
        List<LanguageSkill> languages = Arrays.asList(
                new LanguageSkill("fr", LanguageSkill.Level.very_good, LanguageSkill.Level.very_good));
        JobPosition job = new JobPosition(
                null,
                "Software engineer",
                "A few more words...",
                "CH",
                "Bern",
                "3003",
                true,
                languages
        );
        String jobPositionJson = testHelper.json(job);

        this.document.snippets(requestFields(
                fieldWithPath("title").description("title desc")
                        .attributes(key("constraints").value("Must not be null. Must not be empty")),
                fieldWithPath("city").description("city desc")
                        .attributes(key("constraints").value("nothing")),
                fieldWithPath("description").description("description desc")
                        .attributes(key("constraints").value("nothing")),
                fieldWithPath("countryCode").description("countryCode desc")
                        .attributes(key("constraints").value("nothing")),
                fieldWithPath("zip").description("zip desc")
                        .attributes(key("constraints").value("nothing")),
                fieldWithPath("startImmediate").description("startImmediate desc")
                        .attributes(key("constraints").value("nothing")),
                fieldWithPath("languageSkill").description("languageSkill desc")
                        .attributes(key("constraints").value("Empty List is okay, but null is not allowed. TODO max = 5"))
        ));

        this.mockMvc.perform(post("/job") // FIXME design the "versioning approach" (externally?)
                //FIXME .header(JobPositionController.HEADER_ACCESS_KEY, "MySecretKey")
                .contentType(contentType)
                .content(jobPositionJson))
                .andExpect(status().isCreated());

        // TODO get the id back, and run the GET request
    }

    @Ignore
    @Test
    public void rejectInvalidJobPosition() throws Exception {
        // TODO create a json string without any JobPosition object

        JobPosition job = new JobPosition(
                null,
                "Software engineer",
                "A few more words...",
                "Chut!",
                "Bernnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn",
                "30030000",
                true,
                Arrays.asList()
                );
        String jobPositionJson = testHelper.json(job);

        this.mockMvc.perform(post("/job")
                // FIXME: return unauthorized on invalid access key...
                // FIXME .header(JobPositionController.HEADER_ACCESS_KEY, "MySecretKey")
                .contentType(contentType)
                .content(jobPositionJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getJobPosition() throws Exception {

        this.document.snippets(responseFields(
                fieldWithPath("title").description("title desc"),
                fieldWithPath("city").description("city desc"),
                fieldWithPath("description").description("description desc"),
                fieldWithPath("countryCode").description("countryCode desc"),
                fieldWithPath("zip").description("zip desc"),
                fieldWithPath("startImmediate").description("startImmediate desc"),
                fieldWithPath("languageSkill").description("languageSkill desc"),
                fieldWithPath("_links").description("_links hateoas desc")
                //fieldWithPath("jobPosition").description("wtf spring-data-rest/hateoas?")
        ));

        this.mockMvc.perform(get("/job/1"))
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$.id", Matchers.is(12345)))
                .andExpect(jsonPath("$.title", Matchers.is("John Doe")))
                // FIXME: char encoding problem
                //.andExpect(jsonPath("$.description", Matchers.containsString("hé!")))
                .andExpect(jsonPath("$.description", Matchers.containsString("hey!\n")))
                .andExpect(jsonPath("$.city", Matchers.is("Montreal Nord")))
                .andExpect(jsonPath("$.zip", Matchers.is("QC H1G 2V1")))
                .andExpect(jsonPath("$.startImmediate", Matchers.is(false)))

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

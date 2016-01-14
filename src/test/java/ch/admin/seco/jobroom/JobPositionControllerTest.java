package ch.admin.seco.jobroom;

import ch.admin.seco.jobroom.dto.JobPosition;
import org.hamcrest.Matchers;
import org.junit.Before;
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

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
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
        JobPosition job = new JobPosition();
        job.setTitle("Software engineer");
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
                        .attributes(key("constraints").value("nothing"))
        ));

        this.mockMvc.perform(post("/job")
                .contentType(contentType)
                .content(jobPositionJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void getJobPosition() throws Exception {

        this.document.snippets(responseFields(
                fieldWithPath("title").description("title desc"),
                fieldWithPath("city").description("city desc"),
                fieldWithPath("description").description("description desc"),
                fieldWithPath("countryCode").description("countryCode desc"),
                fieldWithPath("zip").description("zip desc")
        ));

        this.mockMvc.perform(get("/job"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", Matchers.is("Software engineer")))
                .andExpect(jsonPath("$.city", Matchers.is("Berne")));
    }

}

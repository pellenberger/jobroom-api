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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
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

    @Rule
    public final RestDocumentation restDocumentation = new RestDocumentation("build/generated-snippets");

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    RestApiTestHelper testHelper;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(this.restDocumentation))
                .build();
    }

    @Test
    public void createJobPosition() throws Exception {
        String jobPositionJson = testHelper.json(new JobPosition());
        this.mockMvc.perform(post("/job")
                .contentType(contentType)
                .content(jobPositionJson))
                .andExpect(status().isCreated())
                .andDo(document("post-job"));
    }

    @Test
    public void getJobPosition() throws Exception {
        this.mockMvc.perform(get("/job"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", Matchers.is("Software engineer")))
                .andExpect(jsonPath("$.city", Matchers.is("Berne")))
                .andDo(document("get-job"));
    }

}

package ch.admin.seco.jobroom;

import ch.admin.seco.jobroom.helpers.ApiTestHelper;
import ch.admin.seco.jobroom.helpers.JobOfferDatasetHelper;
import ch.admin.seco.jobroom.model.RestAccessKey;
import ch.admin.seco.jobroom.repository.JobOfferRepository;
import ch.admin.seco.jobroom.repository.RestAccessKeyRepository;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Be sure that a value cannot be assigned to a field marked as @JsonIgnore
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@WebAppConfiguration
public class JsonIgnoredFieldsTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    ApiTestHelper apiTestHelper;

    @Autowired
    RestAccessKeyRepository restAccessKeyRepository;

    @Autowired
    JobOfferRepository jobOfferRepository;

    @Before
    public void setup() throws Exception {

        this.mockMvc = webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();

        RestAccessKey restAccessKey = apiTestHelper.getDefaultRestAccessKey();
        restAccessKeyRepository.save(restAccessKey);
    }

    @After
    public void cleanup() {
        apiTestHelper.authenticateDefault();
        jobOfferRepository.deleteAll();
        apiTestHelper.unAuthenticate();
        restAccessKeyRepository.deleteAll();
    }

    private void performTest(String json) throws Exception {
        this.mockMvc.perform(post("/joboffers")
                .with(apiTestHelper.getDefaultHttpBasic())
                .contentType(apiTestHelper.getContentType())
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void jsonIgnoredVersion() throws Exception {

        JSONObject json = JobOfferDatasetHelper.getMutableJson();
        json.put("version", 10);

        performTest(json.toString());
    }

    @Test
    public void jsonIgnoredOwner() throws Exception {

        JSONObject json = JobOfferDatasetHelper.getMutableJson();
        json.put("owner", new JSONObject().put("accessKey", "password123").put("ownerName", "Bob").put("active", 1));

        performTest(json.toString());
    }

    @Test
    public void jsonIgnoredCreationDate() throws Exception {

        JSONObject json = JobOfferDatasetHelper.getMutableJson();
        json.put("creationDate", "2016-01-01");

        performTest(json.toString());
    }

    @Test
    public void jsonIgnoredLastModificationDate() throws Exception {

        JSONObject json = JobOfferDatasetHelper.getMutableJson();
        json.put("lastModificationDate", "2016-01-01");

        performTest(json.toString());
    }

    @Test
    public void jsonIgnoredCancellationDate() throws Exception {

        JSONObject json = JobOfferDatasetHelper.getMutableJson();
        json.put("cancellationDate", "2016-01-01");

        performTest(json.toString());
    }

    @Test
    public void jsonIgnoredCancellationReasonCode() throws Exception {

        JSONObject json = JobOfferDatasetHelper.getMutableJson();
        json.put("cancellationReasonCode", "2016-01-01");

        performTest(json.toString());
    }
}

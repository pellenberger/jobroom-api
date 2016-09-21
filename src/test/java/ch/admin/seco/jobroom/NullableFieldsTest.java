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
 * First part :
 *      Be sure that a mandatory property cannot be set to NULL through the API without raising an error (ALV #5181)
 *      All data types have to be tested
 *
 * Second part :
 *      Be sure that all nullable fields can be set to NULL through the API
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@WebAppConfiguration
public class NullableFieldsTest {

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

    /**
     * Create JobOffer without "job" (data type : embedded Object)
     */
    @Test
    public void missingObject() throws Exception {

        JSONObject json = JobOfferDatasetHelper.getMutableJson();
        json.remove("job");

        performTest(json.toString());
    }

    /**
     * Create JobOffer without "publicationStartDate" (data type : LocalDate)
     */
    @Test
    public void missingLocalDate() throws Exception {

        JSONObject json = JobOfferDatasetHelper.getMutableJson();
        json.remove("publicationStartDate");

        performTest(json.toString());
    }

    /**
     * Create JobOffer without "contact.title" (data type : EnumType)
     */
    @Test
    public void missingEnumType() throws Exception {

        JSONObject json = JobOfferDatasetHelper.getMutableJson();
        json.getJSONObject("contact").remove("title");

        performTest(json.toString());
    }

    /**
     * Create JobOffer without "job.title" (data type : String)
     */
    @Test
    public void missingString() throws Exception {

        JSONObject json = JobOfferDatasetHelper.getMutableJson();
        json.getJSONObject("job").remove("title");

        performTest(json.toString());
    }

    /**
     * Create JobOffer without "job.workingTimePercentageFrom" (data type : Int)
     */
    @Test
    public void missingInt() throws Exception {

        JSONObject json = JobOfferDatasetHelper.getMutableJson();
        json.getJSONObject("job").remove("workingTimePercentageFrom");

        performTest(json.toString());
    }

    /**
     * Create JobOffer without "application.telephonic" (data type : Boolean)
     */
    @Test
    public void missingBoolean() throws Exception {

        JSONObject json = JobOfferDatasetHelper.getMutableJson();
        json.getJSONObject("application").remove("telephonic");

        performTest(json.toString());
    }

    /**
     * Be sure that all nullable fields can be set to NULL through the API
     */
    @Test
    public void nullableFields() throws Exception {

        JSONObject json = JobOfferDatasetHelper.getMutableJson();

        json.remove("publicationEndDate");
        json.remove("reference");
        json.remove("url");
        json.getJSONObject("job").remove("startDate");
        json.getJSONObject("job").remove("endDate");
        json.getJSONObject("job").remove("languageSkills");
        json.getJSONObject("job").getJSONObject("location").remove("locality");
        json.getJSONObject("job").getJSONObject("location").remove("postalCode");
        json.getJSONObject("job").getJSONObject("location").remove("additionalDetails");
        json.getJSONObject("company").remove("phoneNumber");
        json.getJSONObject("company").remove("email");
        json.getJSONObject("company").remove("website");
        json.getJSONObject("company").remove("postbox");
        json.getJSONObject("application").remove("additionalDetails");
        json.getJSONObject("application").remove("url");

        this.mockMvc.perform(post("/joboffers")
                .with(apiTestHelper.getDefaultHttpBasic())
                .contentType(apiTestHelper.getContentType())
                .content(json.toString()))
                .andExpect(status().isCreated());
    }
}

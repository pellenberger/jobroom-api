package ch.admin.seco.jobroom;

import ch.admin.seco.jobroom.helpers.DatasetHelper;
import ch.admin.seco.jobroom.helpers.TestHelper;
import ch.admin.seco.jobroom.repository.JobOfferRepository;
import ch.admin.seco.jobroom.repository.RestAccessKeyRepository;
import org.json.JSONObject;
import org.junit.*;
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
 * Test constraints that are defined on SQL level
 * IMPORTANT : Tests have been validated with a local Oracle database but are now ignored to not fail the CI build process
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@WebAppConfiguration
public class SqlConstraintsTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    TestHelper testHelper;

    @Autowired
    RestAccessKeyRepository restAccessKeyRepository;

    @Autowired
    JobOfferRepository jobOfferRepository;

    @Before
    public void setup() throws Exception {

        this.mockMvc = webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();

        restAccessKeyRepository.save(testHelper.getDefaultRestAccessKey());
    }

    @After
    public void cleanup() {
        testHelper.authenticateDefault();
        jobOfferRepository.deleteAll();
        testHelper.unAuthenticate();
        restAccessKeyRepository.deleteAll();
    }

    @Test
    public void invalidLanguageSkill() throws Exception {

        this.mockMvc.perform(post("/joboffers").with(testHelper.getDefaultHttpBasic())
                .with(testHelper.getDefaultHttpBasic())
                .contentType(testHelper.getContentType())
                .content(DatasetHelper.getJsonWithLanguageSkills("fr", "good", "good").toString()))
                .andExpect(status().isBadRequest());

        this.mockMvc.perform(post("/joboffers").with(testHelper.getDefaultHttpBasic())
                .with(testHelper.getDefaultHttpBasic())
                .contentType(testHelper.getContentType())
                .content(DatasetHelper.getJsonWithLanguageSkills("41", "good", "good").toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void invalidJobStartEndDate() throws Exception {

        String jobStartDate = "2300-01-15";
        String jobEndDate = "2300-01-10";

        JSONObject jobOffer = DatasetHelper.getJson();
        jobOffer.getJSONObject("job")
                .put("startDate", jobStartDate)
                .put("endDate", jobEndDate);

        this.mockMvc.perform(post("/joboffers").with(testHelper.getDefaultHttpBasic())
                .with(testHelper.getDefaultHttpBasic())
                .contentType(testHelper.getContentType())
                .content(jobOffer.toString()))
                .andExpect(status().isConflict());
    }

    @Test
    public void invalidWorkingTimePercentage() throws Exception {

        // workingTimePercentageFrom = 0
        this.mockMvc.perform(post("/joboffers").with(testHelper.getDefaultHttpBasic())
                .with(testHelper.getDefaultHttpBasic())
                .contentType(testHelper.getContentType())
                .content(getJsonWithWorkingTimePercentage(0, 50).toString()))
                .andExpect(status().isConflict());

        // workingTimePercentageTo = 101
        this.mockMvc.perform(post("/joboffers").with(testHelper.getDefaultHttpBasic())
                .with(testHelper.getDefaultHttpBasic())
                .contentType(testHelper.getContentType())
                .content(getJsonWithWorkingTimePercentage(50, 101).toString()))
                .andExpect(status().isConflict());

        // workingTimePercentageFrom > workingTimePercentageTo
        this.mockMvc.perform(post("/joboffers").with(testHelper.getDefaultHttpBasic())
                .with(testHelper.getDefaultHttpBasic())
                .contentType(testHelper.getContentType())
                .content(getJsonWithWorkingTimePercentage(80, 20).toString()))
                .andExpect(status().isConflict());
    }

    @Test
    public void invalidPublicationStartEndDate() throws Exception {

        // publicationStartDate > publicationEndDate
        this.mockMvc.perform(post("/joboffers").with(testHelper.getDefaultHttpBasic())
                .with(testHelper.getDefaultHttpBasic())
                .contentType(testHelper.getContentType())
                .content(DatasetHelper.getJson()
                        .put("publicationStartDate", "2200-04-25")
                        .put("publicationEndDate", "2200-04-20").toString()))
                .andExpect(status().isConflict());

    }

    @Test
    public void lengthExceeded() throws Exception {

        String longAdditionalDetails = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                "Etiam vitae finibus nunc. Nulla tristique molestie massa eget molestie. Cras luctus vehicula " +
                "ex. Integer tristique tempor facilisis. Mauris lacus mi, facilisis in ex tristique, " +
                "tristique turpis duis.";

        Assert.assertEquals(256, longAdditionalDetails.length());

        JSONObject jobOffer = DatasetHelper.getJson();
        jobOffer.getJSONObject("application")
                        .put("telephonic", false)
                        .put("written", false)
                        .put("electronic", false)
                        .put("additionalDetails", longAdditionalDetails);

        this.mockMvc.perform(post("/joboffers").with(testHelper.getDefaultHttpBasic())
                .with(testHelper.getDefaultHttpBasic())
                .contentType(testHelper.getContentType())
                .content(jobOffer.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void invalidCountryCode() throws Exception {

        String invalidCountryCode = "XX";

        // test on job location
        this.mockMvc.perform(post("/joboffers").with(testHelper.getDefaultHttpBasic())
                .with(testHelper.getDefaultHttpBasic())
                .contentType(testHelper.getContentType())
                .content(getJsonWithJobLocation(invalidCountryCode, "Bern", "3010").toString()))
                .andExpect(status().isBadRequest());

        // test on company
        this.mockMvc.perform(post("/joboffers").with(testHelper.getDefaultHttpBasic())
                .with(testHelper.getDefaultHttpBasic())
                .contentType(testHelper.getContentType())
                .content(getJsonWithCompanyLocation(invalidCountryCode, "3010").toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void invalidPostalCode() throws Exception {

        // warning : locations used in tests have to be located in Switzerland (countryCode = CH)

        String invalidPostalCode = "1111";
        String nonNumericPostalCode = "ABC123";

        // test on job location
        this.mockMvc.perform(post("/joboffers").with(testHelper.getDefaultHttpBasic())
                .with(testHelper.getDefaultHttpBasic())
                .contentType(testHelper.getContentType())
                .content(getJsonWithJobLocation("CH", "Bern", invalidPostalCode).toString()))
                .andExpect(status().isBadRequest());

        // test on company
        this.mockMvc.perform(post("/joboffers").with(testHelper.getDefaultHttpBasic())
                .with(testHelper.getDefaultHttpBasic())
                .contentType(testHelper.getContentType())
                .content(getJsonWithCompanyLocation("CH", invalidPostalCode).toString()))
                .andExpect(status().isBadRequest());

        // test on company postbox
        JSONObject jobOffer = DatasetHelper.getJson();
        jobOffer.getJSONObject("company")
                .put("postbox", new JSONObject()
                        .put("number", "12")
                        .put("locality", "Bern")
                        .put("postalCode", invalidPostalCode));

        this.mockMvc.perform(post("/joboffers").with(testHelper.getDefaultHttpBasic())
                .with(testHelper.getDefaultHttpBasic())
                .contentType(testHelper.getContentType())
                .content(jobOffer.toString()))
                .andExpect(status().isBadRequest());

        // job postal code can contain non-numeric values in other countries
        this.mockMvc.perform(post("/joboffers").with(testHelper.getDefaultHttpBasic())
                .with(testHelper.getDefaultHttpBasic())
                .contentType(testHelper.getContentType())
                .content(getJsonWithJobLocation("FR", "Nice", nonNumericPostalCode).toString()))
                .andExpect(status().isCreated());

        // company postal code can contain non-numeric values in other countries
        this.mockMvc.perform(post("/joboffers").with(testHelper.getDefaultHttpBasic())
                .with(testHelper.getDefaultHttpBasic())
                .contentType(testHelper.getContentType())
                .content(getJsonWithCompanyLocation("FR", nonNumericPostalCode).toString()))
                .andExpect(status().isCreated());

        // job without locality nor postal code
        jobOffer = DatasetHelper.getJson();
        jobOffer.getJSONObject("job")
                .put("location", new JSONObject()
                        .put("countryCode", "CH"));

        this.mockMvc.perform(post("/joboffers").with(testHelper.getDefaultHttpBasic())
                .with(testHelper.getDefaultHttpBasic())
                .contentType(testHelper.getContentType())
                .content(jobOffer.toString()))
                .andExpect(status().isCreated());
    }

    private JSONObject getJsonWithCompanyLocation(String countryCode, String postalCode) {

        JSONObject jobOffer = DatasetHelper.getJson();

        jobOffer.getJSONObject("company")
                .put("countryCode", countryCode)
                .put("postalCode", postalCode);

        return jobOffer;
    }

    private JSONObject getJsonWithJobLocation(String countryCode, String locality, String postalCode) {

        JSONObject jobOffer = DatasetHelper.getJson();

        jobOffer.getJSONObject("job").getJSONObject("location")
                .put("countryCode", countryCode)
                .put("locality", locality)
                .put("postalCode", postalCode);

        return jobOffer;
    }

    private JSONObject getJsonWithWorkingTimePercentage(int from, int to) {

        JSONObject jobOffer = DatasetHelper.getJson();

        jobOffer.getJSONObject("job")
                .put("workingTimePercentageFrom", from)
                .put("workingTimePercentageTo", to);

        return jobOffer;
    }
}

package ch.admin.seco.jobroom;

import ch.admin.seco.jobroom.helpers.ApiTestHelper;
import ch.admin.seco.jobroom.helpers.JobOfferDatasetHelper;
import ch.admin.seco.jobroom.repository.JobOfferRepository;
import ch.admin.seco.jobroom.repository.RestAccessKeyRepository;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import javax.json.JsonObject;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Test constraints that are defined on SQL level *
 * IMPORTANT : Tests have been validated with a local Oracle database but are now ignored to not fail the CI build process
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApiApplication.class)
@WebAppConfiguration
public class SqlConstraintsTest {

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

        restAccessKeyRepository.save(apiTestHelper.getDefaultRestAccessKey());
    }

    @After
    public void cleanup() {
        apiTestHelper.authenticateDefault();
        jobOfferRepository.deleteAll();
        apiTestHelper.unAuthenticate();
        restAccessKeyRepository.deleteAll();
    }

    @Ignore
    @Test
    public void invalidLanguageSkill() throws Exception {

        //FIXME test fails

        //TODO make the validation trigger raise an error that produces the same behaviour as other SQL constraints

        this.mockMvc.perform(post("/joboffers").with(apiTestHelper.getDefaultHttpBasic())
                .with(apiTestHelper.getDefaultHttpBasic())
                .contentType(apiTestHelper.getContentType())
                .content(JobOfferDatasetHelper.getJsonInvalidLanguageSkill().toString()))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void invalidApplication() throws Exception {

        JsonObject invalidTelephonic = JobOfferDatasetHelper.getJsonWithApplication(2, 0, 0);
        JsonObject invalidWritten = JobOfferDatasetHelper.getJsonWithApplication(0, 2, 0);
        JsonObject invalidElectronic = JobOfferDatasetHelper.getJsonWithApplication(0, 0, 2);

        this.mockMvc.perform(post("/joboffers").with(apiTestHelper.getDefaultHttpBasic())
                .with(apiTestHelper.getDefaultHttpBasic())
                .contentType(apiTestHelper.getContentType())
                .content(invalidTelephonic.toString()))
                .andExpect(status().isConflict());

        this.mockMvc.perform(post("/joboffers").with(apiTestHelper.getDefaultHttpBasic())
                .with(apiTestHelper.getDefaultHttpBasic())
                .contentType(apiTestHelper.getContentType())
                .content(invalidWritten.toString()))
                .andExpect(status().isConflict());

        this.mockMvc.perform(post("/joboffers").with(apiTestHelper.getDefaultHttpBasic())
                .with(apiTestHelper.getDefaultHttpBasic())
                .contentType(apiTestHelper.getContentType())
                .content(invalidElectronic.toString()))
                .andExpect(status().isConflict());
    }

    @Test
    public void invalidJobStartEndDate() throws Exception {

        String jobStartDate = "2300-01-15";
        String jobEndDate = "2300-01-10";

        this.mockMvc.perform(post("/joboffers").with(apiTestHelper.getDefaultHttpBasic())
                .with(apiTestHelper.getDefaultHttpBasic())
                .contentType(apiTestHelper.getContentType())
                .content(JobOfferDatasetHelper.getJsonWithJobStartEndDate(jobStartDate, jobEndDate).toString()))
                .andExpect(status().isConflict());
    }

    @Test
    public void invalidWorkingTimePercentage() throws Exception {

        // workingTimePercentageFrom = 0
        this.mockMvc.perform(post("/joboffers").with(apiTestHelper.getDefaultHttpBasic())
                .with(apiTestHelper.getDefaultHttpBasic())
                .contentType(apiTestHelper.getContentType())
                .content(JobOfferDatasetHelper.getJsonWithWorkingTimePercentage(0, 50).toString()))
                .andExpect(status().isConflict());

        // workingTimePercentageTo = 101
        this.mockMvc.perform(post("/joboffers").with(apiTestHelper.getDefaultHttpBasic())
                .with(apiTestHelper.getDefaultHttpBasic())
                .contentType(apiTestHelper.getContentType())
                .content(JobOfferDatasetHelper.getJsonWithWorkingTimePercentage(50, 101).toString()))
                .andExpect(status().isConflict());

        // workingTimePercentageFrom > workingTimePercentageTo
        this.mockMvc.perform(post("/joboffers").with(apiTestHelper.getDefaultHttpBasic())
                .with(apiTestHelper.getDefaultHttpBasic())
                .contentType(apiTestHelper.getContentType())
                .content(JobOfferDatasetHelper.getJsonWithWorkingTimePercentage(80, 20).toString()))
                .andExpect(status().isConflict());
    }

    @Test
    public void invalidPublicationStartEndDate() throws Exception {

        // publicationStartDate > publicationEndDate
        this.mockMvc.perform(post("/joboffers").with(apiTestHelper.getDefaultHttpBasic())
                .with(apiTestHelper.getDefaultHttpBasic())
                .contentType(apiTestHelper.getContentType())
                .content(JobOfferDatasetHelper.getJsonWithPublicationStartEndDate("2200-04-25", "2200-04-20").toString()))
                .andExpect(status().isConflict());

    }
}
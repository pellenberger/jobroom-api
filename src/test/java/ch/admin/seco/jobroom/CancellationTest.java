package ch.admin.seco.jobroom;

import ch.admin.seco.jobroom.helpers.TestHelper;
import ch.admin.seco.jobroom.helpers.DatasetHelper;
import ch.admin.seco.jobroom.model.JobOffer;
import ch.admin.seco.jobroom.model.RestAccessKey;
import ch.admin.seco.jobroom.repository.JobOfferRepository;
import ch.admin.seco.jobroom.repository.RestAccessKeyRepository;
import org.hamcrest.Matchers;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@WebAppConfiguration
public class CancellationTest {

    private MockMvc mockMvc;

    private int idJob1;
    private int idJob2;

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

        RestAccessKey restAccessKey = testHelper.getDefaultRestAccessKey();
        restAccessKeyRepository.save(restAccessKey);
        testHelper.authenticateDefault();

        // create 2 jobs
        JobOffer job1 = DatasetHelper.get();
        job1.setOwner(restAccessKey);
        idJob1 = jobOfferRepository.save(job1).getId();

        JobOffer job2 = DatasetHelper.get();
        job2.setOwner(restAccessKey);
        idJob2 = jobOfferRepository.save(job2).getId();

        testHelper.unAuthenticate();
    }

    @After
    public void cleanup() {
        testHelper.authenticateDefault();
        jobOfferRepository.deleteAll();
        testHelper.unAuthenticate();
        restAccessKeyRepository.deleteAll();
    }

    @Test
    public void cancellation() throws Exception {

        // GET /joboffers should 2 elements

        this.mockMvc.perform(get("/joboffers/")
                .with(testHelper.getDefaultHttpBasic())
                .contentType(testHelper.getContentType()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.jobOffers", Matchers.hasSize(2)));

        // POST /cancel without reasonCode

        this.mockMvc.perform(post("/joboffers/" + idJob1 + "/cancel")
                .with(testHelper.getDefaultHttpBasic())
                .contentType(testHelper.getContentType()))
                .andExpect(status().isBadRequest());

        // POST /cancel

        this.mockMvc.perform(post("/joboffers/" + idJob1 + "/cancel")
                .with(testHelper.getDefaultHttpBasic())
                .param("reasonCode", "1")
                .contentType(testHelper.getContentType()))
                .andExpect(status().isNoContent());

        // POST /cancel on already-cancelled-job

        this.mockMvc.perform(post("/joboffers/" + idJob1 + "/cancel")
                .with(testHelper.getDefaultHttpBasic())
                .param("reasonCode", "1")
                .contentType(testHelper.getContentType()))
                .andExpect(status().isNotFound());

        // GET /joboffers should now return only 1 element

        this.mockMvc.perform(get("/joboffers/")
                .with(testHelper.getDefaultHttpBasic())
                .contentType(testHelper.getContentType()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.jobOffers", Matchers.hasSize(1)));

    }

    @Test
    public void deleteNotAllowed() throws Exception {
        this.mockMvc.perform(delete("/joboffers/" + idJob2)
                .with(testHelper.getDefaultHttpBasic())
                .contentType(testHelper.getContentType()))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void authorizedCancellationReasonCode() throws Exception {
        this.mockMvc.perform(post("/joboffers/" + idJob2 + "/cancel")
                .with(testHelper.getDefaultHttpBasic())
                .param("reasonCode", "4")
                .contentType(testHelper.getContentType()))
                .andExpect(status().isBadRequest());

    }

}

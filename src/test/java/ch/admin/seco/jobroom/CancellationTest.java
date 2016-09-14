package ch.admin.seco.jobroom;

import ch.admin.seco.jobroom.helpers.ApiTestHelper;
import ch.admin.seco.jobroom.helpers.JobOfferDatasetHelper;
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
        apiTestHelper.authenticateDefault();

        // create 2 jobs
        JobOffer job1 = JobOfferDatasetHelper.get();
        job1.setOwner(restAccessKey);
        idJob1 = jobOfferRepository.save(job1).getId();

        JobOffer job2 = JobOfferDatasetHelper.get();
        job2.setOwner(restAccessKey);
        idJob2 = jobOfferRepository.save(job2).getId();

        apiTestHelper.unAuthenticate();
    }

    @After
    public void cleanup() {
        apiTestHelper.authenticateDefault();
        jobOfferRepository.deleteAll();
        apiTestHelper.unAuthenticate();
        restAccessKeyRepository.deleteAll();
    }

    @Test
    public void cancellation() throws Exception {

        // GET /joboffers should 2 elements

        this.mockMvc.perform(get("/joboffers/")
                .with(apiTestHelper.getDefaultHttpBasic())
                .contentType(apiTestHelper.getContentType()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.jobOffers", Matchers.hasSize(2)));

        // POST /cancel without reasonCode

        this.mockMvc.perform(post("/joboffers/" + idJob1 + "/cancel")
                .with(apiTestHelper.getDefaultHttpBasic())
                .contentType(apiTestHelper.getContentType()))
                .andExpect(status().isBadRequest());

        // POST /cancel

        this.mockMvc.perform(post("/joboffers/" + idJob1 + "/cancel")
                .with(apiTestHelper.getDefaultHttpBasic())
                .param("reasonCode", "1")
                .contentType(apiTestHelper.getContentType()))
                .andExpect(status().isNoContent());

        // POST /cancel on already-cancelled-job

        this.mockMvc.perform(post("/joboffers/" + idJob1 + "/cancel")
                .with(apiTestHelper.getDefaultHttpBasic())
                .param("reasonCode", "1")
                .contentType(apiTestHelper.getContentType()))
                .andExpect(status().isNotFound());

        // GET /joboffers should now return only 1 element

        this.mockMvc.perform(get("/joboffers/")
                .with(apiTestHelper.getDefaultHttpBasic())
                .contentType(apiTestHelper.getContentType()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.jobOffers", Matchers.hasSize(1)));

    }

    @Test
    public void deleteNotAllowed() throws Exception {
        this.mockMvc.perform(delete("/joboffers/" + idJob2)
                .with(apiTestHelper.getDefaultHttpBasic())
                .contentType(apiTestHelper.getContentType()))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void authorizedCancellationReasonCode() throws Exception {
        this.mockMvc.perform(post("/joboffers/" + idJob2 + "/cancel")
                .with(apiTestHelper.getDefaultHttpBasic())
                .param("reasonCode", "4")
                .contentType(apiTestHelper.getContentType()))
                .andExpect(status().isBadRequest());

    }

}

package ch.admin.seco.jobroom;

import ch.admin.seco.jobroom.helpers.ApiTestHelper;
import ch.admin.seco.jobroom.helpers.DatasetTestHelper;
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
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApiApplication.class)
@WebAppConfiguration
public class EncodingTest {

    private MockMvc mockMvc;
    private int idJob;

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

        JobOffer jobOffer = DatasetTestHelper.getCompleteJobOffer();
        jobOffer.setOwner(restAccessKey);

        apiTestHelper.authenticateDefault();
        idJob = jobOfferRepository.save(jobOffer).getId();
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
    public void encodingGet() throws Exception {

        this.mockMvc.perform(get("/joboffers/" + idJob)
                .with(apiTestHelper.getDefaultHttpBasic())
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().encoding("UTF-8"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(header().string("Content-Type", "application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.contact.firstName", Matchers.is("Alizée")));
    }

    @Test
    public void encodingPost() throws Exception {

        String jobOfferJson = DatasetTestHelper.getCompleteJobOfferJson().toString();

        this.mockMvc.perform(post("/joboffers")
                .with(apiTestHelper.getDefaultHttpBasic())
                .contentType(apiTestHelper.getContentType())
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content(jobOfferJson))
                .andExpect(status().isCreated())
                .andExpect(content().encoding("UTF-8"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(header().string("Content-Type", "application/json;charset=UTF-8"));

        apiTestHelper.authenticateDefault();
        JobOffer lastSavedJob = com.google.common.collect.Iterables.getLast(jobOfferRepository.findAll());
        apiTestHelper.unAuthenticate();

        org.junit.Assert.assertEquals("Boulé", lastSavedJob.getContact().getLastName());
    }
}

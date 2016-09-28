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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@WebAppConfiguration
public class EncodingGetTest {

    private MockMvc mockMvc;
    private int idJob;

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

        JobOffer jobOffer = DatasetHelper.get();
        jobOffer.setOwner(restAccessKey);

        testHelper.authenticateDefault();
        idJob = jobOfferRepository.save(jobOffer).getId();
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
    public void encodingGet() throws Exception {

        this.mockMvc.perform(get("/joboffers/" + idJob)
                .with(testHelper.getDefaultHttpBasic())
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().encoding("UTF-8"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(header().string("Content-Type", "application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.contact.firstName", Matchers.is("Alizée")));
    }
}

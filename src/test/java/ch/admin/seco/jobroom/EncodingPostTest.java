package ch.admin.seco.jobroom;

import ch.admin.seco.jobroom.helpers.TestHelper;
import ch.admin.seco.jobroom.helpers.DatasetHelper;
import ch.admin.seco.jobroom.model.JobOffer;
import ch.admin.seco.jobroom.model.RestAccessKey;
import ch.admin.seco.jobroom.repository.JobOfferRepository;
import ch.admin.seco.jobroom.repository.RestAccessKeyRepository;
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

import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@WebAppConfiguration
public class EncodingPostTest {

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

        RestAccessKey restAccessKey = testHelper.getDefaultRestAccessKey();
        restAccessKeyRepository.save(restAccessKey);
    }

    @After
    public void cleanup() {
        testHelper.authenticateDefault();
        jobOfferRepository.deleteAll();
        testHelper.unAuthenticate();
        restAccessKeyRepository.deleteAll();
    }

    @Test
    public void encodingPost() throws Exception {

        String jobOfferJson = DatasetHelper.getJson().toString();

        this.mockMvc.perform(post("/joboffers")
                .with(testHelper.getDefaultHttpBasic())
                .contentType(testHelper.getContentType())
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content(jobOfferJson))
                .andExpect(status().isCreated())
                .andExpect(content().encoding("UTF-8"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(header().string("Content-Type", "application/json;charset=UTF-8"));

        testHelper.authenticateDefault();
        assertEquals(1, jobOfferRepository.count());
        JobOffer job = jobOfferRepository.findAll().iterator().next();
        testHelper.unAuthenticate();

        assertEquals("Boulé", job.getContact().getLastName());
    }
}

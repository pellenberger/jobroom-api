package ch.admin.seco.jobroom;

import ch.admin.seco.jobroom.helpers.TestHelper;
import ch.admin.seco.jobroom.helpers.DatasetHelper;
import ch.admin.seco.jobroom.model.JobOffer;
import ch.admin.seco.jobroom.model.RestAccessKey;
import ch.admin.seco.jobroom.repository.JobOfferRepository;
import ch.admin.seco.jobroom.repository.RestAccessKeyRepository;
import org.joda.time.LocalDate;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@WebAppConfiguration
public class OptimisticLockingTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    TestHelper testHelper;

    @Autowired
    RestAccessKeyRepository restAccessKeyRepository;

    @Autowired
    JobOfferRepository jobOfferRepository;

    private int idNewJobOffer;
    private JobOffer jobOffer;

    @Before
    public void setup() throws Exception {

        this.mockMvc = webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();

        RestAccessKey restAccessKey = testHelper.getDefaultRestAccessKey();
        restAccessKeyRepository.save(restAccessKey);

        testHelper.authenticateDefault();

        jobOffer = DatasetHelper.get();
        jobOffer.setOwner(restAccessKey);
        idNewJobOffer = jobOfferRepository.save(jobOffer).getId();

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
    public void optimisticLocking() throws Exception {

        // validate presence and value of ETag header
        this.mockMvc.perform(get("/joboffers/" + idNewJobOffer).with(testHelper.getDefaultHttpBasic()))
                .andExpect(status().isOk())
                .andExpect(header().string("ETag", "\"0\""));

        // update resource (change version)
        testHelper.authenticateDefault();
        jobOffer.setPublicationEndDate(LocalDate.parse("2101-02-03"));
        jobOfferRepository.save(jobOffer);
        testHelper.unAuthenticate();

        // try to patch with previous version tag
        JSONObject jobOfferJson = DatasetHelper.getJsonPartial();
        this.mockMvc.perform(patch("/joboffers/" + idNewJobOffer).with(testHelper.getDefaultHttpBasic())
                .contentType(testHelper.getContentType())
                .header("If-Match", "0")
                .content(jobOfferJson.toString()))
                .andExpect(status().isPreconditionFailed());

        // try to patch with correct version tag
        this.mockMvc.perform(patch("/joboffers/" + idNewJobOffer).with(testHelper.getDefaultHttpBasic())
                .contentType(testHelper.getContentType())
                .header("If-Match", "1")
                .content(jobOfferJson.toString()))
                .andExpect(status().isNoContent());
    }
}

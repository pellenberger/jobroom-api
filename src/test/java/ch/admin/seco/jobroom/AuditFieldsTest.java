package ch.admin.seco.jobroom;

import ch.admin.seco.jobroom.helpers.ApiTestHelper;
import ch.admin.seco.jobroom.helpers.JobOfferDatasetHelper;
import ch.admin.seco.jobroom.model.JobOffer;
import ch.admin.seco.jobroom.model.RestAccessKey;
import ch.admin.seco.jobroom.repository.JobOfferRepository;
import ch.admin.seco.jobroom.repository.RestAccessKeyRepository;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApiApplication.class)
@WebAppConfiguration
public class AuditFieldsTest {

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

    @Test
    public void auditFields() throws Exception {

        // create new job offer
        this.mockMvc.perform(post("/joboffers")
                .with(apiTestHelper.getDefaultHttpBasic())
                .contentType(apiTestHelper.getContentType())
                .content(JobOfferDatasetHelper.getJson().toString()))
                .andExpect(status().isCreated());

        apiTestHelper.authenticateDefault();
        JobOffer jobOffer = jobOfferRepository.findAll().iterator().next();
        apiTestHelper.unAuthenticate();

        // expect creationDate to be "now"
        int dSeconds = 5;
        DateTime creationDate = new DateTime(jobOffer.getCreationDate());
        DateTime now = DateTime.now();
        Assert.assertTrue(creationDate.isAfter(now.minusSeconds(dSeconds)) && creationDate.isBefore(now.plusSeconds(dSeconds)));

        // expect lastModificationDate to be null
        Assert.assertNull(jobOffer.getLastModificationDate());

        // update job offer
        this.mockMvc.perform(patch("/joboffers/" + jobOffer.getId())
                .with(apiTestHelper.getDefaultHttpBasic())
                .contentType(apiTestHelper.getContentType())
                .content(JobOfferDatasetHelper.getJsonPartial().toString()))
                .andExpect(status().isNoContent());

        // expect lastModificationDate to be "now"
        DateTime lastModificationDate = new DateTime(jobOffer.getLastModificationDate());
        now = DateTime.now();
        Assert.assertTrue(lastModificationDate.isAfter(now.minusSeconds(dSeconds)) && lastModificationDate.isBefore(now.plusSeconds(dSeconds)));
    }
}

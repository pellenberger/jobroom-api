package ch.admin.seco.jobroom;

import ch.admin.seco.jobroom.helpers.ApiTestHelper;
import ch.admin.seco.jobroom.helpers.JobOfferDatasetHelper;
import ch.admin.seco.jobroom.model.Application;
import ch.admin.seco.jobroom.model.JobOffer;
import ch.admin.seco.jobroom.model.RestAccessKey;
import ch.admin.seco.jobroom.repository.JobOfferRepository;
import ch.admin.seco.jobroom.repository.RestAccessKeyRepository;
import org.junit.After;
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
/**
 * Test validation of company informations compared to application types
 */
public class CompanyInformationsTest {

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
        apiTestHelper.authenticateDefault();

        // create job with application.electronic = 1 and application.telephonic = 1
        JobOffer job = JobOfferDatasetHelper.getWithApplication(new Application(1, 0, 1, "", ""));
        job.setOwner(restAccessKey);
        jobOfferRepository.save(job);
        idJob = job.getId();

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
    public void applicationElectronic() throws Exception {

        // on POST : company.email empty

        this.mockMvc.perform(post("/joboffers")
                .with(apiTestHelper.getDefaultHttpBasic())
                .contentType(apiTestHelper.getContentType())
                .content(JobOfferDatasetHelper.getJsonWithCompanyInformations("", "+41123456789").toString()))
                .andExpect(status().isBadRequest());

        // on PATCH : company.email empty

        this.mockMvc.perform(patch("/joboffers/" + idJob)
                .with(apiTestHelper.getDefaultHttpBasic())
                .contentType(apiTestHelper.getContentType())
                .content(JobOfferDatasetHelper.getJsonWithCompanyInformations("", "+41123456789").toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void applicationTelephonic() throws Exception {

        // on POST : company.phoneNumber empty

        this.mockMvc.perform(post("/joboffers")
                .with(apiTestHelper.getDefaultHttpBasic())
                .contentType(apiTestHelper.getContentType())
                .content(JobOfferDatasetHelper.getJsonWithCompanyInformations("test@example.com", "").toString()))
                .andExpect(status().isBadRequest());

        // on PATCH : company.phoneNumber empty

        this.mockMvc.perform(patch("/joboffers/" + idJob)
                .with(apiTestHelper.getDefaultHttpBasic())
                .contentType(apiTestHelper.getContentType())
                .content(JobOfferDatasetHelper.getJsonWithCompanyInformations("test@example.com", "").toString()))
                .andExpect(status().isBadRequest());

    }
}

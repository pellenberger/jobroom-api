package ch.admin.seco.jobroom;

import ch.admin.seco.jobroom.helpers.ApiTestHelper;
import ch.admin.seco.jobroom.helpers.DatasetTestHelper;
import ch.admin.seco.jobroom.model.JobOffer;
import ch.admin.seco.jobroom.model.RestAccessKey;
import ch.admin.seco.jobroom.repository.JobOfferRepository;
import ch.admin.seco.jobroom.repository.RestAccessKeyRepository;
import org.joda.time.LocalDate;
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

import javax.json.JsonObject;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApiApplication.class)
@WebAppConfiguration
public class PublicationStartDateTest {

    private MockMvc mockMvc;

    private int idDefaultJob;
    private int idPublishedJobToday;
    private int idPublishedJobNotToday;

    private static final int MINUS_DAYS = 10;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    ApiTestHelper apiTestHelper;

    @Autowired
    RestAccessKeyRepository restAccessKeyRepository;

    @Autowired
    JobOfferRepository jobOfferRepository;

    /**
     * @return current date formatted yyyy-mm-dd
     */
    private String getCurrentDate() {
        return new LocalDate().toString();
    }

    /**
     * @return yesterday's date formatted yyyy-mm-dd
     */
    private String getYesterdayDate() {
       return new LocalDate().minusDays(1).toString();
    }

    @Before
    public void setup() throws Exception {

        this.mockMvc = webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();

        RestAccessKey restAccessKey = apiTestHelper.getDefaultRestAccessKey();
        restAccessKeyRepository.save(restAccessKey);
        apiTestHelper.authenticateDefault();

        // create default job offer
        JobOffer defaultJob = DatasetTestHelper.getCompleteJobOffer();
        defaultJob.setOwner(restAccessKey);
        idDefaultJob = jobOfferRepository.save(defaultJob).getId();

        // create job offer published since today
        JobOffer publishedJobToday = DatasetTestHelper.getCompleteJobOffer(new LocalDate().toString());
        publishedJobToday.setOwner(restAccessKey);
        idPublishedJobToday = jobOfferRepository.save(publishedJobToday).getId();

        // create job offer published since 10 day
        JobOffer publishedJobNotToday = DatasetTestHelper.getCompleteJobOffer(new LocalDate().minusDays(MINUS_DAYS).toString());
        publishedJobNotToday.setOwner(restAccessKey);
        idPublishedJobNotToday = jobOfferRepository.save(publishedJobNotToday).getId();

        apiTestHelper.unAuthenticate();
    }

    @After
    public void cleanup() {
        apiTestHelper.authenticateDefault();
        jobOfferRepository.deleteAll();
        apiTestHelper.unAuthenticate();
        restAccessKeyRepository.deleteAll();
    }

    /**
     * Test that publication start date cannot be smaller than current date
     */
    @Test
    public void currentDate() throws Exception {

        // on POST : publication start date < current date -> KO

        this.mockMvc.perform(post("/joboffers")
                .with(apiTestHelper.getDefaultHttpBasic())
                .contentType(apiTestHelper.getContentType())
                .content(DatasetTestHelper.getCompleteJobOfferJson(getYesterdayDate()).toString()))
                .andExpect(status().isBadRequest());

        // on POST : publication start date == current date -> OK

        this.mockMvc.perform(post("/joboffers")
                .with(apiTestHelper.getDefaultHttpBasic())
                .contentType(apiTestHelper.getContentType())
                .content(DatasetTestHelper.getCompleteJobOfferJson(getCurrentDate()).toString()))
                .andExpect(status().isCreated());

        // on PATCH : publication start date < current date -> KO

        this.mockMvc.perform(patch("/joboffers/" + idDefaultJob)
                .with(apiTestHelper.getDefaultHttpBasic())
                .contentType(apiTestHelper.getContentType())
                .content(DatasetTestHelper.getCompleteJobOfferJson(getYesterdayDate()).toString()))
                .andExpect(status().isBadRequest());

        // on PATCH : publication start date == current date -> OK

        this.mockMvc.perform(patch("/joboffers/" + idDefaultJob)
                .with(apiTestHelper.getDefaultHttpBasic())
                .contentType(apiTestHelper.getContentType())
                .content(DatasetTestHelper.getCompleteJobOfferJson(getCurrentDate()).toString()))
                .andExpect(status().isNoContent());
    }

    /**
     * Test that the publication start date of a job cannot be changed if this one is already published
     * Job is published when publication start date <= current date
     */
    @Test
    public void alreadyPublished() throws Exception {

        JsonObject newPublicationDateJob = DatasetTestHelper.getCompleteJobOfferJson(new LocalDate().plusDays(15).toString());
        JsonObject samePublicationDateJob = DatasetTestHelper.getCompleteJobOfferJson(new LocalDate().minusDays(MINUS_DAYS).toString());

        // job already published (publication start date == today) -> KO
        this.mockMvc.perform(patch("/joboffers/" + idPublishedJobToday)
                .with(apiTestHelper.getDefaultHttpBasic())
                .contentType(apiTestHelper.getContentType())
                .content(newPublicationDateJob.toString()))
                .andExpect(status().isBadRequest());

        // job already published (publication start date < today) -> KO
        this.mockMvc.perform(patch("/joboffers/" + idPublishedJobNotToday)
                .with(apiTestHelper.getDefaultHttpBasic())
                .contentType(apiTestHelper.getContentType())
                .content(newPublicationDateJob.toString()))
                .andExpect(status().isBadRequest());

        // job already published but publication start date doesn't change -> OK
        this.mockMvc.perform(patch("/joboffers/" + idPublishedJobNotToday)
                .with(apiTestHelper.getDefaultHttpBasic())
                .contentType(apiTestHelper.getContentType())
                .content(samePublicationDateJob.toString()))
                .andExpect(status().isNoContent());
    }
}

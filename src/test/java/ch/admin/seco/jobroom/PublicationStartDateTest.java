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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApiApplication.class)
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
    TestHelper testHelper;

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

        RestAccessKey restAccessKey = testHelper.getDefaultRestAccessKey();
        restAccessKeyRepository.save(restAccessKey);
        testHelper.authenticateDefault();

        // create default job offer
        JobOffer defaultJob = DatasetHelper.get();
        defaultJob.setOwner(restAccessKey);
        idDefaultJob = jobOfferRepository.save(defaultJob).getId();

        // create job offer published since today
        JobOffer publishedJobToday = DatasetHelper.get();
        publishedJobToday.setPublicationStartDate(new LocalDate());

        publishedJobToday.setOwner(restAccessKey);
        idPublishedJobToday = jobOfferRepository.save(publishedJobToday).getId();

        // create job offer published since 10 day
        JobOffer publishedJobNotToday = DatasetHelper.get();
        publishedJobNotToday.setPublicationStartDate(new LocalDate().minusDays(MINUS_DAYS));
        publishedJobNotToday.setOwner(restAccessKey);
        idPublishedJobNotToday = jobOfferRepository.save(publishedJobNotToday).getId();

        testHelper.unAuthenticate();
    }

    @After
    public void cleanup() {
        testHelper.authenticateDefault();
        jobOfferRepository.deleteAll();
        testHelper.unAuthenticate();
        restAccessKeyRepository.deleteAll();
    }

    /**
     * Test that publication start date cannot be smaller than current date
     */
    @Test
    public void currentDate() throws Exception {

        JSONObject jobOffer = DatasetHelper.getJson();

        // on POST : publication start date < current date -> KO

        this.mockMvc.perform(post("/joboffers")
                .with(testHelper.getDefaultHttpBasic())
                .contentType(testHelper.getContentType())
                .content(jobOffer.put("publicationStartDate", getYesterdayDate()).toString()))
                .andExpect(status().isBadRequest());

        // on POST : publication start date == current date -> OK

        this.mockMvc.perform(post("/joboffers")
                .with(testHelper.getDefaultHttpBasic())
                .contentType(testHelper.getContentType())
                .content(jobOffer.put("publicationStartDate", getCurrentDate()).toString()))
                .andExpect(status().isCreated());

        // on PATCH : publication start date < current date -> KO

        this.mockMvc.perform(patch("/joboffers/" + idDefaultJob)
                .with(testHelper.getDefaultHttpBasic())
                .contentType(testHelper.getContentType())
                .content(jobOffer.put("publicationStartDate", getYesterdayDate()).toString()))
                .andExpect(status().isBadRequest());

        // on PATCH : publication start date == current date -> OK

        this.mockMvc.perform(patch("/joboffers/" + idDefaultJob)
                .with(testHelper.getDefaultHttpBasic())
                .contentType(testHelper.getContentType())
                .content(jobOffer.put("publicationStartDate", getCurrentDate()).toString()))
                .andExpect(status().isNoContent());
    }

    /**
     * Test that the publication start date of a job cannot be changed if this one is already published
     * Job is published when publication start date <= current date
     */
    @Test
    public void alreadyPublished() throws Exception {

        JSONObject newPublicationDateJob = DatasetHelper.getJson()
                .put("publicationStartDate", new LocalDate().plusDays(15).toString());
        JSONObject samePublicationDateJob = DatasetHelper.getJson()
                .put("publicationStartDate", new LocalDate().minusDays(MINUS_DAYS).toString());

        // job already published (publication start date == today) -> KO
        this.mockMvc.perform(patch("/joboffers/" + idPublishedJobToday)
                .with(testHelper.getDefaultHttpBasic())
                .contentType(testHelper.getContentType())
                .content(newPublicationDateJob.toString()))
                .andExpect(status().isBadRequest());

        // job already published (publication start date < today) -> KO
        this.mockMvc.perform(patch("/joboffers/" + idPublishedJobNotToday)
                .with(testHelper.getDefaultHttpBasic())
                .contentType(testHelper.getContentType())
                .content(newPublicationDateJob.toString()))
                .andExpect(status().isBadRequest());

        // job already published but publication start date doesn't change -> OK
        this.mockMvc.perform(patch("/joboffers/" + idPublishedJobNotToday)
                .with(testHelper.getDefaultHttpBasic())
                .contentType(testHelper.getContentType())
                .content(samePublicationDateJob.toString()))
                .andExpect(status().isNoContent());
    }
}

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@WebAppConfiguration
public class PagingTest {

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

        //
        // Create a dataset of 6 job offers
        //

        RestAccessKey restAccessKey = testHelper.getDefaultRestAccessKey();
        restAccessKeyRepository.save(restAccessKey);
        testHelper.authenticateDefault();

        for (int i = 0; i < 6; i ++) {
            JobOffer jobOffer = DatasetHelper.get();
            jobOffer.setOwner(restAccessKey);
            jobOfferRepository.save(jobOffer);
        }

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
    public void paging() throws Exception {

        // without paging
        this.mockMvc.perform(get("/joboffers").with(testHelper.getDefaultHttpBasic()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.jobOffers", Matchers.hasSize(6)));

        // page 1/3
        this.mockMvc.perform(get("/joboffers/?size=2").with(testHelper.getDefaultHttpBasic()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.jobOffers", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.page.size", Matchers.is(2)))
                .andExpect(jsonPath("$.page.totalElements", Matchers.is(6)))
                .andExpect(jsonPath("$.page.totalPages", Matchers.is(3)))
                .andExpect(jsonPath("$.page.number", Matchers.is(0)))
                .andExpect(jsonPath("$._links.first").exists())
                .andExpect(jsonPath("$._links.last").exists())
                .andExpect(jsonPath("$._links.prev").doesNotExist())
                .andExpect(jsonPath("$._links.next").exists());

        // page 2/3
        this.mockMvc.perform(get("/joboffers/?size=2&page=1").with(testHelper.getDefaultHttpBasic()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.jobOffers", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.page.size", Matchers.is(2)))
                .andExpect(jsonPath("$.page.totalElements", Matchers.is(6)))
                .andExpect(jsonPath("$.page.totalPages", Matchers.is(3)))
                .andExpect(jsonPath("$.page.number", Matchers.is(1)))
                .andExpect(jsonPath("$._links.first").exists())
                .andExpect(jsonPath("$._links.last").exists())
                .andExpect(jsonPath("$._links.prev").exists())
                .andExpect(jsonPath("$._links.next").exists());

        // page 3/3
        this.mockMvc.perform(get("/joboffers/?size=2&page=2").with(testHelper.getDefaultHttpBasic()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.jobOffers", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.page.size", Matchers.is(2)))
                .andExpect(jsonPath("$.page.totalElements", Matchers.is(6)))
                .andExpect(jsonPath("$.page.totalPages", Matchers.is(3)))
                .andExpect(jsonPath("$.page.number", Matchers.is(2)))
                .andExpect(jsonPath("$._links.first").exists())
                .andExpect(jsonPath("$._links.last").exists())
                .andExpect(jsonPath("$._links.prev").exists())
                .andExpect(jsonPath("$._links.next").doesNotExist());
    }
}

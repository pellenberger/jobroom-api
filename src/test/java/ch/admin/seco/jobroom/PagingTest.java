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
@SpringApplicationConfiguration(classes = ApiApplication.class)
@WebAppConfiguration
public class PagingTest {

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

        //
        // Create a dataset of 6 job offers
        //

        RestAccessKey restAccessKey = apiTestHelper.getDefaultRestAccessKey();
        restAccessKeyRepository.save(restAccessKey);
        apiTestHelper.authenticateDefault();

        for (int i = 0; i < 6; i ++) {
            JobOffer jobOffer = DatasetTestHelper.getCompleteJobOffer();
            jobOffer.setOwner(restAccessKey);
            jobOfferRepository.save(jobOffer);
        }

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
    public void paging() throws Exception {

        // without paging
        this.mockMvc.perform(get("/joboffers").with(apiTestHelper.getDefaultHttpBasic()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.jobOffers", Matchers.hasSize(6)));

        // page 1/3
        this.mockMvc.perform(get("/joboffers/?size=2").with(apiTestHelper.getDefaultHttpBasic()))
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
        this.mockMvc.perform(get("/joboffers/?size=2&page=1").with(apiTestHelper.getDefaultHttpBasic()))
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
        this.mockMvc.perform(get("/joboffers/?size=2&page=2").with(apiTestHelper.getDefaultHttpBasic()))
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

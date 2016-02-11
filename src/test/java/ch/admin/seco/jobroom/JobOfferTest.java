package ch.admin.seco.jobroom;

import ch.admin.seco.jobroom.helpers.ApiTestHelper;
import ch.admin.seco.jobroom.helpers.JobOfferTestHelper;
import ch.admin.seco.jobroom.web.JobOfferRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApiApplication.class)
@WebAppConfiguration
public class JobOfferTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    JobOfferRepository jobOfferRepository;

    @Autowired
    ApiTestHelper apiTestHelper;

    @Before
    public void setup() throws Exception {

        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void createIncompleteJobOffer() throws Exception {

        String jobOffer = JobOfferTestHelper.getIncompleteJobOfferJson().toString();

        mockMvc.perform(post("/joboffers")
                .contentType(apiTestHelper.getContentType())
                .content(jobOffer))
                .andExpect(status().isBadRequest());
    }
}

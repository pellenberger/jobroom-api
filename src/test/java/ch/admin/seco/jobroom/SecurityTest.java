package ch.admin.seco.jobroom;

import ch.admin.seco.jobroom.helpers.ApiTestHelper;
import ch.admin.seco.jobroom.helpers.JobOfferTestHelper;
import ch.admin.seco.jobroom.model.JobOffer;
import ch.admin.seco.jobroom.model.RestAccessKey;
import ch.admin.seco.jobroom.repository.JobOfferRepository;
import ch.admin.seco.jobroom.repository.RestAccessKeyRepository;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApiApplication.class)
@WebAppConfiguration
public class SecurityTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    ApiTestHelper apiTestHelper;

    @Autowired
    RestAccessKeyRepository restAccessKeyRepository;

    @Autowired
    JobOfferRepository jobOfferRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    private static boolean isInitialized = false;
    private static RestAccessKey key1;
    private static RestAccessKey key2;
    private static RestAccessKey key3;
    private static RestAccessKey key4;

    @BeforeClass
    public static void setupClass() throws Exception {
        key1 = new RestAccessKey("key1", "owner1", 1);
        key2 = new RestAccessKey("key2", "owner2", 1);
        key3 = new RestAccessKey("key3", "owner3", 1);
        key4 = new RestAccessKey("key4", "owner4", 0);

    }

    @Before
    public void setup() throws Exception {

        this.mockMvc = webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();

        if (!isInitialized) {
            setupOnce();
            isInitialized = true;
        }
    }

    /**
     * Creates 3 active users containing each 3 joboffers + 1 inactive user
     */
    private void setupOnce() {

        List<RestAccessKey> keys = new ArrayList<>(Arrays.asList(
                key1,
                key2,
                key3
        ));

        for (int i = 0; i < 3; i ++) {
            RestAccessKey key = keys.get(i);
            restAccessKeyRepository.save(key);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(key.getKeyOwner(), key.getAccessKey());
            SecurityContext securityContext = new SecurityContextImpl();
            securityContext.setAuthentication(authenticationToken);
            authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.setContext(securityContext);

            for(int j = 0; j < 3; j ++) {
                JobOffer job = JobOfferTestHelper.getCompleteJobOffer();
                job.setOwner(key);
                jobOfferRepository.save(job);
            }
        }
    }

    @Test
    public void accessWithoutAuth() throws Exception {

        this.mockMvc.perform(get("/joboffers"))
                .andExpect(status().isUnauthorized());

        this.mockMvc.perform(post("/joboffers")
                .contentType(apiTestHelper.getContentType())
                .content(JobOfferTestHelper.getCompleteJobOfferJson().toString()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void accessWithBadCredentials() throws Exception {

        this.mockMvc.perform(get("/joboffers").with(httpBasic("wrong_user", "wrong_password")))
                .andExpect(status().isUnauthorized());

        this.mockMvc.perform(post("/joboffers")
                .contentType(apiTestHelper.getContentType())
                .content(JobOfferTestHelper.getCompleteJobOfferJson().toString()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void restAccessKeys() throws Exception {

        this.mockMvc.perform(get("/restAccessKeys").with(httpBasic(key1.getKeyOwner(), key1.getAccessKey())))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getOnlyOwningJobs() throws Exception {
        this.mockMvc.perform(get("/joboffers").with(httpBasic(key1.getKeyOwner(), key1.getAccessKey())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.jobOffers", Matchers.hasSize(3)));
    }

    @Test
    public void postJob() throws Exception {
        this.mockMvc.perform(post("/joboffers")
                .with(httpBasic(key2.getKeyOwner(), key2.getAccessKey()))
                .contentType(apiTestHelper.getContentType())
                .content(JobOfferTestHelper.getCompleteJobOfferJson().toString()))
                .andExpect(status().isCreated());

        // user that created the job has one more job on its collection
        this.mockMvc.perform(get("/joboffers").with(httpBasic(key2.getKeyOwner(), key2.getAccessKey())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.jobOffers", Matchers.hasSize(4)));

        // other user has always the same number of jobs
        this.mockMvc.perform(get("/joboffers").with(httpBasic(key3.getKeyOwner(), key3.getAccessKey())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.jobOffers", Matchers.hasSize(3)));
    }

    @Test
    public void accessNotOwningJob() throws Exception {

        // job created in data.sql with login user:password
        int id = 1;
        this.mockMvc.perform(get("/joboffers/" + String.valueOf(id)).with(httpBasic("user", "password")))
                .andExpect(status().isOk());

        // try to access the job with owner2 (GET)
        this.mockMvc.perform(get("/joboffers/" + String.valueOf(id)).with(httpBasic(key2.getKeyOwner(), key2.getAccessKey())))
                .andExpect(status().isNotFound());

        // try to access the job with owner2 (DELETE)
        this.mockMvc.perform(delete("/joboffers/" + String.valueOf(id)).with(httpBasic(key2.getKeyOwner(), key2.getAccessKey())))
                .andExpect(status().isNotFound());

        // try to access the job with owner2 (PATCH)
        this.mockMvc.perform(patch("/joboffers/" + String.valueOf(id))
                .with(httpBasic(key2.getKeyOwner(), key2.getAccessKey()))
                .contentType(apiTestHelper.getContentType())
                .content(JobOfferTestHelper.getCompleteJobOfferJson().toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void accessWithInactiveKey() throws Exception {

        this.mockMvc.perform(get("/joboffers").with(httpBasic(key4.getKeyOwner(), key4.getAccessKey())))
                .andExpect(status().isUnauthorized());
    }
}

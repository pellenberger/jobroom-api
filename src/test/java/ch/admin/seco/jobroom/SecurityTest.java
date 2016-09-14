package ch.admin.seco.jobroom;

import ch.admin.seco.jobroom.helpers.ApiTestHelper;
import ch.admin.seco.jobroom.helpers.JobOfferDatasetHelper;
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
@SpringBootTest(classes = ApiApplication.class)
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

    private List<RestAccessKey> users;

    private RestAccessKey user1;
    private RestAccessKey user2;
    private RestAccessKey user3;

    private int idJobUser1;
    private int idJobUser2;

    @Before
    public void setup() throws Exception {

        this.mockMvc = webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();

        //
        // Create dataset
        // 2 active users + 1 inactive user containing each 3 joboffers
        //

        user1 = new RestAccessKey("key1", "ownerName1", "ownerEmail1", 1);
        user2 = new RestAccessKey("key2", "ownerName2", "ownerEmail2", 1);
        user3 = new RestAccessKey("key3", "ownerName3", "ownerEmail3", 1);

        users = new ArrayList<>(Arrays.asList(
                user1,
                user2,
                user3
        ));

        for (int i = 0; i < 3; i ++) {
            RestAccessKey user = users.get(i);
            restAccessKeyRepository.save(user);
            apiTestHelper.authenticate(user);

            for(int j = 0; j < 3; j ++) {
                JobOffer job = JobOfferDatasetHelper.get();
                job.setOwner(user);
                jobOfferRepository.save(job);

                // save id of one of user1 and user2 job
                if (i == 0 && j == 0) {
                    idJobUser1 = job.getId();
                } else if (i == 1 && j == 0) {
                    idJobUser2 = job.getId();
                }
            }
        }

        user3.setActive(0);
        restAccessKeyRepository.save(user3);

        apiTestHelper.unAuthenticate();
    }

    @After
    public void cleanup() {

        user3.setActive(1);
        restAccessKeyRepository.save(user3);

        for (int i = 0; i < 3; i ++) {
            apiTestHelper.authenticate(users.get(i));
            jobOfferRepository.deleteAll();
        }
        restAccessKeyRepository.deleteAll();
        apiTestHelper.unAuthenticate();
    }

    @Test
    public void accessWithoutAuth() throws Exception {

       this.mockMvc.perform(get("/joboffers"))
                .andExpect(status().isUnauthorized());

        this.mockMvc.perform(post("/joboffers")
                .contentType(apiTestHelper.getContentType())
                .content(JobOfferDatasetHelper.get().toString()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void accessWithBadCredentials() throws Exception {

        // wrong user and wrong password (GET / POST)

        this.mockMvc.perform(get("/joboffers").with(httpBasic("wrong_user", "wrong_password")))
                .andExpect(status().isUnauthorized());

        this.mockMvc.perform(post("/joboffers").with(httpBasic("wrong_user", "wrong_password"))
                .contentType(apiTestHelper.getContentType())
                .content(JobOfferDatasetHelper.get().toString()))
                .andExpect(status().isUnauthorized());

        // correct user but wrong password (GET / POST)

        this.mockMvc.perform(get("/joboffers").with(httpBasic(user1.getOwnerName(), "wrong_password")))
                .andExpect(status().isUnauthorized());

        this.mockMvc.perform(post("/joboffers").with(httpBasic(user1.getOwnerName(), "wrong_password"))
                .contentType(apiTestHelper.getContentType())
                .content(JobOfferDatasetHelper.get().toString()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void restAccessKeys() throws Exception {
        // RestAccessKey resource must not be exposed through API
        this.mockMvc.perform(get("/restAccessKeys").with(httpBasic(user1.getOwnerName(), user1.getAccessKey())))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getOnlyOwningJobs() throws Exception {
        this.mockMvc.perform(get("/joboffers").with(httpBasic(user1.getOwnerName(), user1.getAccessKey())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.jobOffers", Matchers.hasSize(3)));
    }

    @Test
    public void postJob() throws Exception {
        this.mockMvc.perform(post("/joboffers")
                .with(httpBasic(user1.getOwnerName(), user1.getAccessKey()))
                .contentType(apiTestHelper.getContentType())
                .content(JobOfferDatasetHelper.getJson().toString()))
                .andExpect(status().isCreated());

        // user that created the job has one more job on its collection
        this.mockMvc.perform(get("/joboffers").with(httpBasic(user1.getOwnerName(), user1.getAccessKey())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.jobOffers", Matchers.hasSize(4)));

        // other user has always the same number of jobs
        this.mockMvc.perform(get("/joboffers").with(httpBasic(user2.getOwnerName(), user2.getAccessKey())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.jobOffers", Matchers.hasSize(3)));
    }

    @Test
    public void accessNotOwningJob() throws Exception {

        this.mockMvc.perform(get("/joboffers/" + String.valueOf(idJobUser1)).with(httpBasic(user1.getOwnerName(), user1.getAccessKey())))
                .andExpect(status().isOk());

        // try to access job of other owner (GET)
        this.mockMvc.perform(get("/joboffers/" + String.valueOf(idJobUser2)).with(httpBasic(user1.getOwnerName(), user1.getAccessKey())))
                .andExpect(status().isNotFound());

        // try to access job of other owner (POST /cancel)
        this.mockMvc.perform(post("/joboffers/" + String.valueOf(idJobUser2) + "/cancel")
                .with(httpBasic(user1.getOwnerName(), user1.getAccessKey())))
                .andExpect(status().isNotFound());

        // try to access job of other owner (PATCH)
        this.mockMvc.perform(patch("/joboffers/" + String.valueOf(idJobUser2))
                .with(httpBasic(user1.getOwnerName(), user1.getAccessKey()))
                .contentType(apiTestHelper.getContentType())
                .content(JobOfferDatasetHelper.get().toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void accessWithInactiveKey() throws Exception {

        this.mockMvc.perform(get("/joboffers").with(httpBasic(user3.getOwnerName(), user3.getAccessKey())))
                .andExpect(status().isUnauthorized());

        user3.setActive(1);
        restAccessKeyRepository.save(user3);

        this.mockMvc.perform(get("/joboffers").with(httpBasic(user3.getOwnerName(), user3.getAccessKey())))
                .andExpect(status().isOk());
    }
}

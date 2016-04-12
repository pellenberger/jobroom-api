package ch.admin.seco.jobroom.doc;

import ch.admin.seco.jobroom.ApiApplication;
import ch.admin.seco.jobroom.helpers.ApiTestHelper;
import ch.admin.seco.jobroom.helpers.DatasetTestHelper;
import ch.admin.seco.jobroom.model.JobOffer;
import ch.admin.seco.jobroom.model.RestAccessKey;
import ch.admin.seco.jobroom.repository.JobOfferRepository;
import ch.admin.seco.jobroom.repository.RestAccessKeyRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.restdocs.RestDocumentation;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApiApplication.class)
@WebAppConfiguration
/**
 * This class generates doc snippets to be included in Asciidoc documentation
 */
public class ApiDocEdit {

    private MockMvc mockMvc;

    private int idNewJobOffer;

    @Rule
    public final RestDocumentation restDocumentation = new RestDocumentation("build/generated-snippets");

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    JobOfferRepository jobOfferRepository;

    @Autowired
    RestAccessKeyRepository restAccessKeyRepository;

    @Autowired
    ApiTestHelper apiTestHelper;

    @Before
    public void setup() throws Exception {

        this.mockMvc = webAppContextSetup(webApplicationContext)
                .apply(apiTestHelper.getDocumentationConfiguration(restDocumentation))
                .apply(springSecurity())
                .build();

        RestAccessKey restAccessKey = apiTestHelper.getDefaultRestAccessKey();
        restAccessKeyRepository.save(restAccessKey);

        apiTestHelper.authenticateDefault();

        JobOffer jobOffer = DatasetTestHelper.getCompleteJobOffer();
        jobOffer.setOwner(restAccessKey);
        idNewJobOffer = jobOfferRepository.save(jobOffer).getId();
    }

    @After
    public void cleanup() {
        apiTestHelper.authenticateDefault();
        jobOfferRepository.deleteAll();
        restAccessKeyRepository.deleteAll();
    }

    @Test
    public void editJobOffer() throws Exception {

        String jobOfferJson = DatasetTestHelper.getPartialJobOfferJson().toString();

        this.mockMvc.perform(patch("/joboffers/" + idNewJobOffer)
                .with(apiTestHelper.getDefaultHttpBasic())
                .contentType(apiTestHelper.getContentType())
                .content(jobOfferJson))
                .andExpect(status().isNoContent())

                .andDo(document("{method-name}", apiTestHelper.getPreprocessRequest(), apiTestHelper.getPreprocessResponse()));
    }
}

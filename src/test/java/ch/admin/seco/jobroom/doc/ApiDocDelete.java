package ch.admin.seco.jobroom.doc;

import ch.admin.seco.jobroom.ApiApplication;
import ch.admin.seco.jobroom.helpers.TestHelper;
import ch.admin.seco.jobroom.helpers.DatasetHelper;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@WebAppConfiguration
/**
 * This class generates doc snippets to be included in Asciidoc documentation
 */
public class ApiDocDelete {

    private MockMvc mockMvc;

    private int idNewJobOffer;

    @Value("${api.basePath}")
    private String basePath;

    @Rule
    public JUnitRestDocumentation restDocumentation =
            new JUnitRestDocumentation("build/generated-snippets");

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    JobOfferRepository jobOfferRepository;

    @Autowired
    RestAccessKeyRepository restAccessKeyRepository;

    @Autowired
    TestHelper testHelper;

    @Before
    public void setup() throws Exception {

        this.mockMvc = webAppContextSetup(webApplicationContext)
                .apply(testHelper.getDocumentationConfiguration(restDocumentation))
                .apply(springSecurity())
                .build();

        RestAccessKey restAccessKey = testHelper.getDefaultRestAccessKey();
        restAccessKeyRepository.save(restAccessKey);

        testHelper.authenticateDefault();

        JobOffer jobOffer = DatasetHelper.get();
        jobOffer.setOwner(restAccessKey);
        idNewJobOffer = jobOfferRepository.save(jobOffer).getId();

        testHelper.unAuthenticate();
    }

    @After
    public void cleanup() {

        testHelper.authenticateDefault();
        jobOfferRepository.deleteAll();
        restAccessKeyRepository.deleteAll();
        testHelper.unAuthenticate();
    }

    @Test
    public void cancelJobOffer() throws Exception {

        this.mockMvc.perform(post(basePath + "/joboffers/" + idNewJobOffer + "/cancel")
                .with(testHelper.getDefaultHttpBasic())
                .contextPath(basePath)
                .param("reasonCode", "1"))
                .andExpect(status().isNoContent())

                .andDo(document("{method-name}", testHelper.getPreprocessRequest(), testHelper.getPreprocessResponse(),
                        requestParameters(
                                parameterWithName("reasonCode")
                                        .description("Reason why the joboffer must be cancelled.")
                                        .attributes(key("constraints").value(
                                                "* Not null.\n" +
                                                "* Must be one of authorized reason codes (see section <<Cancellation reason codes>>)."
                                        )
                                        ))));
    }
}

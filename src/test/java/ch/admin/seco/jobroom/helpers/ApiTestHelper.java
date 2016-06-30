package ch.admin.seco.jobroom.helpers;

import ch.admin.seco.jobroom.model.RestAccessKey;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.restdocs.RestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationMockMvcConfigurer;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

@Component
public class ApiTestHelper {

    private final static String DEFAULT_USER_EMAIL = "email";
    private final static String DEFAULT_USER_NAME = "user";
    private final static String DEFAULT_PASSWORD = "password";
    private final static int DEFAULT_ACTIVE = 1;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    public MediaType getContentType() {
        return contentType;
    }

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(
                hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();

        Assert.assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    public String json(Object o) throws IOException {

        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

    public OperationRequestPreprocessor getPreprocessRequest() {
        return preprocessRequest(prettyPrint());
    }

    public OperationResponsePreprocessor getPreprocessResponse() {
        return preprocessResponse(prettyPrint());
    }

    public org.springframework.test.web.servlet.request.RequestPostProcessor getDefaultHttpBasic() {
        return httpBasic(DEFAULT_USER_NAME, DEFAULT_PASSWORD);
    }

    public RestDocumentationMockMvcConfigurer getDocumentationConfiguration(RestDocumentation restDocumentation) {
        RestDocumentationMockMvcConfigurer documentation = documentationConfiguration(restDocumentation);
        documentation.uris()
                    .withScheme("http")
                    .withHost("api.job-room.ch")
                    .withPort(80);
        return documentation;
    }

    public RestAccessKey getDefaultRestAccessKey() {
        return new RestAccessKey(DEFAULT_PASSWORD, DEFAULT_USER_NAME, DEFAULT_USER_EMAIL, DEFAULT_ACTIVE);
    }

    /**
     * User has to be defined in RestAccessKeyRepository
     * Don't forget to unauthenticate when dataset is created
     */
    public void authenticateDefault() {
        this.authenticate(DEFAULT_USER_NAME, DEFAULT_PASSWORD);
    }

    /**
     * User has to be defined in RestAccessKeyRepository
     * Don't forget to unauthenticate when dataset is created
     */
    public void authenticate(RestAccessKey restAccessKey) {
        this.authenticate(restAccessKey.getOwnerName(), restAccessKey.getAccessKey());
    }

    /**
     * User has to be defined in RestAccessKeyRepository
     * Don't forget to unauthenticate when dataset is created
     */
    public void authenticate(String username, String password) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        SecurityContext securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(authenticationToken);
        SecurityContextHolder.setContext(securityContext);
    }

    public void unAuthenticate() {
        SecurityContext securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(null);
        SecurityContextHolder.setContext(securityContext);
    }
}

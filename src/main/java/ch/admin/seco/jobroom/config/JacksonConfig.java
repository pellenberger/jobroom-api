package ch.admin.seco.jobroom.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.kotlin.ExtensionsKt;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

@Configuration
public class JacksonConfig extends RepositoryRestConfigurerAdapter {

    @Override
    public void configureJacksonObjectMapper(ObjectMapper objectMapper) {
        //TODO needed or not??? super.configureJacksonObjectMapper(objectMapper);
        ExtensionsKt.registerKotlinModule(objectMapper);
    }

}


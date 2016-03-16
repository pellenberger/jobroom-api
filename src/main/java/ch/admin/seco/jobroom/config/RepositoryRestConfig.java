package ch.admin.seco.jobroom.config;

import ch.admin.seco.jobroom.validator.BeforeCreateJobOfferValidator;
import ch.admin.seco.jobroom.validator.BeforeSaveJobOfferValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class RepositoryRestConfig extends RepositoryRestConfigurerAdapter {

    @Bean
    @Primary
    Validator validator() {
        return new LocalValidatorFactoryBean();
    }

    @Override
    public void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener validatingListener) {
        Validator validator = validator();
        validatingListener.addValidator("beforeCreate", validator);
        validatingListener.addValidator("beforeCreate", beforeCreateJobOfferValidator());
        validatingListener.addValidator("beforeSave", validator);
        validatingListener.addValidator("beforeSave", beforeSaveJobOfferValidator());
    }

    @Bean
    public BeforeSaveJobOfferValidator beforeSaveJobOfferValidator() {
        return new BeforeSaveJobOfferValidator();
    }

    @Bean
    public BeforeCreateJobOfferValidator beforeCreateJobOfferValidator() {
        return new BeforeCreateJobOfferValidator();
    }
}

package ch.admin.seco.jobroom.validator;


import ch.admin.seco.jobroom.model.JobOffer;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class BeforeCreateJobOfferValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return JobOffer.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        JobOffer jobOffer = (JobOffer) target;

        JobOfferValidatorHelper.validatePublicationStartDate(jobOffer, errors);
    }
}

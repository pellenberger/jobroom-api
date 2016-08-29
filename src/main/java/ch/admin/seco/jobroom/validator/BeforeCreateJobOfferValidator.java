package ch.admin.seco.jobroom.validator;


import ch.admin.seco.jobroom.model.JobOffer;
import org.joda.time.LocalDate;
import org.springframework.http.HttpStatus;
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

        validatePublicationStartDate(jobOffer, errors);
    }

    /**
     * Prevents user from saving a publication start date that is smaller than current date
     */
    private void validatePublicationStartDate(JobOffer jobOffer, Errors errors) {

        LocalDate today = new LocalDate();

        if (jobOffer.getPublicationStartDate().isBefore(today)) {
            errors.rejectValue("publicationStartDate",
                    String.valueOf(HttpStatus.BAD_REQUEST.value()),
                    "publicationStartDate cannot be smaller than current date");
        }
    }
}


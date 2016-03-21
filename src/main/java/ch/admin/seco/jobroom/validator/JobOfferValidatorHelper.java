package ch.admin.seco.jobroom.validator;


import ch.admin.seco.jobroom.model.JobOffer;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;

import java.sql.Date;

/**
 * Contains validation methods used by several event validators
 */
public class JobOfferValidatorHelper {

    /**
     * Prevents user from saving a publication start date that is smaller than current date
     */
    public static void validatePublicationStartDate(JobOffer jobOffer, Errors errors) {
        if (jobOffer.getPublicationStartDate().before(JobOfferValidatorHelper.now())) {
            errors.rejectValue("publicationStartDate",
                    String.valueOf(HttpStatus.BAD_REQUEST.value()),
                    "publicationStartDate cannot be smaller than current date");
        }
    }

    public static Date now() {
        return new Date(new java.util.Date().getTime());
    }
}

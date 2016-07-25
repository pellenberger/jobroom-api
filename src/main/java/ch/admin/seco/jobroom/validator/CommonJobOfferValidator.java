package ch.admin.seco.jobroom.validator;

import ch.admin.seco.jobroom.model.JobOffer;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;

/**
 * Validation methods used by both BeforeCreate (POST) and BeforeSave (PATCH)
 */
public class CommonJobOfferValidator {

    /**
     * company.email should not be empty if application.electronic is TRUE
     * company.phoneNumber should not be empty if application.telephonic is TRUE
     */
    public static void validateCompanyInformations(JobOffer jobOffer, Errors errors) {
        String companyEmail = jobOffer.getCompany().getEmail();
        if (jobOffer.getApplication().getElectronic() == 1 && (companyEmail == null || companyEmail.isEmpty())) {
            errors.rejectValue("company.email",
                    String.valueOf(HttpStatus.BAD_REQUEST.value()), "company.email cannot be empty when application.electronic is set to 1");
        }

        String companyPhoneNumber = jobOffer.getCompany().getPhoneNumber();
        if (jobOffer.getApplication().getTelephonic() == 1 && (companyPhoneNumber == null || companyPhoneNumber.isEmpty())) {
            errors.rejectValue("company.phoneNumber",
                    String.valueOf(HttpStatus.BAD_REQUEST.value()), "company.phoneNumber cannot be empty when application.telephonic is set to 1");
        }
    }
}

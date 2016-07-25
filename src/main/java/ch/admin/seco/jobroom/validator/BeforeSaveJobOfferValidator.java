package ch.admin.seco.jobroom.validator;

import ch.admin.seco.jobroom.model.JobOffer;
import ch.admin.seco.jobroom.repository.JobOfferRepository;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Validator triggered on PATCH
 */
@Component
public class BeforeSaveJobOfferValidator implements Validator {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    JobOfferRepository jobOfferRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return JobOffer.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        JobOffer jobOffer = (JobOffer) target;

        validatePublicationStartDateChangedOnPublishedJob(jobOffer, errors);
        CommonJobOfferValidator.validateCompanyInformations(jobOffer, errors);
    }

    /**
     * Prevents user from updating publication start date on a published job
     * Job is published when publication start date <= current date
     */
    private void validatePublicationStartDateChangedOnPublishedJob(JobOffer jobOffer, Errors errors) {
        entityManager.detach(jobOffer);
        JobOffer jobOfferOld = jobOfferRepository.findOne(jobOffer.getId());

        LocalDate today = new LocalDate();
        LocalDate publicationStartDateOld = new LocalDate(jobOfferOld.getPublicationStartDate());
        LocalDate publicationStartDate = new LocalDate(jobOffer.getPublicationStartDate());

        boolean jobAlreadyPublished = publicationStartDateOld.isBefore(today) || publicationStartDateOld.isEqual(today);
        boolean publicationStartDateChanged = !publicationStartDateOld.isEqual(publicationStartDate);

        if (jobAlreadyPublished && publicationStartDateChanged) {
            errors.rejectValue("publicationStartDate",
                    String.valueOf(HttpStatus.BAD_REQUEST.value()), "publicationStartDate cannot be changed on a job that is already published");
        } else if (!jobAlreadyPublished && publicationStartDate.isBefore(today)) {
            errors.rejectValue("publicationStartDate",
                    String.valueOf(HttpStatus.BAD_REQUEST.value()),
                    "publicationStartDate cannot be smaller than current date");
        }
    }
}

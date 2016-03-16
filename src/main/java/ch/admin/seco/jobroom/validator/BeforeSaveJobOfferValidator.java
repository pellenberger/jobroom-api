package ch.admin.seco.jobroom.validator;

import ch.admin.seco.jobroom.model.JobOffer;
import ch.admin.seco.jobroom.repository.JobOfferRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


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

        JobOffer jobOfferNew = (JobOffer) target;

        entityManager.detach(jobOfferNew);
        JobOffer jobOfferOld = jobOfferRepository.findOne(jobOfferNew.getId());

        java.sql.Date currentDate = new java.sql.Date(new java.util.Date().getTime());

        boolean jobAlreadyPublished = !(jobOfferOld.getPublicationStartDate().compareTo(currentDate) > 0);
        boolean publicationStartDateChanged = !jobOfferOld.getPublicationStartDate().equals(jobOfferNew.getPublicationStartDate());

        // prevents user from updating publication start date on a published job
        if (jobAlreadyPublished && publicationStartDateChanged) {
            errors.rejectValue("publicationStartDate", String.valueOf(HttpStatus.BAD_REQUEST.value()), "publicationStartDate cannot be changed on a job that is already published");
        }
    }
}

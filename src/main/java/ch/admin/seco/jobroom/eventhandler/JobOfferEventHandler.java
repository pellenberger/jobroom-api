package ch.admin.seco.jobroom.eventhandler;

import ch.admin.seco.jobroom.model.JobOffer;
import ch.admin.seco.jobroom.model.RestAccessKey;
import ch.admin.seco.jobroom.repository.RestAccessKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class JobOfferEventHandler {

    @Autowired
    RestAccessKeyRepository restAccessKeyRepository;

    @HandleBeforeCreate
    public void handleBeforeCreate(JobOffer jobOffer) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails currentUser = (UserDetails) principal;

        RestAccessKey restAccessKey = restAccessKeyRepository.findByKeyOwnerAndActive(currentUser.getUsername(), 1);

        jobOffer.setOwner(restAccessKey);
    }
}

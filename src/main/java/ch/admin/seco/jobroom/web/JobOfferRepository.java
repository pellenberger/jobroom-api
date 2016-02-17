
package ch.admin.seco.jobroom.web;

import ch.admin.seco.jobroom.model.JobOffer;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasRole('ROLE_USER')")
@RepositoryRestResource(path = "joboffers")
public interface JobOfferRepository extends PagingAndSortingRepository<JobOffer, Long> {}

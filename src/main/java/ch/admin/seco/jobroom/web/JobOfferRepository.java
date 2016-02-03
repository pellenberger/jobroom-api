
package ch.admin.seco.jobroom.web;

import ch.admin.seco.jobroom.model.JobOffer;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "joboffers")
// TODO enable "security" (authentication control)
public interface JobOfferRepository extends PagingAndSortingRepository<JobOffer, Long> {}

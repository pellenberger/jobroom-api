
package ch.admin.seco.jobroom.web;

import ch.admin.seco.jobroom.model.JobPosition;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
// TODO enable "security" (authentication control)
public interface JobPositionRepository extends PagingAndSortingRepository<JobPosition, Long> {}

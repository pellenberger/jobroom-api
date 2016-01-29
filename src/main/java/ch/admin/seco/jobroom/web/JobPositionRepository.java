
package ch.admin.seco.jobroom.web;

import ch.admin.seco.jobroom.model.JobPosition;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

// TODO enable "security" (authentication control) and "optimistic locking"
@RepositoryRestResource(collectionResourceRel = "job", path = "job")
public interface JobPositionRepository extends CrudRepository<JobPosition, Long> {}

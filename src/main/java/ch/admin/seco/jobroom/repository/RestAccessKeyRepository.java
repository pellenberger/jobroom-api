package ch.admin.seco.jobroom.repository;


import ch.admin.seco.jobroom.model.RestAccessKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

@RestResource(exported = false)
public interface RestAccessKeyRepository extends JpaRepository<RestAccessKey, Long> {

    RestAccessKey findByKeyOwnerAndActive(String keyOwner, int active);
}

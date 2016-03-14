package ch.admin.seco.jobroom.web;


import ch.admin.seco.jobroom.model.RestAccessKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestAccessKeyRepository extends JpaRepository<RestAccessKey, Long> {

    RestAccessKey findByKeyOwnerAndActive(String keyOwner, int active);
}

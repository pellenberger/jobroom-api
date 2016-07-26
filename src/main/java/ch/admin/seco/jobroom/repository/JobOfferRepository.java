
package ch.admin.seco.jobroom.repository;

import ch.admin.seco.jobroom.model.JobOffer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasRole('ROLE_USER')")
@RepositoryRestResource(path = "joboffers")
public interface JobOfferRepository extends PagingAndSortingRepository<JobOffer, Integer> {

    @Query("select j from JobOffer j where j.id = ?1 and j.owner.ownerName = ?#{principal?.username} and j.cancellationDate = null")
    @Override
    JobOffer findOne(Integer integer);

    @Query("select j from JobOffer j where j.owner.ownerName = ?#{principal?.username} and j.cancellationDate = null")
    @Override
    Page<JobOffer> findAll(Pageable pageable);

    @PreAuthorize("#joboffer.owner.ownerName == principal.username")
    @Override
    <S extends JobOffer> S save(@Param("joboffer") S entity);

    // ********
    // all following actions are not exposed through REST api
    // ********

    @RestResource(exported = false)
    @Override
    Iterable<JobOffer> findAll(Sort sort);

    @RestResource(exported = false)
    @Override
    Iterable<JobOffer> findAll();

    @RestResource(exported = false)
    @Override
    Iterable<JobOffer> findAll(Iterable<Integer> integers);

    @RestResource(exported = false)
    @Override
    long count();

    @RestResource(exported = false)
    @Override
    void delete(Integer integer);

    @RestResource(exported = false)
    @Override
    void delete(JobOffer entity);

    @RestResource(exported = false)
    @Override
    void delete(Iterable<? extends JobOffer> entities);

    @RestResource(exported = false)
    @Override
    void deleteAll();

    @RestResource(exported = false)
    @Override
    boolean exists(Integer integer);

    @RestResource(exported = false)
    @Override
    <S extends JobOffer> Iterable<S> save(Iterable<S> entities);
}

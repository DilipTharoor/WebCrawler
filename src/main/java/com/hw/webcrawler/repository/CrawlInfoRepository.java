package com.hw.webcrawler.repository;

import com.hw.webcrawler.entity.UrlInfoEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * CrawlInfoRepository is the DAO for {@link UrlInfoEntity} based on Spring Data JPA.
 *
 * @author Dilip Tharoor
 */
@Repository
public interface CrawlInfoRepository extends CrudRepository<UrlInfoEntity, Long> {

    /**
     * This gets the next link to read
     * @param limit is usually 1 to read the next entry
     * @return the next entity to consider for parsing
     */
    @Query("select entity from UrlInfoEntity entity where entity.depth = " +
            "(select MIN(entity.depth) from UrlInfoEntity entity where entity.parseCompleted = false)")
    List<UrlInfoEntity> findNextEligibleUrl(Pageable limit);

    List<UrlInfoEntity> findByUrl(String url);

    Optional<UrlInfoEntity> findById(long id);

}

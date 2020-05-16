package com.newscrawler.repository;


import com.newscrawler.entity.Keyword;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repository for handle CRUD of Keyword
 */
@Repository
public interface KeywordRepository extends CrudRepository<Keyword, Long> {
    /**
     * Retrieves an entity by its id.
     * @param newsId Id of current news
     * @return the entity with the given id or Optional#empty() if none found
     */
    Optional<Keyword> findByNewsId(Long newsId);
}

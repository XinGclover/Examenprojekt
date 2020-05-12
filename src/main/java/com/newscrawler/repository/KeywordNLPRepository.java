package com.newscrawler.repository;

import com.newscrawler.entity.KeywordNLP;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KeywordNLPRepository extends CrudRepository<KeywordNLP, Long> {
    /**
     * Retrieves an entity by its id.
     * @param newsId Id of current news
     * @return the entity with the given id or Optional#empty() if none found
     */
    Optional<KeywordNLP> findByNewsId(Long newsId);
}

package com.newscrawler.repository;


import com.newscrawler.entity.Keyword;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KeywordRepository extends CrudRepository<Keyword, Long> {
    Optional<Keyword> findByNewsId(Long newsId);
}

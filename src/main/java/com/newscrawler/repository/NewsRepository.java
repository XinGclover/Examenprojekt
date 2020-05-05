package com.newscrawler.repository;

import com.newscrawler.entity.News;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for handle CRUD of News
 */
@Repository
public interface NewsRepository extends CrudRepository<News, Long> {

}

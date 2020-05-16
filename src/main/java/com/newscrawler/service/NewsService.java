package com.newscrawler.service;

import com.newscrawler.entity.News;
import com.newscrawler.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service of news that handle saving, finding news
 */
@Service
public class NewsService {
    private final NewsRepository newsRepository;

    /**
     * Constructor
     * @param newsRepository object of NewsRepository
     */
    @Autowired
    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    /**
     * Save news to database
     * @param news object of News
     */
    public void saveNews(News news){
        newsRepository.save(news);
    }

    /**
     * Find all news through repository
     * @return List of all news
     */
    public List<News> findAllNews() {
        Iterable<News> newsIterable =  newsRepository.findAll();
        List<News> newsList = new ArrayList<>();
        for (News news : newsIterable) {
            newsList.add(news);
        }
        return newsList;
    }

    /**
     * Find new by newsId
     * @param id newsId
     * @return news with the id
     */
    public News findNewsById(Long id) {
        Optional<News> optionalNews = newsRepository.findById(id);
        News news= null;
        if (optionalNews.isPresent()) {
            news = optionalNews.get();
            return news;
        }
        return null;
    }


}

package com.newscrawler.service;

import com.newscrawler.entity.News;
import com.newscrawler.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NewsService {
    private final NewsRepository newsRepository;

    @Autowired
    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public void saveNews(News news){
        newsRepository.save(news);
    }

    public List<News> findAllNews() {
        Iterable<News> newsIterable =  newsRepository.findAll();
        List<News> newsList = new ArrayList<>();
        for (News news : newsIterable) {
            newsList.add(news);
        }
        return newsList;
    }

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

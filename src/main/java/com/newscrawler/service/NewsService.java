package com.newscrawler.service;

import com.newscrawler.entity.News;
import com.newscrawler.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewsService {
    private final NewsRepository newsRepository;

    @Autowired
    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public void saveNews(News news){
        newsRepository.save(news);
        System.out.println("HHHHHHHHHH save HHHHHHHHHHH");
    }

}

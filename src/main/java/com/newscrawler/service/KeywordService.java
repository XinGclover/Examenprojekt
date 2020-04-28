package com.newscrawler.service;

import com.newscrawler.entity.Keyword;
import com.newscrawler.entity.News;
import com.newscrawler.repository.KeywordRepository;
import com.newscrawler.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class KeywordService {
    private final KeywordRepository keywordRepository;
    private final NewsRepository newsRepository;

    @Autowired
    public KeywordService(KeywordRepository keywordRepository, NewsRepository newsRepository) {
        this.keywordRepository = keywordRepository;
        this.newsRepository = newsRepository;
    }

    public void saveKeyword(Keyword keyword){
        keywordRepository.save(keyword);
    }

    public List<Keyword> findAllByNewsId(Long newsId){
        Optional<News> optionalNews = newsRepository.findById(newsId);
        List<Keyword> keywordList = new ArrayList<>();
        if (optionalNews.isPresent()) {
            Iterator<Keyword> keywordIterator = optionalNews.get().getKeywords().iterator();
            while (keywordIterator.hasNext()) {
                Keyword keyword = keywordIterator.next();
                keywordList.add(keyword);
                }
            System.out.println("======keyword "+ keywordList.size());
            return keywordList;
            }
        return null;

        }

    }




package com.newscrawler.service;

import com.newscrawler.entity.Keyword;
import com.newscrawler.entity.KeywordNLP;
import com.newscrawler.entity.News;
import com.newscrawler.repository.KeywordNLPRepository;
import com.newscrawler.repository.KeywordRepository;
import com.newscrawler.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class KeywordNLPService {
    private final KeywordNLPRepository keywordNLPRepository;
    private final NewsRepository newsRepository;

    /**
     * Constructor
     * @param keywordNLPRepository object of KeywordRepository
     * @param newsRepository object of NewsRepository
     */
    @Autowired
    public KeywordNLPService(KeywordNLPRepository keywordNLPRepository, NewsRepository newsRepository) {
        this.keywordNLPRepository = keywordNLPRepository;
        this.newsRepository = newsRepository;
    }

    /**
     * Save keywordNLP to database
     * @param keywordNLP object of Keyword
     */
    public void saveKeyword(KeywordNLP keywordNLP){
        keywordNLPRepository.save(keywordNLP);
    }

    /**
     * Find all keywords of the news by newsId
     * @param newsId newsId of the news
     * @return list of Keyword of this news
     */
    public List<KeywordNLP> findAllByNewsId(Long newsId){
        Optional<News> optionalNews = newsRepository.findById(newsId);
        List<KeywordNLP> keywordList = new ArrayList<>();
        if (optionalNews.isPresent()) {
            Iterator<KeywordNLP> keywordIterator = optionalNews.get().getKeywordsNLP().iterator();
            while (keywordIterator.hasNext()) {
                KeywordNLP keyword = keywordIterator.next();
                keywordList.add(keyword);
            }
            return keywordList;
        }
        return null;

    }

}

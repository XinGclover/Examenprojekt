package com.newscrawler.service;

import com.newscrawler.entity.Keyword;
import com.newscrawler.repository.KeywordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KeywordService {
    private final KeywordRepository keywordRepository;

    @Autowired
    public KeywordService(KeywordRepository keywordRepository) {
        this.keywordRepository = keywordRepository;
    }

    public void saveKeyword(Keyword keyword){
        keywordRepository.save(keyword);
    }
}

package com.newscrawler.controller;

import com.newscrawler.entity.Keyword;
import com.newscrawler.service.KeywordService;
import com.newscrawler.util.Analysis.KeywordGenerator;
import com.newscrawler.util.Analysis.KeywordsExtractor;
import com.newscrawler.util.Analysis.TfidfCalculation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/keyword")
@Api(value = "keyword")
public class KeywordController {
    private static final Logger LOGGER = LoggerFactory.getLogger(KeywordGenerator.class);
    private KeywordGenerator keywordGenerator;
    private TfidfCalculation tfidfCalculation;
    private KeywordService keywordService;
    private KeywordsExtractor keywordsExtractor;

@Autowired
    public KeywordController(KeywordGenerator keywordGenerator, TfidfCalculation tfidfCalculation, KeywordService keywordService, KeywordsExtractor keywordsExtractor) {
        this.keywordGenerator = keywordGenerator;
        this.tfidfCalculation = tfidfCalculation;
        this.keywordService = keywordService;
    this.keywordsExtractor = keywordsExtractor;
}

    @ApiOperation(value = "Get Top-10 Keywords from News")
    @GetMapping("/topten")
    public void getTopFiveKeywords() throws IOException {
//        tfidfCalculation.saveTopFiveKeywords();
//        keywordGenerator.generateKeywordsMap();
        keywordsExtractor.saveTopTenKeywords();
    }

    @ApiOperation(value = "Gets keywords by newsId")
    @GetMapping("/{id}")
    public ResponseEntity<List<Keyword>> getTopFiveKeywords(@PathVariable Long id){
        List<Keyword> keywordList;
        try{
            keywordList=keywordService.findAllByNewsId(id);
        }
        catch (Exception e){
            LOGGER.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(keywordList);
    }
}
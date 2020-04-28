package com.newscrawler.controller;

import com.newscrawler.entity.Keyword;
import com.newscrawler.service.KeywordService;
import com.newscrawler.util.Analysis.KeywordGenerator;
import com.newscrawler.util.Analysis.TfidfCalculation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/keyword")
@Api(value = "keyword")
public class KeywordController {
    private static final Logger LOGGER = LoggerFactory.getLogger(KeywordGenerator.class);
    private KeywordGenerator keywordGenerator;
    private TfidfCalculation tfidfCalculation;
    private KeywordService keywordService;

@Autowired
    public KeywordController(KeywordGenerator keywordGenerator, TfidfCalculation tfidfCalculation, KeywordService keywordService) {
        this.keywordGenerator = keywordGenerator;
        this.tfidfCalculation = tfidfCalculation;
        this.keywordService = keywordService;
}

    @ApiOperation(value = "Get Top-5 Keywords from News")
    @GetMapping("/topfive")
    public void getTopFiveKeywords() {
        tfidfCalculation.saveTopFiveKeywords();
//        keywordGenerator.generateKeywordsMap();
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

package com.newscrawler.controller;

import com.newscrawler.util.Analysis.KeywordGenerator;
import com.newscrawler.util.Analysis.TfidfCalculation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;

@RestController
@RequestMapping("/keyword")
@Api(value = "keyword")
public class KeywordController {
    private KeywordGenerator keywordGenerator;
    private TfidfCalculation tfidfCalculation;

@Autowired
    public KeywordController(KeywordGenerator keywordGenerator, TfidfCalculation tfidfCalculation) {
        this.keywordGenerator = keywordGenerator;
        this.tfidfCalculation = tfidfCalculation;
    }

    @ApiOperation(value = "Get Top-5 Keywords from News")
    @GetMapping("/topfive")
    public void getTopFiveKeywords() {
        tfidfCalculation.saveTopFiveKeywords();
//        keywordGenerator.generateKeywordsMap();
    }
}

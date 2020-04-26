package com.newscrawler.controller;

import com.newscrawler.util.Analysis.KeywordGenerator;
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

    @Autowired
    public KeywordController(KeywordGenerator keywordGenerator){
        this.keywordGenerator= keywordGenerator;
    }

    @ApiOperation(value = "Generate Key Words of news")
    @GetMapping("/keywords")
    public void generateNewsKeywords() {
        keywordGenerator.generateKeywords();
    }
}

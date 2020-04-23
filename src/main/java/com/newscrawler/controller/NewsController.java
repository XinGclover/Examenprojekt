package com.newscrawler.controller;

import com.newscrawler.util.SVTCrawlerUtil;
import com.newscrawler.service.NewsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;

@RestController
@RequestMapping("/news")
@Api(value = "newscrawlerapi")
public class NewsController {
    private SVTCrawlerUtil svtCrawler;
    private NewsService newsService;

    @Autowired
    public NewsController(SVTCrawlerUtil basicCrawler, NewsService newsService) {
        this.svtCrawler = basicCrawler;
        this.newsService = newsService;
    }

    @ApiOperation(value = "Scape News from SVT")
    @GetMapping("/svt")
    public void pullSVTNews() throws MalformedURLException {
        svtCrawler.pullNews();
    }


}

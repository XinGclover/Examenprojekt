package com.newscrawler.controller;

import com.newscrawler.util.BasicCrawlerUtil;
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
    private BasicCrawlerUtil basicCrawler;
    private NewsService newsService;

    @Autowired
    public NewsController(BasicCrawlerUtil basicCrawler, NewsService newsService) {
        this.basicCrawler = basicCrawler;
        this.newsService = newsService;
    }

    @ApiOperation(value = "Scape News from SVT")
    @GetMapping("/svt")
    public void pullSVTNews() throws MalformedURLException {
        basicCrawler.pullNews();
    }


}

package com.newscrawler.controller;

import com.newscrawler.util.BBCCrawlerUtil;
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
    private BBCCrawlerUtil bbcCrawler;
    private NewsService newsService;

    @Autowired
    public NewsController(SVTCrawlerUtil svtCrawler,BBCCrawlerUtil bbcCrawler, NewsService newsService) {
        this.svtCrawler = svtCrawler;
        this.bbcCrawler = bbcCrawler;
        this.newsService = newsService;
    }

    @ApiOperation(value = "Scape News from SVT")
    @GetMapping("/svt")
    public void pullSVTNews() throws MalformedURLException {
        svtCrawler.pullNews();
    }

    @ApiOperation(value = "Scape News from BBC")
    @GetMapping("/bbc")
    public void pullBBCNews() throws MalformedURLException {
        bbcCrawler.pullNews();
    }


}

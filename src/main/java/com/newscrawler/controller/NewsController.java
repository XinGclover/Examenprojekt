package com.newscrawler.controller;

import com.newscrawler.util.Crawler.BBCCrawlerUtil;
import com.newscrawler.util.Crawler.SVTCrawlerUtil;
import com.newscrawler.service.NewsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;

@RestController
@RequestMapping("/news")
@Api(value = "news")
public class NewsController {
    private SVTCrawlerUtil svtCrawler;
    private BBCCrawlerUtil bbcCrawler;


    @Autowired
    public NewsController(SVTCrawlerUtil svtCrawler,BBCCrawlerUtil bbcCrawler) {
        this.svtCrawler = svtCrawler;
        this.bbcCrawler = bbcCrawler;

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

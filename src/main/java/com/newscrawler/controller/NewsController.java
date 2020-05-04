package com.newscrawler.controller;

import com.newscrawler.entity.News;
import com.newscrawler.util.Crawler.BBCCrawlerUtil;
import com.newscrawler.util.Crawler.SVTCrawlerUtil;
import com.newscrawler.service.NewsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.util.List;

@RestController
@RequestMapping("/news")
@Api(value = "news")
public class NewsController {
    private SVTCrawlerUtil svtCrawler;
    private BBCCrawlerUtil bbcCrawler;
    private NewsService newsService;


    @Autowired
    public NewsController(SVTCrawlerUtil svtCrawler, BBCCrawlerUtil bbcCrawler, NewsService newsService) {
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

    @ApiOperation(value = "Find News on BBC by Id")
    @GetMapping("/bbc/{id}")
    public ResponseEntity<News> getNewsById(@PathVariable Long id){
        News news= null;
        try {
            news= newsService.findNewsById(id);
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(news);
    }

    @ApiOperation(value = "Find All News on BBC")
    @GetMapping("/bbc/allnews")
    public ResponseEntity<List<News>> getAllNews(){
        List<News> newsList= newsService.findAllNews();
        return ResponseEntity.status(HttpStatus.OK).body(newsList);
    }

}

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

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

/**
 * Controller to handle web requests of get news
 */
@RestController
@RequestMapping("/news")
@Api(value = "news")
public class NewsController {
    private SVTCrawlerUtil svtCrawler;
    private BBCCrawlerUtil bbcCrawler;
    private NewsService newsService;


    /**
     * Constructor
     * @param svtCrawler instance of SVTCrawler
     * @param bbcCrawler instance of BBCCrawler
     * @param newsService instance of NewsService
     */
    @Autowired
    public NewsController(SVTCrawlerUtil svtCrawler, BBCCrawlerUtil bbcCrawler, NewsService newsService) {
        this.svtCrawler = svtCrawler;
        this.bbcCrawler = bbcCrawler;
        this.newsService = newsService;
    }

    /**
     * Scape news from SVT
     * @throws MalformedURLException Thrown to indicate that a malformed URL has occurred. Either no legal protocol could be found in a specification string or the string could not be parsed
     */
    @ApiOperation(value = "Scape News from SVT")
    @GetMapping("/svt")
    public void pullSVTNews() throws MalformedURLException {
        svtCrawler.pullNews();
    }

    /**
     * Scape news from BBC
     * @throws MalformedURLException Thrown to indicate that a malformed URL has occurred. Either no legal protocol could be found in a specification string or the string could not be parsed
     */
    @ApiOperation(value = "Scape News from BBC")
    @GetMapping("/bbc")
    public void pullBBCNews() throws IOException {
        bbcCrawler.pullNews();
    }

    /**
     * Find news by newsId
     * @param id newsId
     * @return Object of news
     */
    @ApiOperation(value = "Find News by Id")
    @GetMapping("/{id}")
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

    /**
     * Find all news
     * @return list of all the news
     */
    @ApiOperation(value = "Find All News")
    @GetMapping("/allnews")
    public ResponseEntity<List<News>> getAllNews(){
        List<News> newsList= newsService.findAllNews();
        return ResponseEntity.status(HttpStatus.OK).body(newsList);
    }

}

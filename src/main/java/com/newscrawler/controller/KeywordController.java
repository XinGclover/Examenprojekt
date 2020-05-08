package com.newscrawler.controller;

import com.newscrawler.entity.Keyword;
import com.newscrawler.service.KeywordService;
import com.newscrawler.util.Analysis.KeywordsExtractor;
import com.newscrawler.util.Visualization.KumoWordCloud;
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
import java.util.stream.Collectors;


/**
 * Controller that handle web requests about keywords
 */
@RestController
@RequestMapping("/keyword")
@Api(value = "keyword")
public class KeywordController {
    private static final Logger LOGGER = LoggerFactory.getLogger(KeywordController.class);
    private KeywordService keywordService;
    private KeywordsExtractor keywordsExtractor;
    private KumoWordCloud kumoWordCloud;

    /**
     * Constructor
     * @param keywordService object of KeywordService
     * @param keywordsExtractor object of KeywordExtractor
     * @param kumoWordCloud
     */
@Autowired
    public KeywordController(KeywordService keywordService, KeywordsExtractor keywordsExtractor, KumoWordCloud kumoWordCloud) {
        this.keywordService = keywordService;
        this.keywordsExtractor = keywordsExtractor;
        this.kumoWordCloud = kumoWordCloud;
}

    /**
     * Call the method to save top-10 keywords of all the news to database
     */
    @ApiOperation(value = "Save Keywords of News")
    @GetMapping("/allkeywords")
    public void getTopTenKeywords()  {
        keywordsExtractor.saveKeywords();
    }

    /**
     * Get top-10 keywords of news by newsId
     * @param id Id of news
     * @return List of top-10 keywords of this news
     */
    @ApiOperation(value = "Get keywords by newsId")
    @GetMapping("/topten/{id}")
    public ResponseEntity<List<Keyword>> getTopTenKeywords(@PathVariable Long id){
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

    /**
     * Prepare data of words of news by news Id for word cloud displaying
     * @param id Id of news
     * @return
     */
    @ApiOperation(value = "Get keywords by newsId")
    @GetMapping("/wordcloud/{id}")
    public ResponseEntity<String> getWordCloud(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(kumoWordCloud.getWordCloud(id));
    }

}

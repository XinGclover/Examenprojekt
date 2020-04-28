package com.newscrawler.util.Analysis;

import com.newscrawler.entity.Keyword;
import com.newscrawler.entity.News;
import com.newscrawler.service.KeywordService;
import com.newscrawler.service.NewsService;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.tartarus.snowball.ext.PorterStemmer;
import java.io.StringReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.lucene.analysis.en.EnglishAnalyzer.ENGLISH_STOP_WORDS_SET;


@Component
public class KeywordGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(KeywordGenerator.class);
    private final EnglishAnalyzer analyzer;
    private final PorterStemmer stemmer;
    private final NewsService newsService;
    private final KeywordService keywordService;


    public KeywordGenerator(NewsService newsService, KeywordService keywordService) {
        this.newsService = newsService;
        this.keywordService = keywordService;
        analyzer =  new EnglishAnalyzer(ENGLISH_STOP_WORDS_SET);
        stemmer = new PorterStemmer();
    }


    public Set<String> generateKeyWords(String content) {
        Set<String> keywords = new HashSet<>();
        TokenStream stream = analyzer.tokenStream("contents", new StringReader(content));
        try {
            stream.reset();
            while(stream.incrementToken()) {
                String kw = stream.getAttribute(CharTermAttribute.class).toString();
                if(!isDigit(kw)){
                    stemmer.setCurrent(kw);
                    stemmer.stem();
                    keywords.add(stemmer.getCurrent());
                }
            }
        }catch(Exception ex) {
            LOGGER.error(ex.getMessage());
        }finally {

            try {
                stream.end();
                stream.close();
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        }
        return keywords;
    }

    public  boolean isDigit(String input) {
        String regex = "(.)*(\\d)(.)*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        boolean isMatched = matcher.matches();
        if (isMatched) {
            return true;
        }
        return false;
    }

//    public void generateKeywords(){
//        List<News> newsList= newsService.findAll();
//        if(!newsList.isEmpty()) {
//            for (News news : newsList) {
//                Set<String> terms = generateKeyWords(news.getTitle());
//                for (String s : terms) {
//                    Keyword keyword = new Keyword();
//                    keyword.setNews(news);
//                    keyword.setTerm(s);
//                    keywordService.saveKeyword(keyword);
//                }
//            }
//        }
//        else {
//            LOGGER.error("No news in Database!");
//        }
//    }

    public Map<News,Set<String>> generateKeywordsMap(){
        List<News> newsList= newsService.findAllNews();
        System.out.println("newsList0000000 "+newsList.size());
        Map<News,Set<String>> newsKeywordsMap= new HashMap<>();
        if(!newsList.isEmpty()) {
            for (News news : newsList) {
                Set<String> terms = generateKeyWords(news.getContent());
                newsKeywordsMap.put(news,terms);
                System.out.println(news.getId()+" "+terms.size());
            }
        }
        else {
            LOGGER.error("No news in Database!");
        }
        return newsKeywordsMap;
    }

    public List<News> getallNews(){
        List<News> newsList= newsService.findAllNews();
        return newsList;
    }

}

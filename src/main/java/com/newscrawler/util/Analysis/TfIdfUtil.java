package com.newscrawler.util.Analysis;

import com.newscrawler.entity.KeywordNLP;
import com.newscrawler.entity.News;
import com.newscrawler.service.KeywordNLPService;
import com.newscrawler.service.NewsService;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Util to calculate TF-IDF of every lemma of words in news text and save lemmas of top-10 high score to database
 * @see CoreNLP
 */
@Component
public class TfIdfUtil {

    private Map<Long,List<String>> newsWordsMap;
    private Map<Long,Map<String,Integer>> newsTfMap;
    private Map<String,Double>  idfMap;
    private final NewsService newsService;
    private final CoreNLP coreNLP;
    private final KeywordNLPService keywordNLPService;

    /**
     * Constructor
     * @param newsService object of NewsService
     * @param coreNLP object of CoreNLP
     * @param keywordNLPService object of KeyWordNLPService
     */
    public TfIdfUtil(NewsService newsService, CoreNLP coreNLP, KeywordNLPService keywordNLPService) {
        this.newsService = newsService;
        this.coreNLP = coreNLP;
        this.keywordNLPService = keywordNLPService;
    }


    /**
     * Implement all steps of calculating, sort and save lemma with top-10 high score to database
     */
    public void eval(){
        this.splitWord();
        this.calTf();
        this.calIdf();
        for(News news: newsService.findAllNews()) {
            Map<String, Double> topTenofNews = calTfIdf().get(news.getId()).entrySet()
                    .stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .limit(10)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

            for (Map.Entry<String, Double> entry : topTenofNews.entrySet()) {
                KeywordNLP keywordNLP = new KeywordNLP();
                String lemma = entry.getKey();
                keywordNLP.setLemma(lemma);
                keywordNLP.setNews(news);
                keywordNLP.setTf(newsTfMap.get(news.getId()).get(lemma));
                keywordNLP.setTfIdf(entry.getValue());
                keywordNLPService.saveKeyword(keywordNLP);
            }
        }
    }


    /**
     * Create Map of news and list of lemmas, newsId as key, lemma list as value
     */
    private  void splitWord(){
        newsWordsMap = new HashMap<>();
        List<News> allnewsList= newsService.findAllNews();
        for(News news: allnewsList){
            newsWordsMap.put(news.getId(),coreNLP.nlpText(news.getId()));
        }

    }

    /**
     * Calculate TF of every words of all news, create a Map, newsId as key,Map of lemma and frequency as value
     */
    private void calTf() {
        newsTfMap = new HashMap<>();
        for( Map.Entry<Long,List<String>> entry: newsWordsMap.entrySet()) {
            Map<String,Integer> countMap = new HashMap<String,Integer>();
            for(String word : entry.getValue()) {
                if(countMap.containsKey(word)) {
                    countMap.put(word, countMap.get(word)+1);
                }else {
                    countMap.put(word, 1);
                }
            }
            newsTfMap.put(entry.getKey(),countMap);
        }
    }

    /**
     * Calculate IDF of every word of all news, create a Map, lemma as key, IDF value as value
     */
    private void calIdf() {
        int documentCount = newsWordsMap.size();
        idfMap = new HashMap<>();
        Map<String,Integer> wordAppearMap = new HashMap<>();
        for(Map<String,Integer> countMap : newsTfMap.values()  ) {
            for(String word : countMap.keySet()) {
                if(wordAppearMap.containsKey(word)) {
                    wordAppearMap.put(word, wordAppearMap.get(word)+1);
                }else {
                    wordAppearMap.put(word, 1);
                }
            }
        }
        for(String word : wordAppearMap.keySet()) {
            double idf = Math.log( documentCount  / (wordAppearMap.get(word)+1)  );
            idfMap.put(word, idf);
        }
    }

    /**
     * Calculate TF-IDF of every word of all news, create Map, newsId as key, Map of lemma and TF-IDF value as value.
     * @return Map of newId and lemma and TF-IDF value
     */
    private Map<Long,Map<String,Double>> calTfIdf() {
        Map<Long,Map<String,Double>> tfidfRes = new HashMap<>();
        for(Map.Entry<Long,Map<String,Integer>> entry : newsTfMap.entrySet() ) {
            Map<String,Double> tfIdfMap = new HashMap<>();
            for(String word : entry.getValue().keySet()) {
                int wordTF= entry.getValue().get(word);
                double tfidf = idfMap.get(word) * wordTF;
                tfIdfMap.put(word, tfidf);
            }
            tfidfRes.put(entry.getKey(),tfIdfMap);
        }
        return tfidfRes;
    }



}

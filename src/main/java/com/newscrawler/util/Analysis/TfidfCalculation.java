package com.newscrawler.util.Analysis;

import com.newscrawler.entity.Keyword;
import com.newscrawler.entity.News;
import com.newscrawler.service.KeywordService;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class TfidfCalculation {
    private final KeywordGenerator keywordGenerator;
    private final KeywordService keywordService;
    private final KeywordsExtractor keywordsExtractor;


    //存放（单词，单词数量）
    private HashMap<String, Integer> dict = new HashMap<String, Integer>();
    //存放（单词，单词词频）
    private HashMap<String, Float> tf = new HashMap<String, Float>();

    public TfidfCalculation(KeywordGenerator keywordGenerator, KeywordService keywordService, KeywordsExtractor keywordsExtractor) {
        this.keywordGenerator = keywordGenerator;
        this.keywordService = keywordService;
        this.keywordsExtractor = keywordsExtractor;
    }

    public HashMap<String,Float> calculateTermFrequency(Set<String> keywordsSet){
        int wordCount=0;

        /**
         * 统计每个单词的数量，并存放到map中去
         * 便于以后计算每个单词的词频
         * 单词的tf=该单词出现的数量n/总的单词数wordCount
         */
        for(String word:keywordsSet){
            wordCount++;
            if(dict.containsKey(word)){
                dict.put(word,  dict.get(word)+1);
            }else{
                dict.put(word, 1);
            }
        }
        for(Map.Entry<String, Integer> entry:dict.entrySet()){
//            float wordTf=(float)entry.getValue()/wordCount;
            float wordTf= (float)wordCount;
            tf.put(entry.getKey(), wordTf);
        }
        return tf;
    }


    public HashMap<String,Float> calculateInverseDocFrequency(int D, Map<News,Set<String>> newsKeywordsMap,Map<String,Float> tf){
        HashMap<String,Float> tfidf=new HashMap<>();
        for(String key:tf.keySet()){
            int Dt=0;
            for(Map.Entry<News, Set<String>> entry : newsKeywordsMap.entrySet()){
                Set<String > keywords= entry.getValue();
                if(keywords.contains(key)){
                    Dt++;
                }
            }
            float idfvalue=(float) Math.log(Float.valueOf(D)/Dt+1);
            tfidf.put(key, idfvalue * tf.get(key));

        }
        return tfidf;
    }

    public HashMap<String,Float> sortMap(Map<String,Float> keywordmap){
        HashMap<String,Float> sortedTFIDF= keywordmap.entrySet().stream()
                .sorted(Map.Entry
                .comparingByValue())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue,newValue)-> oldValue, LinkedHashMap::new));
        return sortedTFIDF;
    }

    public TreeMap<String,Float> getTopFiveKeywords(Map<String,Float> tfidfmap){
        TreeMap<String,Float> topFive= tfidfmap.entrySet().stream().limit(10)
                .collect(TreeMap::new,(m,e)->m.put(e.getKey(),e.getValue()),Map::putAll);
        return topFive;
    }

    public void saveTopFiveKeywords(){
        Map<News,Set<String>> newsKeywordsMap= keywordGenerator.generateKeywordsMap();
        for(Map.Entry<News,Set<String>> entry : newsKeywordsMap.entrySet()){
            News news= entry.getKey();
            HashMap<String,Float> tfmap= calculateTermFrequency(entry.getValue());
            HashMap<String,Float> tfidfmap= calculateInverseDocFrequency(newsKeywordsMap.size(),
                    newsKeywordsMap,
                    tfmap);
            TreeMap<String,Float> topFive= getTopFiveKeywords(sortMap(tfmap));
            List<Keyword> keywordList= new ArrayList<>();
            for(Map.Entry<String, Float> keywordEntry:topFive.entrySet()){
                Keyword keyword = new Keyword();
//                    keyword.setNews(news);
                    keyword.setStem(keywordEntry.getKey());
//                    keyword.setTfidf(keywordEntry.getValue());
//                    keywordService.saveKeyword(keyword);
//                    keywordList.add(keyword);
                System.out.println(keyword.getStem()+" "+keyword.getFrequency());
            }
            news.setKeywords(keywordList);
        }

    }

}

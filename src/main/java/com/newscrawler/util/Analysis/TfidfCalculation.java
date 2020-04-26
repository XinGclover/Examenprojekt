package com.newscrawler.util.Analysis;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TfidfCalculation {
    //存放（单词，单词数量）
    HashMap<String, Integer> dict = new HashMap<String, Integer>();
    //存放（单词，单词词频）
    HashMap<String, Float> tf = new HashMap<String, Float>();
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
            float wordTf=(float)entry.getValue()/wordCount;
            tf.put(entry.getKey(), wordTf);
        }
        return tf;
    }


    public Map<String,Float> calculateInverseDocFrequency(int D, Map<Long,Set<String>> news_keywords,Map<String,Float> tf){
        HashMap<String,Float> tfidf=new HashMap<String, Float>();
        for(String key:tf.keySet()){
            int Dt=0;
            for(Map.Entry<Long, Set<String>> entry:news_keywords.entrySet()){
                Set<String > keywords= entry.getValue();
                if(keywords.contains(key)){
                    Dt++;
                }
            }
            float idfvalue=(float) Math.log(Float.valueOf(D)/Dt);
            tfidf.put(key, idfvalue * tf.get(key));

        }
        return tfidf;
    }

    public Map<String,Float> sortFirstFiveKeywords(){
        
    }

}

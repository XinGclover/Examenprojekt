package com.newscrawler.util.Analysis;

import com.newscrawler.entity.News;
import com.newscrawler.service.NewsService;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import org.springframework.stereotype.Component;
import java.util.*;
import static org.apache.lucene.analysis.en.EnglishAnalyzer.ENGLISH_STOP_WORDS_SET;

/**
 * Implement Stanford CoreNLP pipeline to get lemma list of words in text
 */
@Component
public class CoreNLP {

    private final NewsService newsService;

    /**
     * Constructor
     * @param newsService object of NewsService
     */
    public CoreNLP(NewsService newsService) {
        this.newsService = newsService;
    }

    /**
     * Implement Stanford CoreNLP pipeline to get list of lemma of words of a news text
     * creates a StanfordCoreNLP object, with POS tagging, lemmatization,NER,parsing,and coreference resolution
     * remove punctuation marks in text, run all Annotators on this text
     * create Annotators on text
     * get lemma of words and save to list except those belongs to stopwords
     * @param newsId Id of the news
     * @return List of words of lemma form after lemmatization
     */
    public List<String> nlpText(Long newsId) {
        List<String> wordList = new ArrayList<>();
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

       News news= new News();
        try{
            news=newsService.findNewsById(newsId);
        }catch (Exception e){
            e.printStackTrace();
        }
        String text= news.getContent();
        text = text.replaceAll("-+", "-0");
        text = text.replaceAll("[\\p{Punct}&&[^'-]]+", " ");
        text = text.replaceAll("(?:'(?:[tdsm]|[vr]e|ll))+\\b", "");
        Annotation document = new Annotation(text);
        pipeline.annotate(document);
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);

        for (CoreMap sentence : sentences) {
            for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
                String lemma = token.get(LemmaAnnotation.class);
                if(!convertCharSetToCollection().contains(lemma)){
                    wordList.add(lemma);
                }
            }

        }return  wordList;

    }


    /**
     * Convert a Lucene CharArraySet ENGLISH_STOP_WORDS_SET to Set<String>
     * @return the Set<String> of ENGLISH_STOP_WORDS_SET
     */
    public Set<String> convertCharSetToCollection(){
        Set<String> stopWords = new HashSet<>();
        Iterator iter = ENGLISH_STOP_WORDS_SET.iterator();
        while(iter.hasNext()) {
            char[] stopWord = (char[]) iter.next();
            stopWords.add(new String (stopWord));
        }
        return stopWords;
    }


}



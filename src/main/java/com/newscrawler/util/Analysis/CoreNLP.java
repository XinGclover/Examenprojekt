package com.newscrawler.util.Analysis;

import com.newscrawler.entity.News;
import com.newscrawler.service.NewsService;
import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefChainAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.util.CoreMap;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.lucene.analysis.en.EnglishAnalyzer.ENGLISH_STOP_WORDS_SET;

@Component
public class CoreNLP {

    private final NewsService newsService;

    public CoreNLP(NewsService newsService) {
        this.newsService = newsService;
    }

    public List<String> nlpText(Long newsId) {

        List<String> wordList = new ArrayList<String>();
        /**
         * Generate a StanfordCoreNLP object
         * tokenize,ssplit, pos、lemma
         * ner,parse,
         */

        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

//        String text = "A fire has engulfed a skyscraper in Sharjah, one of the largest cities in the United Arab Emirates, showering debris on cars in the streets below. Dozens of firefighters worked to put out the blaze in the Abbco Tower using at least a dozen fire engines and drones, a local journalist reports. They are now trying to cool the 48-floor building down. There were no immediate reports of casualties or of the likely cause of the fire. It took several drones, at least a dozen fire trucks, and scores of firefighters but authorities finally put out a massive #fire that ravaged the 40-floor Abbco Tower in #AlNahda #Sharjah. Cooling ops underway Video: Fire breaks out in Sharjah building https://t.co/8f4ND1H1z8 pic.twitter.com/gEDRRtOlV0 End of Twitter post by @vickykapur Emergency services were called to the scene shortly after 21:00 (17:00 GMT) on Tuesday. At least five buildings nearby were evacuated as the fire service worked at the scene, the Dubai-based Khaleej Times reports.";               // 输入文本
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
        System.out.println("word\tpos\tlemma\tner");

        for (CoreMap sentence : sentences) {
            for (CoreLabel token : sentence.get(TokensAnnotation.class)) {

                String word = token.get(TextAnnotation.class);

                String lemma = token.get(LemmaAnnotation.class);
                System.out.println(lemma);
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



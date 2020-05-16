package com.newscrawler.util.Analysis;


import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class CoreNLPTest {

    @Test
    void nlpText() {
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        List<String> lemmaList= new ArrayList<>();
        String text = "Barack Obama was born in Hawaii.";
        Annotation document = new Annotation(text);
        pipeline.annotate(document);
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        for (CoreMap sentence : sentences) {
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                String lemma = token.get(CoreAnnotations.LemmaAnnotation.class);
                lemmaList.add(lemma);
            }
        }
        assertEquals("Barack",lemmaList.get(0));
        assertEquals("Obama",lemmaList.get(1));
        assertEquals("be",lemmaList.get(2));
        assertEquals("bear",lemmaList.get(3));
    }
}
package com.newscrawler.util.Analysis;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class KeywordsExtractorTest {

    @Test
    void getStemForm() throws IOException {
        String term = "worlds";
        TokenStream tokenStream;
        StandardTokenizer stdToken = new StandardTokenizer();
        stdToken.setReader(new StringReader(term));
        tokenStream = new PorterStemFilter(stdToken);
        tokenStream.reset();
        CharTermAttribute token = tokenStream.getAttribute(CharTermAttribute.class);
        while (tokenStream.incrementToken()) {
            assertEquals("world", token.toString());
        }
        }


}
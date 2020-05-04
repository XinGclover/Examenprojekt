package com.newscrawler.util.Analysis;

import com.newscrawler.entity.Keyword;
import com.newscrawler.entity.News;
import com.newscrawler.service.KeywordService;
import com.newscrawler.service.NewsService;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.tartarus.snowball.ext.PorterStemmer;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.lucene.analysis.en.EnglishAnalyzer.ENGLISH_STOP_WORDS_SET;

/**
 * Keywords extractor functionality handler
 */
@Component
public class KeywordsExtractor {
    private static final Logger LOGGER = LoggerFactory.getLogger(KeywordsExtractor.class);
    private final NewsService newsService;
    private final KeywordService keywordService;
    private final EnglishAnalyzer analyzer;
    private final PorterStemmer stemmer;

    public KeywordsExtractor(NewsService newsService, KeywordService keywordService) {
        this.newsService = newsService;
        this.keywordService = keywordService;
        analyzer =  new EnglishAnalyzer(ENGLISH_STOP_WORDS_SET);
        stemmer = new PorterStemmer();
    }

    /**
     * Get list of keywords with stem form, frequency rank, and terms dictionary
     *
     * @param content
     * @return List<CardKeyword>, which contains keywords cards
     * @throws IOException
     */
    public List<Keyword> getKeywordsList(String content) throws IOException {

        TokenStream stream = null;

        try {
            // treat the dashed words, don't let separate them during the processing
            content = content.replaceAll("-+", "-0");

            // replace any punctuation char but apostrophes and dashes with a space
            content = content.replaceAll("[\\p{Punct}&&[^'-]]+", " ");

            // replace most common English contractions
            content = content.replaceAll("(?:'(?:[tdsm]|[vr]e|ll))+\\b", "");

            stream = analyzer.tokenStream("contents", new StringReader(content));
            stream.reset();

            List<Keyword> Keywords = new LinkedList<>();

            CharTermAttribute token = stream.getAttribute(CharTermAttribute.class);

            while (stream.incrementToken()) {

                String term = token.toString();
                String stem = getStemForm(term);

                if (stem != null) {
                   Keyword cardKeyword = find(Keywords, new Keyword(stem.replaceAll("-0", "-")));
                    // treat the dashed words back, let look them pretty
                    cardKeyword.add(term.replaceAll("-0", "-"));
                }
            }

            // reverse sort by frequency
            Collections.sort(Keywords);

            return Keywords;
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Get stem form of the term
     *
     * @param term
     * @return String, which contains the stemmed form of the term
     * @throws IOException
     */
    public String getStemForm(String term) throws IOException {

        TokenStream tokenStream = null;

        try {
            StandardTokenizer stdToken = new StandardTokenizer();
            stdToken.setReader(new StringReader(term));

            tokenStream = new PorterStemFilter(stdToken);
            tokenStream.reset();

            // eliminate duplicate tokens by adding them to a set
            Set<String> stems = new HashSet<>();

            CharTermAttribute token = tokenStream.getAttribute(CharTermAttribute.class);

            while (tokenStream.incrementToken()) {
                stems.add(token.toString());
            }

            // if stem form was not found or more than 2 stems have been found, return null
            if (stems.size() != 1) {
                return null;
            }

            String stem = stems.iterator().next();

            // if the stem form has non-alphanumerical chars, return null
            if (!stem.matches("[a-zA-Z0-9-]+")) {
                return null;
            }

            return stem;
        } finally {
            if (tokenStream != null) {
                try {
                    tokenStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Find sample in collection
     *
     * @param collection
     * @param sample
     * @param <T>
     * @return <T> T, which contains the found object within collection if exists, otherwise the initially searched object
     */
    public <T> T find(Collection<T> collection, T sample) {

        for (T element : collection) {
            if (element.equals(sample)) {
                return element;
            }
        }
        collection.add(sample);

        return sample;
    }
    
    public List<Keyword> getTopTenKeywords(List<Keyword> keywordList){
       return keywordList.stream().limit(10).collect(Collectors.toList());
    }

    public void saveTopTenKeywords() throws IOException {
        List<News> newsList= newsService.findAllNews();
        for(News news:newsList){
            List<Keyword> topTenKeywords= getTopTenKeywords(getKeywordsList(news.getContent()));
            List<Keyword> keywordList= new ArrayList<>();
            for(Keyword keyword:topTenKeywords){
                keyword.setNews(news);
                keywordService.saveKeyword(keyword);
                System.out.println(news.getId()+" "+keyword.getStem()+" "+keyword.getFrequency());
            }
            news.setKeywords(keywordList);
        }

    }


}

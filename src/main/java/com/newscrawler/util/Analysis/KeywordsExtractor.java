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
 * Keywords extractor functional handler.
 * Use EnglishAnalyzer of Apache Lucene to convert text to terms then sort list of keywords with stem form by frequency.
 * @author Xin Gao
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
     * @param content content of a news uesed to extract keywords
     * @return List<Keyword> which contains keywords
     */
    public List<Keyword> getKeywordsList(String content)  {

        TokenStream stream = null;

        try {
            content = content.replaceAll("-+", "-0");
            content = content.replaceAll("[\\p{Punct}&&[^'-]]+", " ");
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
                    cardKeyword.add(term.replaceAll("-0", "-"));
                }
            }

            Collections.sort(Keywords);
            return Keywords;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }return null;
    }

    /**
     * Get stem form of the term
     * @param term the most fundamental indexed representation of text
     * @return String, which contains the stemmed form of the term
     */
    public String getStemForm(String term)  {
        TokenStream tokenStream = null;
        try {
            StandardTokenizer stdToken = new StandardTokenizer();
            stdToken.setReader(new StringReader(term));
            tokenStream = new PorterStemFilter(stdToken);
            tokenStream.reset();
            Set<String> stems = new HashSet<>();
            CharTermAttribute token = tokenStream.getAttribute(CharTermAttribute.class);

            while (tokenStream.incrementToken()) {
                stems.add(token.toString());
            }
            if (stems.size() != 1) {
                return null;
            }

            String stem = stems.iterator().next();

            // if the stem form has non-alphanumerical chars, return null
            if (!stem.matches("[a-zA-Z0-9-]+")) {
                return null;
            }

            return stem;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (tokenStream != null) {
                try {
                    tokenStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }return null;
    }

    /**
     * Find sample in collection
     * @param collection object of Collection
     * @param sample object of T
     * @param <T> the type of elements in this collection
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

    /**
     * Get the first 10 elements of a list
     * @param keywordList list of all keywords of a news
     * @return the list of keywords with top-10 frequency
     */
    public List<Keyword> getTopTenKeywords(List<Keyword> keywordList){
       return keywordList.stream().limit(10).collect(Collectors.toList());
    }

    /**
     * Save the Top-10 terms of a news in database
     */
    public void saveTopTenKeywords() {
        List<News> newsList= newsService.findAllNews();
        for(News news:newsList){
            List<Keyword> topTenKeywords= getTopTenKeywords(getKeywordsList(news.getContent()));
            for(Keyword keyword:topTenKeywords){
                keyword.setNews(news);
                keywordService.saveKeyword(keyword);
                System.out.println(news.getId()+" "+keyword.getStem()+" "+keyword.getFrequency());
            }
            news.setKeywords(topTenKeywords);
        }

    }


}

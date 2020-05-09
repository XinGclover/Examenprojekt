package com.newscrawler.util.Visualization;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.nlp.tokenizers.EnglishWordTokenizer;
import com.kennycason.kumo.palette.ColorPalette;
import com.newscrawler.entity.News;
import com.newscrawler.service.NewsService;
import org.springframework.stereotype.Component;
import static org.apache.lucene.analysis.en.EnglishAnalyzer.ENGLISH_STOP_WORDS_SET;

import java.awt.*;
import java.io.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Implement Kumo Word Cloud API in Java.
 * Kumo directly generates an image file without the need to create an applet as many other libraries do.
 */
@Component
public class KumoWordCloud {
    private final NewsService newsService;

    /**
     * Constructor of KumoWordCloud
     * @param newsService instance of NewsService
     */
    public KumoWordCloud(NewsService newsService) {
        this.newsService = newsService;
    }

    /**
     * Creat a image of word cloud of a news by newsId
     * The process includes some fundamental text analysis,such as tokenization and omitting stopwords,
     * and the setting of image
     * @param newsId Id of a news, its content is to be displayed as a word cloud
     * @return the String of Base64 encoding converted by image
     */
    public String getWordCloud(Long newsId) {
        FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordTokenizer(new EnglishWordTokenizer());
        frequencyAnalyzer.setStopWords(convertCharSetToCollection());
        News news= new News();
        try{
            news=newsService.findNewsById(newsId);
        }catch (Exception e){
            e.printStackTrace();
        }
        InputStream inputStream= new ByteArrayInputStream(news.getContent().getBytes());
        List<WordFrequency> wordFrequencyList = null;
        try {
            wordFrequencyList = frequencyAnalyzer.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Dimension dimension = new Dimension(500, 500);
        WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        java.awt.Font font = new java.awt.Font("STSong-Light", 2, 18);
        wordCloud.setKumoFont(new KumoFont(font));
        wordCloud.setPadding(2);
        wordCloud.setColorPalette(new ColorPalette(new Color(0xed1941), new Color(0xf26522), new Color(0x845538),new Color(0x8a5d19),new Color(0x7f7522),new Color(0x5c7a29),new Color(0x1d953f),new Color(0x007d65),new Color(0x65c294)));
        wordCloud.setBackground(new CircleBackground(200));
        wordCloud.setFontScalar(new SqrtFontScalar(10, 40));
        wordCloud.setBackgroundColor(new Color(255, 255, 255));
        wordCloud.build(wordFrequencyList);
        OutputStream output = new ByteArrayOutputStream();
        wordCloud.writeToStream("png", output);
        byte[] outputByte = ((ByteArrayOutputStream)output).toByteArray();
        return org.apache.commons.codec.binary.Base64.encodeBase64String(outputByte);
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

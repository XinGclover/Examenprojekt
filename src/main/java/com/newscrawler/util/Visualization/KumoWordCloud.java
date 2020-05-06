package com.newscrawler.util.Visualization;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.palette.ColorPalette;
import com.newscrawler.entity.Keyword;
import com.newscrawler.service.KeywordService;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class KumoWordCloud {
    private final KeywordService keywordService;

    public KumoWordCloud(KeywordService keywordService) {
        this.keywordService = keywordService;
    }

    public String getWordCloud(Long newsId) {
        List<String> words= keywordService.findAllByNewsId(newsId).stream().map(keyword -> keyword.getStem()).collect(Collectors.toList());
        System.out.println("words ======"+ words.size());
        FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        List<WordFrequency> wordFrequencyList = frequencyAnalyzer.load(words);
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


}

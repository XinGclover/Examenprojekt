package com.newscrawler.util.Visualization;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.font.scale.LinearFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import org.junit.jupiter.api.Test;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


class KumoWordCloudTest {

    @Test
    void getWordCloud() throws IOException {

        List<WordFrequency> wordFrequencies = new ArrayList<>();
        Dimension dimension = new Dimension(600, 600);
        WordCloud wordCloud = new WordCloud(dimension, CollisionMode.RECTANGLE);
        FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        wordFrequencies= frequencyAnalyzer.load("/Users/xingao/Documents/Nackademin/Examenarbete/Mythesis/MyProject/NewsCrawler/newscrawler/test.txt");
        wordCloud.setPadding(0);
        wordCloud.setBackground(new CircleBackground(300));
        wordCloud.setFontScalar(new LinearFontScalar(10, 40));
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile("/Users/xingao/Documents/Nackademin/Examenarbete/Mythesis/MyProject/NewsCrawler/newscrawler/wordcloud_circle.png");

      }
    }



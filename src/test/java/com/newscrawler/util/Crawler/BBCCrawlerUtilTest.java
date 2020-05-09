package com.newscrawler.util.Crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;


import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class BBCCrawlerUtilTest {

    @Test
    void pullNews() throws IOException {
        File input= new File("/Users/xingao/Documents/Nackademin/Examenarbete/Mythesis/MyProject/NewsCrawler/newscrawler/BBC.html");
        String html= Jsoup.parse(input,"UTF-8").toString();
        Document doc= Jsoup.parse(html);
        Elements elements= doc.select("div#site-container");
        assertTrue(elements.size()>0);

    }


}
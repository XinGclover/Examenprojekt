package com.newscrawler.util;

import com.newscrawler.entity.News;
import com.newscrawler.service.NewsService;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashSet;


@Component
public class BBCCrawlerUtil implements BasicCrawler{
    @Autowired
    private NewsService newsService;
    private String baseUrl= "https://www.bbc.com/news";

    //fetch and parse a HTML document from the web
    @Override
    public void pullNews() throws MalformedURLException {
        Document document= null;
        try{
            document= getHtmlFromUrl(baseUrl);
            BufferedWriter writer = null;
            try
            {
                writer = new BufferedWriter( new FileWriter("/Users/xingao/Documents/Nackademin/Examenarbete/Mythesis/MyProject/NewsCrawler/newscrawler/BBC.txt"));
                writer.write(document.toString());
            }
            catch ( IOException e)
            {
                System.out.println("Can not save to file!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements hrefElements = document.select("div#site-container").select("a[href]");

        HashSet<String> urlSet = new HashSet<>();

        for(Element e :hrefElements ){
            String url = e.attr("href");
            if(url.contains("news")&&url.matches(".*\\d+.*")){
                if(url.contains("http")){
                    urlSet.add(url);
                }else{
                    URL absoluteUrl= new URL(new URL("https://www.bbc.com"),url);
                    urlSet.add(absoluteUrl.toString());
                }
            }
        }

        urlSet.forEach(url->{
//          System.out.println("==========news url =========="+url);
            News news= new News();
            Document newsHtml = null;
            try{
                newsHtml= getHtmlFromUrl(url);
                Element newsContent = newsHtml.select("div.container").first();
                if(newsContent!=null) {
                    Element titleElement = newsContent.select("h1.story-body__h1").first();
                    String title = null;
                    if (titleElement != null) {
                        title = titleElement.text();
                    } else {
                        System.out.println("No News Title!");
                    }
                    String content = newsContent.select("div.story-body__inner").select("p").text();
                    String newsTime= newsContent.select("div").attr("data-datetime");
                    news.setUrl(url);
                    news.setTitle(title);
                    news.setContent(content);
                    news.setSource("BBC");
                    news.setCreateDate(new Date());
                    news.setNewsDate(newsTime);
                    newsService.saveNews(news);
                }
                else {
                    System.out.println("There is no article!");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }


}

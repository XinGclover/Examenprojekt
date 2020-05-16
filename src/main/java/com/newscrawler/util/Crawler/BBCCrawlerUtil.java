package com.newscrawler.util.Crawler;

import com.newscrawler.entity.News;
import com.newscrawler.service.NewsService;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Specialized scraping for BBC, obtain data from requied elements on BBC.
 * Use Jsoup to select necessary elements from document and fetch text or attribute from those elements.
 * @see BasicCrawler
 */
@Component
public class BBCCrawlerUtil implements BasicCrawler{
    @Autowired
    private NewsService newsService;
    private String baseUrl= "https://www.bbc.com/news";
    private static final Logger LOGGER = LoggerFactory.getLogger(BBCCrawlerUtil.class);
    private int n=0;

    /**
     * Fetching data from requied elements and call newsservice to save data as entity to database
     * @throws IOException
     */
    @Override
    public int pullNews() throws IOException {
        Document document= null;
        document= getHtmlFromUrl(baseUrl);
        saveDocumentAsFile(document,"/Users/xingao/Documents/Nackademin/Examenarbete/Mythesis/MyProject/NewsCrawler/newscrawler/BBC.html");
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
                    n++;
                }
                else {
                    LOGGER.error("There is no article!");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        return n;
    }


    /**
     * Save parsed html document to a text file
     * @param document html document parsed from Jsoup
     * @param fileName fileName that used to save parsed html ducument
     */
    public void saveDocumentAsFile(Document document, String fileName){
        BufferedWriter writer = null;
        try
        {
            writer = new BufferedWriter( new FileWriter(fileName));
            writer.write(document.toString());
        }
        catch ( IOException e)
        {
            LOGGER.error("Can not save to file!");
        }
    }


}

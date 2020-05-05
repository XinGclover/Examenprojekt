package com.newscrawler.util.Crawler;
import com.newscrawler.entity.News;
import com.newscrawler.service.NewsService;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashSet;

/**
 * Specialized scraping for SVT, obtain data from requied elements on BBC
 * @see BasicCrawler
 */
@Component
public class SVTCrawlerUtil implements BasicCrawler{
    @Autowired
    private NewsService newsService;
    private String baseUrl= "https://www.svt.se";

    /**
     * Fetching data from requied elements and call newsservice to save data as entity to database
     * @throws MalformedURLException
     * @see BasicCrawler
     */
    @Override
    public void pullNews() throws MalformedURLException {
        Document document= null;
        try{
            document= getHtmlFromUrl(baseUrl);
            BufferedWriter writer = null;
            try
            {
                writer = new BufferedWriter( new FileWriter("/Users/xingao/Documents/Nackademin/Examenarbete/Mythesis/MyProject/NewsCrawler/newscrawler/SVT.txt"));
                writer.write(document.toString());
            }
            catch ( IOException e)
            {
                System.out.println("Can not save to file!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements hrefElements = document.select("main.nyh_body")
                .select("li")
                .select("a[href]");
        HashSet<String> urlSet = new HashSet<>();

        for(Element e :hrefElements ){
            String url = e.attr("href");
            if(url.contains("http")){
                urlSet.add(url);
            }else{
                URL absoluteUrl= new URL(new URL(baseUrl),url);
                urlSet.add(absoluteUrl.toString());
            }
        }

        urlSet.forEach(url->{
            News news= new News();
            Document newsHtml = null;
            try{
                newsHtml= getHtmlFromUrl(url);
                Element newsContent = newsHtml.select("article").first();
                if(newsContent!=null) {
                    Element titleElement = newsContent.select("h1.nyh_article__heading").first();
                    String title = null;
                    if (titleElement != null) {
                        title = titleElement.text();
                    } else {
                        System.out.println("No News Title!");
                    }
                    String content = newsContent.select("div.nyh_article__main").select("p").text();
                    String newsTime= newsContent.select("time").attr("datetime");
                    news.setUrl(url);
                    news.setTitle(title);
                    news.setContent(content);
                    news.setSource("SVT");
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

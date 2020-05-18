package com.newscrawler.util.Crawler;
import com.newscrawler.entity.News;
import com.newscrawler.service.NewsService;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.HashSet;

/**
 * Specialized scraping for SVT, obtain data from requied elements on SVT
 * Use Jsoup to select necessary elements from document and fetch text or attribute from those elements.
 * @see BasicCrawler
 */
@Component
public class SVTCrawlerUtil implements BasicCrawler{
    @Autowired
    private NewsService newsService;
    private String baseUrl= "https://www.svt.se";
    private int n=0;
    private HashSet<String> urlSet;


    /**
     * Get Set of URL containing "news" and change relative URLs to absolute URLs
     * @throws IOException
     */
    @Override
    public void pullNews() throws IOException {
        Document document = null;
        try {
            document = getHtmlFromUrl(baseUrl);
            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter("/Users/xingao/Documents/Nackademin/Examenarbete/Mythesis/MyProject/NewsCrawler/newscrawler/SVT.txt"));
                writer.write(document.toString());
            } catch (IOException e) {
                System.out.println("Can not save to file!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements hrefElements = document.select("main.nyh_body")
                .select("li")
                .select("a[href]");
      urlSet = new HashSet<>();

        for (Element e : hrefElements) {
            String url = e.attr("href");
            if (url.contains("nyheter") && url.matches(".*\\d+.*")) {
                if (url.contains("http")) {
                    urlSet.add(url);
                } else {
                    URL absoluteUrl = new URL(new URL("https://www.svt.se"), url);
                    urlSet.add(absoluteUrl.toString());
                }
            }
        }
    }

    /**
     * Loop Set of URLS of news and fetch data from requied elements and call newsservice to save data as entity to database
     * @return the quantity of news saved
     */
    public int saveSVTNews() throws IOException {
        pullNews();
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
                    n++;
                }
                else {
                    System.out.println("There is no article!");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        });return n;
    }


}

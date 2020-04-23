package com.newscrawler.util;
import com.newscrawler.entity.News;
import com.newscrawler.service.NewsService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
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

@Component
public class SVTCrawlerUtil {
    @Autowired
    private NewsService newsService;

    String baseUrl= "https://www.svt.se";

    public Document  getHtmlFromUrl(String url) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
//        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
        request.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.9; rv:50.0) Gecko/20100101 Firefox/50.0");
        request.setHeader("Connection","keep-alive");
        request.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        request.setHeader("Accept-Encoding", "gzip, deflate, br");
        request.setHeader("Accept-Language", "sv;q=0.9,en;q=0.5");
        request.setHeader("Cache-Control", "no-cache, no-store");
        request.setHeader("Upgrade-Insecure-Requests", "1");
//        request.setHeader("DNT", "1");
        request.setHeader("Pragma", "no-cache");
        CloseableHttpResponse response = null;


        try {
            response = httpclient.execute(request);
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300){
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream inputStream = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder stringBuilder = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line + "\n");
                    }
                    inputStream.close();
                    String responseMessage = stringBuilder.toString();
                    //Jsoup parse a document from a String
                    Document document= Jsoup.parse(responseMessage);
                    return document;
                }else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            }
        }finally {
            httpclient.close();
        }return null;
    }

   //fetch and parse a HTML document from the web
    public void pullNews() throws MalformedURLException {
        Document document= null;
        try{
            document= getHtmlFromUrl(baseUrl);
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
//          System.out.println("==========news url =========="+url);
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
                    news.setUrl(url);
                    news.setTitle(title);
                    news.setContent(content);
                    news.setSource("SVT");
                    news.setCreateDate(new Date());
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

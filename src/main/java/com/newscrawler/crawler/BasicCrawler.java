package com.newscrawler.crawler;
import com.newscrawler.entity.News;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.*;
import java.util.Date;
import java.util.HashSet;
import java.util.logging.Logger;


public class BasicCrawler {
    public Document  getHtmlFromUrl() throws IOException {
        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建http GET请求
        HttpGet httpGet = new HttpGet("http://www.svt.se");
        CloseableHttpResponse response = null;
        HashSet<News> newSet = new HashSet<>();

        try {
            // 执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
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
                    return Jsoup.parse(responseMessage);
                }
            }
        }finally {
            if (response != null) {
                response.close();
            }
            //相当于关闭浏览器
            httpclient.close();
        }return null;
    }

    public void pullNews(){
        Document document= null;
        try{
            document= getHtmlFromUrl();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements hrefElements = document.select("a[href]");
        HashSet<News> newsSet = new HashSet<>();

        for(Element a :hrefElements ){
            String url = a.attr("href");
            String title = a.attr("title");
            News news= new News();
            news.setUrl(url);
            news.setTitle(title);
            news.setCreateDate(new Date());
            newsSet.add(news);
        }
    }


}

package com.newscrawler.util.Crawler;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;

public interface BasicCrawler {
    void pullNews() throws MalformedURLException;

    default Document  getHtmlFromUrl(String url) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
//        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
        request.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.9; rv:50.0) Gecko/20100101 Firefox/50.0");
        request.setHeader("Connection","keep-alive");
        request.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        request.setHeader("Accept-Encoding", "gzip, deflate, br");
        request.setHeader("Accept-Language", "sv;q=0.5,en;q=0.5");
        request.setHeader("Cache-Control", "no-cache, no-store");
        request.setHeader("Upgrade-Insecure-Requests", "1");
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
}

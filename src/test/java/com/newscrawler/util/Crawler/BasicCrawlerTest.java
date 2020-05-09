package com.newscrawler.util.Crawler;

import junit.framework.Assert;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.io.IOException;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class BasicCrawlerTest {
    @Mock
   protected HttpClient mockHttpClient;
    @Mock
   protected HttpGet mockHttpGet;
    @Mock
   protected HttpResponse mockHttpResponse;
    @Mock
   protected StatusLine mockStatusLine;
    @Mock
   protected HttpEntity mockHttpEntity;

   @Before
   public void setup(){
       MockitoAnnotations.initMocks(this);

   }
    @Test
    void getHtmlFromUrl() throws IOException {
        Mockito.when(mockHttpClient.execute(mockHttpGet)).thenReturn(mockHttpResponse);
        Mockito.when(mockHttpResponse.getStatusLine()).thenReturn(mockStatusLine);
        Mockito.when(mockStatusLine.getStatusCode()).thenReturn(200);
        Mockito.when(mockHttpResponse.getEntity()).thenReturn(mockHttpEntity);

        StatusApiClient client = new StatusApiClient(mockHttpClient,mockHttpGet);

        boolean status= client.getStatus();
        Assert.assertTrue(status);
      }

}
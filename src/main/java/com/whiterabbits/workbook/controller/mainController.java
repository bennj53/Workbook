package com.whiterabbits.workbook.controller;

import java.util.Date;

import java.io.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

@RestController
public class mainController {

    @GetMapping("/api/hello")
    public String hello() {
        return "Hello all, the time at the server is now " + new Date() + "\n";
    }

    // passage du pays directement dans l url --> PathVariable
    // @GetMapping(path = "/api/meteo/{country}")
    // public String getMeteo(@PathVariable String country) {
    @GetMapping(path = "/api/meteo")
    public String getMeteo() {
        // TODO : à supprimer remplace par le param de l url
        String country = "London,uk";

        String key = "28531ee195c03fe08022bf0fd763c310";
        HttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpGet httpGetRequest = new HttpGet(
                    "http://api.openweathermap.org/data/2.5/weather?q=" + country + "&APPID=" + key);
            HttpResponse httpResponse = httpClient.execute(httpGetRequest);

            System.out.println("----------------------------------------");
            System.out.println(httpResponse.getStatusLine());
            System.out.println("----------------------------------------");

            HttpEntity entity = httpResponse.getEntity();

            byte[] buffer = new byte[1024];
            if (entity != null) {
                InputStream inputStream = entity.getContent();
                try {
                    int bytesRead = 0;
                    BufferedInputStream bis = new BufferedInputStream(inputStream);
                    while ((bytesRead = bis.read(buffer)) != -1) {
                        String chunk = new String(buffer, 0, bytesRead);
                        System.out.println(chunk);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        inputStream.close();
                    } catch (Exception ignore) {
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        return "";
    }

}
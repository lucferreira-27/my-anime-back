package com.lucferreira.myanimeback.model;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.lucferreira.myanimeback.model.snapshot.Timestamp;

public class InstantArchiveRequest {
    private List<String> urls;

    private String malUrl;

    // Getters and Setters
    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public String getMalUrl() {
        return malUrl;
    }

    public void setMalUrl(String malUrl) {
        this.malUrl = malUrl;
    }
    public List<Timestamp> getUrlTimestamps(){
        if(urls.isEmpty()){
            List.of();
        }

        return urls.stream().map(url ->{
            var stringTimestamp = url;
            return new Timestamp(stringTimestamp);
        } ).collect(Collectors.toList());
        
    }


}

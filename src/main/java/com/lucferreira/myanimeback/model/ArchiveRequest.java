package com.lucferreira.myanimeback.model;

public class ArchiveRequest {
    private String url;

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public ArchiveRequest(){
        
    }
    public ArchiveRequest(String url){
        this.url = url;
    }
}


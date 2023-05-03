package com.lucferreira.myanimeback.service.scraper;

public class TextSelector {
    private String pattern;
    private boolean groupExpect = false;


    public TextSelector(String pattern,boolean groupExpect){
        this.pattern = pattern;
        this.groupExpect =groupExpect;
    }
    public TextSelector(String pattern){
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

    public boolean isGroupExpect() {
        return groupExpect;
    }
}

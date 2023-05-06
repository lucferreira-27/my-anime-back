package com.lucferreira.myanimeback.service.scraper;

import java.util.List;

public class DocSelector {
    private String selector;
    private String pattern;
    private boolean parentSelector = false;


    public DocSelector(String selector,boolean parentSelector){
        this.selector = selector;
        this.parentSelector =parentSelector;
    }
    public DocSelector(String selector,boolean parentSelector, String  pattern ){
        this.selector = selector;
        this.parentSelector =parentSelector;
        this.pattern = pattern;
    }
    public DocSelector(String selector){
        this.selector = selector;
    }
    public String getSelector() {
        return selector;
    }

    public boolean isParentSelector() {
        return parentSelector;
    }

    public String getPattern() {
        return pattern;
    }
}

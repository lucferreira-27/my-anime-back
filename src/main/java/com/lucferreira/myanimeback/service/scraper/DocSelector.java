package com.lucferreira.myanimeback.service.scraper;

public class DocSelector {
    private String selector;
    private boolean parentSelector = false;


    DocSelector(String selector,boolean parentSelector){
        this.selector = selector;
        this.parentSelector =parentSelector;
    }
    DocSelector(String selector){
        this.selector = selector;
    }
    public String getSelector() {
        return selector;
    }

    public boolean isParentSelector() {
        return parentSelector;
    }

}

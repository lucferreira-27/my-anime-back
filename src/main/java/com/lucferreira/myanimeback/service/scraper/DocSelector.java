package com.lucferreira.myanimeback.service.scraper;

public class DocSelector {
    private String selector;
    private boolean parentSelector = false;


    public DocSelector(String selector,boolean parentSelector){
        this.selector = selector;
        this.parentSelector =parentSelector;
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

}

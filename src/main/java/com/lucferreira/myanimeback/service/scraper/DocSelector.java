package com.lucferreira.myanimeback.service.scraper;


public class DocSelector {
    private String selector;
    private String pattern;
    private boolean parentSelector = false;
    private Integer id;


    public DocSelector(String selector,boolean parentSelector,Integer id){
        this.id = id;
        this.selector = selector;
        this.parentSelector =parentSelector;
    }
    public DocSelector(String selector,boolean parentSelector, String  pattern,Integer id ){
        this.id = id;
        this.selector = selector;
        this.parentSelector =parentSelector;
        this.pattern = pattern;
    }
    public DocSelector(String selector,Integer id){
        this.id = id;
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

    public Integer getId() {
        return id;
    }
}

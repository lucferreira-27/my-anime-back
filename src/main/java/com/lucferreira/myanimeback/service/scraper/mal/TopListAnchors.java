package com.lucferreira.myanimeback.service.scraper.mal;

import com.lucferreira.myanimeback.service.scraper.DocSelector;
import com.lucferreira.myanimeback.service.scraper.TextSelector;

import java.util.List;

public enum TopListAnchors {
TOP_LIST_INIT("text","[0-9]+",new DocSelector(".ranking-list")),
    TOP_LIST_SCORE("score","(\\d+\\.\\d{2})",
            new DocSelector(".score-label",true),
            new DocSelector(".score.ac",true)
    ),
    TOP_LIST_RANK("rank","[0-9]+",new DocSelector(".ac",true)),
    TOP_LIST_TITLE("title",null,new DocSelector(".anime_ranking_h3 a",true),
            new DocSelector(".title .hoverinfo_trigger",true)),
    TOP_LIST_URL("title",null,new DocSelector(".anime_ranking_h3 a",true),new DocSelector(".title .hoverinfo_trigger",true)),

    TOP_LIST_INFORMATION(
            "media",
            "^(?<type>[\\w\\s]+) \\((?<count>\\d+|\\?)\\s\\w+\\) (?<startDate>\\w{3}\\s\\d{4}) - (?<endDate>(?:(?<endMonth>\\s?\\w{3})\\s?)?(?<endYear>\\d{4}))?(?:\\s(?<members>[\\d,]+) members)?$",
            new DocSelector(".information",true)
    );




    private final List<DocSelector> selectors;
    private String pattern;
    private final String type;

    TopListAnchors(String type, String pattern, DocSelector ... selectors) {
        this.pattern = pattern;
        this.selectors = selectors != null ? List.of(selectors) : null;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getPattern() {
        return pattern;
    }

    public List<DocSelector> getSelectors() {
        return selectors;
    }
}

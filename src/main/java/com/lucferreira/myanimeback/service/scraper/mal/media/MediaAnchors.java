package com.lucferreira.myanimeback.service.scraper.mal.media;

import com.lucferreira.myanimeback.service.scraper.DocSelector;
import com.lucferreira.myanimeback.service.scraper.TextSelector;

import java.util.List;

public enum MediaAnchors {
    MEDIA_INIT("text", ":", new DocSelector("span.dark_text")),
    MEDIA_SCORE_VALUE("score", "(\\d+\\.\\d{2})", new DocSelector("[itemprop=\"ratingValue\"]"), new DocSelector("div", true)),
    MEDIA_SCORE_VOTES("score", "[0-9]+", new DocSelector("[itemprop=\"ratingCount\"]" ), new DocSelector("div > small",true)),
    MEDIA_POPULARITY("popularity", "[0-9]+", null),
    MEDIA_MEMBERS("members", "[0-9]+", null),
    MEDIA_RANKED("ranked", "[0-9]+", null),
    MEDIA_FAVORITES("favorites", "[0-9]+", null),
    TOP_LIST_INIT("text",null,new DocSelector(".ranking-list")),
    TOP_LIST_SCORE("score",null,new DocSelector(".score-label",true)),
    TOP_LIST_RANK("rank",null,new DocSelector(".ac",true)),
    TOP_LIST_TITLE("title",null,new DocSelector(".anime_ranking_h3 a",true)),
    TOP_LIST_MEMBERS(
            "media",
            "^(.*)\\\\n(.*)\\\\n(.*)$",
            new DocSelector(".information",true)
    ),
    TOP_LIST_VOTES(
            "totalVotes",
                    "^(.*)\\\\n(.*)\\\\n(.*)$",
                    new DocSelector(".information",true)
    ),
    TOP_LIST_COUNT_VOTES(
            "countVotes",
            "^(.*)\\\\n(.*)\\\\n(.*)$",
            new DocSelector(".information",true)
    ),
    TOP_LIST_DATE(
            "date",
            "^(.*)\\\\n(.*)\\\\n(.*)$",
            new DocSelector(".information",true)
    ),
    TOP_LIST_INFORMATION(
            "media",
            "^(.*)\\\\n(.*)\\\\n(.*)$",
            new DocSelector(".information",true)
    );


    private final List<DocSelector> selectors;
    private String pattern;
    private final String type;

    MediaAnchors(String type, String pattern, DocSelector ... selectors) {
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

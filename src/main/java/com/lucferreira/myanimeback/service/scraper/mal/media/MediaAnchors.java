package com.lucferreira.myanimeback.service.scraper.mal.media;

import com.lucferreira.myanimeback.service.scraper.DocSelector;
import com.lucferreira.myanimeback.service.scraper.TextSelector;

import java.util.List;

public enum MediaAnchors {
    MEDIA_INIT("text", ":", new DocSelector("span.dark_text",1)),
    MEDIA_SCORE_VALUE("score", "(\\d+\\.\\d{2})", new DocSelector("[itemprop=\"ratingValue\"]",1), new DocSelector("div", true,1)),
    MEDIA_SCORE_VOTES("score", "[0-9]+", new DocSelector("[itemprop=\"ratingCount\"]" ,1), new DocSelector("div > small",true,1)),
    MEDIA_POPULARITY("popularity", "[0-9]+", null),
    MEDIA_MEMBERS("members", "[0-9]+", null),
    MEDIA_RANKED("ranked", "[0-9]+", null),
    MEDIA_FAVORITES("favorites", "[0-9]+", null);



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

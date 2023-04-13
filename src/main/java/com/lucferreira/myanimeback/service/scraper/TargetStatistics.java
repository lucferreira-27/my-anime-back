package com.lucferreira.myanimeback.service.scraper;

import java.util.List;

public enum TargetStatistics {
    TEXT("text", ":", new DocSelector("span.dark_text")),
    SCORE_VALUE("score", "(\\d+\\.\\d{2})", new DocSelector("[itemprop=\"ratingValue\"]"), new DocSelector("div", true)),
    SCORE_VOTES("score", "[0-9]+", new DocSelector("[itemprop=\"ratingCount\"]" ), new DocSelector("div > small",true)),
    POPULARITY("popularity", "[0-9]+", null),
    MEMBERS("members", "[0-9]+", null),
    RANKED("ranked", "[0-9]+", null),
    FAVORITES("favorites", "[0-9]+", null);

    private final List<DocSelector> selectors;
    private final String pattern;
    private final String type;

    TargetStatistics(String type, String pattern, DocSelector... selectors) {
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

package com.lucferreira.myanimeback.service.scraper;

import java.util.List;

public enum TargetStatistics {
    TEXT("text", ":", "span.dark_text"),
    SCORE_VALUE("score", null, "[itemprop=\"ratingValue\"]"),
    SCORE_VOTES("score", null, "[itemprop=\"ratingCount\"]"),
    POPULARITY("popularity", "[0-9]+", null),
    MEMBERS("members", "[0-9]+", null),
    RANKED("ranked", "[0-9]+", null),
    FAVORITES("favorites", "[0-9]+", null);
    private final List<String> selectors;
    private final String pattern;
    private final String type;

    TargetStatistics(String type, String pattern, String... selectors) {
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

    public List<String> getSelectors() {
        return selectors;
    }
}
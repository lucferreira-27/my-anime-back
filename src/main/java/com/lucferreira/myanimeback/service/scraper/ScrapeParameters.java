package com.lucferreira.myanimeback.service.scraper;

import java.util.List;
import java.util.Map;

public class ScrapeParameters {


    public ScrapeParameters(Map<String, List<String>> timestampSelectors){

        this.timestampSelectors = timestampSelectors;
    }

    private final Map<String,List<String>> timestampSelectors;

    public Map<String, List<String>> getTimestampSelectors() {
        return timestampSelectors;
    }
}

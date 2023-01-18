package com.lucferreira.myanimeback.service.scraper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MALScrapeParameters {



    private List<String> selectorScoreValues;

    private List<String> selectorScoreVotes;
    private List<String> selectorPopularity;
    private List<String> selectorMembers;
    private List<String> selectorFavorites ;
    private List<String> selectorRanked ;

    private Map<String,List<String>> mapSelectors;


    public MALScrapeParameters(){
        selectorScoreValues = List.of("[itemprop=\"ratingValue\"]");
        selectorScoreVotes = List.of("[itemprop=\"ratingCount\"]");
        selectorRanked = List.of();
        selectorPopularity = List.of();
        selectorMembers = List.of();
        selectorFavorites = List.of();
        mapSelectors = Map.of(
                "statisticsText", Arrays.asList("span.dark_text"),
                "scoreValue",selectorScoreValues,
                "scoreVotes",selectorScoreVotes,
                "popularity",selectorPopularity,
                "members",selectorMembers,
                "ranked",selectorRanked,
                "favorites",selectorFavorites
        );

    }

    public Map<String, List<String>> getMapSelectors() {
        return mapSelectors;
    }
}

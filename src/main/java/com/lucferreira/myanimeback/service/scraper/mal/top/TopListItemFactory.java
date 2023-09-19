package com.lucferreira.myanimeback.service.scraper.mal.top;


import com.lucferreira.myanimeback.exception.ScrapeParseError;
import com.lucferreira.myanimeback.exception.SelectorQueryException;
import com.lucferreira.myanimeback.model.TopListItem;
import com.lucferreira.myanimeback.util.ParseNumber2;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TopListItemFactory {

    private final AnchorHandler anchorHandler;
    @Autowired
    public TopListItemFactory(AnchorHandler anchorHandler) {
        this.anchorHandler = anchorHandler;
    }

    public TopListItem processAnchors(Element element, ListElementID listElementID, List<TopListAnchors> topListAnchors) throws SelectorQueryException, ScrapeParseError {
        Map<TopListAnchors, String> valuesMap = anchorHandler.extractValuesFromAnchors(element, listElementID, topListAnchors);
        return buildTopListItem(valuesMap);
    }

    private TopListItem buildTopListItem(Map<TopListAnchors, String> valuesMap) throws ScrapeParseError {
        return new TopListItem.Builder()
                .scoreValue(ParseNumber2.getDoubleValue(TopListAnchors.TOP_LIST_SCORE, valuesMap))
                .ranked(ParseNumber2.getIntValue(TopListAnchors.TOP_LIST_RANK, valuesMap))
                .title(valuesMap.get(TopListAnchors.TOP_LIST_TITLE))
                .textDate(valuesMap.get(TopListAnchors.TOP_LIST_DATE))
                .members(ParseNumber2.getIntValue(TopListAnchors.TOP_LIST_MEMBERS, valuesMap))
                .count(ParseNumber2.getIntValue(TopListAnchors.TOP_LIST_COUNT, valuesMap))
                .type(valuesMap.get(TopListAnchors.TOP_LIST_TYPE))
                .archiveUrl(valuesMap.get(TopListAnchors.TOP_LIST_URL))
                .build();
    }
}

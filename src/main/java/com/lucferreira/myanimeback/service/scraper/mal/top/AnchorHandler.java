package com.lucferreira.myanimeback.service.scraper.mal.top;

import com.lucferreira.myanimeback.exception.ScrapeParseError;
import com.lucferreira.myanimeback.exception.SelectorQueryException;
import com.lucferreira.myanimeback.service.scraper.DocElement;
import com.lucferreira.myanimeback.service.scraper.ScrapeHelper;
import com.lucferreira.myanimeback.util.Regex;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AnchorHandler {

    private final ScrapeHelper scrapeHelper;

    @Autowired
    public AnchorHandler(ScrapeHelper scrapeHelper) {
        this.scrapeHelper = scrapeHelper;
    }

    public Map<TopListAnchors, String> extractValuesFromAnchors(Element element, Document doc, List<TopListAnchors> topListAnchors) throws SelectorQueryException, ScrapeParseError {
        Map<TopListAnchors, String> valuesMap = new HashMap<>();
        for (TopListAnchors anchor : topListAnchors) {
            if (anchor == TopListAnchors.TOP_LIST_INIT) {
                continue;
            }

            Optional<DocElement> optional = queryAnchorElements(doc, element, anchor);
            if (optional.isEmpty()) {
                continue;
            }

            DocElement docElement = optional.get();
            Elements selectElements = docElement.elements();
            if (selectElements == null) {
                continue;
            }

            String result = handleAnchor(anchor, docElement, doc);
            valuesMap.put(anchor, result);
        }
        return valuesMap;
    }
    private Optional<DocElement> queryAnchorElements(Document doc, Element element, TopListAnchors anchor) throws SelectorQueryException {
        return scrapeHelper.queryElements(doc, element, anchor.getSelectors());
    }

    private String handleAnchor(TopListAnchors anchor, DocElement docElement, Document doc) {
        final Elements selectElements = docElement.elements();
        if (anchor == TopListAnchors.TOP_LIST_URL) {
            return extractUrl(selectElements, doc);
        } else {
            String text = selectElements.text();
            return extractResult(anchor, text, docElement);
        }
    }

    private String extractUrl(Elements selectElements, Document doc) {
        String href = selectElements.attr("href");
        if (!href.startsWith("http")) {
            return doc.baseUri().replaceAll("/top.+", "/") + href;
        }
        return href;
    }

    private String extractResult(TopListAnchors anchor, String text, DocElement docElement) {
        if (anchor.getSelectors() == null || docElement.docSelector().getPattern() == null) {
            return text;
        }

        Map<Integer, String> results = Regex.groups(text, docElement.docSelector().getPattern());
        if(results.isEmpty()){
            return null;
        }
        if (anchor == TopListAnchors.TOP_LIST_DATE) {
            return results.get(0);
        }

        return results.size() > 1 ? results.get(1) : results.get(0);
    }
}

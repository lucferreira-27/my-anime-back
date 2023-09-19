package com.lucferreira.myanimeback.service.scraper.mal.top;

import com.lucferreira.myanimeback.exception.ScrapeParseError;
import com.lucferreira.myanimeback.exception.SelectorQueryException;
import com.lucferreira.myanimeback.service.scraper.DocElement;
import com.lucferreira.myanimeback.service.scraper.DocSelector;
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

    public Map<TopListAnchors, String> extractValuesFromAnchors(Element element, ListElementID listElementID, List<TopListAnchors> topListAnchors) throws SelectorQueryException, ScrapeParseError {
        Map<TopListAnchors, String> valuesMap = new HashMap<>();
        for (TopListAnchors anchor : topListAnchors) {
            if (anchor == TopListAnchors.TOP_LIST_INIT)  continue;
            Optional<String> optionalResult = getResult(element, listElementID, anchor);
            if (optionalResult.isEmpty()) continue;
            String result = optionalResult.get();
            valuesMap.put(anchor, result);
        }
        return valuesMap;
    }

    private Optional<String> getResult(Element element, ListElementID listElementID, TopListAnchors anchor) throws SelectorQueryException {
        Optional<DocElement> optional = queryAnchorElements(listElementID, element, anchor.getSelectors());
        if (optional.isEmpty()) {
            return Optional.empty();
        }

        DocElement docElement = optional.get();

        Elements selectElements = docElement.elements();
        if (selectElements == null) {
            return Optional.empty();
        }

        Optional<String> optionalResult = handleAnchor(anchor, docElement, listElementID);
        if(optionalResult.isEmpty()){
            return Optional.empty();
        }
        if(optionalResult.get().isEmpty()){
            if(docElement.position() < anchor.getSelectors().size()){
                return getResultNext(element,listElementID,anchor,docElement.position());
            }
        }
        return optionalResult;
    }
    private Optional<String> getResultNext(Element element, ListElementID listElementID, TopListAnchors anchor, int fromNext) throws SelectorQueryException {
        List<DocSelector> splitDocSelectors = anchor.getSelectors().subList(fromNext + 1,anchor.getSelectors().size());
        Optional<DocElement> optional = queryAnchorElements(listElementID, element, splitDocSelectors);
        if (optional.isEmpty()) {
            return Optional.empty();
        }

        DocElement docElement = optional.get();
        Elements selectElements = docElement.elements();
        if (selectElements == null) {
            return Optional.empty();
        }

        Optional<String> optionalResult = handleAnchor(anchor, docElement, listElementID);
        if(optionalResult.get().isEmpty()){
            if(docElement.position() < anchor.getSelectors().size()){
                return getResultNext(element,listElementID,anchor,docElement.position());
            }
        }
        return optionalResult;
    }
    private Optional<DocElement> queryAnchorElements(ListElementID listElementID, Element element, List<DocSelector> docSelectors) throws SelectorQueryException {
        return scrapeHelper.queryElements(listElementID, element, docSelectors);
    }
    
    private Optional<String> handleAnchor(TopListAnchors anchor, DocElement docElement, ListElementID listElementID) {
        Document doc = listElementID.doc();
        final Elements selectElements = docElement.elements();
        if (anchor == TopListAnchors.TOP_LIST_URL) {
            return Optional.of(extractUrl(selectElements, doc));
        }
        String text = selectElements.text();
        String result = extractResult(anchor, text, docElement);
        return Optional.ofNullable(result);

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

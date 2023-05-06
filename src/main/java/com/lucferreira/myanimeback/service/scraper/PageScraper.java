package com.lucferreira.myanimeback.service.scraper;

import com.lucferreira.myanimeback.exception.ArchiveScraperException;
import com.lucferreira.myanimeback.exception.SelectorQueryException;
import com.lucferreira.myanimeback.service.scraper.mal.media.MediaAnchors;
import com.lucferreira.myanimeback.util.Regex;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class PageScraper<T> {

    protected final  ScrapeHelper scrapeHelper;

    protected PageScraper(ScrapeHelper scrapeHelper) {
        this.scrapeHelper = scrapeHelper;
    }

    public T scrape(String url) throws ArchiveScraperException, IOException {
        return null;
    }


    protected Map<MediaAnchors, String> getParseTexts(Elements elements, Document doc) throws SelectorQueryException {
        Map<MediaAnchors, String> parseTextMap = new HashMap<>();
        List<MediaAnchors> targetTypes = List.of(MediaAnchors.values());

        for (Element el : elements) {
            List<MediaAnchors> targetStatistics = findTargetTypes(el, targetTypes);

            for (MediaAnchors target : targetStatistics) {
                Element parentElement = el.parent();
                String parseText = extractData(doc, target, parentElement);
                parseTextMap.put(target, parseText);
            }
        }
        return parseTextMap;
    }

    protected List<MediaAnchors> findTargetTypes(Element el, List<MediaAnchors> targetTypes) {
        String elText = el.ownText().replaceAll(MediaAnchors.MEDIA_INIT.getPattern(), "").toLowerCase();
        return targetTypes.stream().filter(targetType -> targetType.getType().equals(elText)).collect(Collectors.toList());
    }

    protected String extractData(Document doc, MediaAnchors target, Element parentElement) throws SelectorQueryException {
        if (target.getSelectors() != null) {
            Optional<DocElement> optional = scrapeHelper.queryElements(doc, parentElement,target.getSelectors());
            optional.orElseThrow(() -> new SelectorQueryException("No elements found for the given selectors."));
            DocElement docElement = optional.get();
            Element element = docElement.elements().first();
            return extractText(target, element);
        }
        return extractText(target, parentElement);
    }

    protected String extractText(MediaAnchors target, Element element) {
        if (target.getPattern() == null) {
            return element.ownText();
        }
        return Regex.matchAll(element.ownText(), target.getPattern());
    }

}

package com.lucferreira.myanimeback.service.scraper;

import com.lucferreira.myanimeback.exception.*;
import com.lucferreira.myanimeback.model.Record;
import com.lucferreira.myanimeback.util.ParseNumber;
import com.lucferreira.myanimeback.util.Regex;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MyAnimeListScraper implements ArchiveScraper {

    public Record scrape(String url) throws ArchiveScraperException {
        Connection.Response response = connectToUrl(url);
        String documentUrl = response.url().toString();;
        Document doc = jsoupParse(response);
        Elements elements = queryElements(doc, null,TargetStatistics.TEXT.getSelectors());
        Map<TargetStatistics, String> parseTextMap = getParseTexts(elements, doc);

        return new Record.Builder()
                .archiveUrl(documentUrl)
                .members(ParseNumber.getIntValue(TargetStatistics.MEMBERS, parseTextMap))
                .scoreValue(ParseNumber.getDoubleValue(TargetStatistics.SCORE_VALUE, parseTextMap))
                .totalVotes(ParseNumber.getIntValue(TargetStatistics.SCORE_VOTES, parseTextMap))
                .favorites(ParseNumber.getIntValue(TargetStatistics.FAVORITES, parseTextMap))
                .popularity(ParseNumber.getIntValue(TargetStatistics.POPULARITY, parseTextMap))
                .ranked(ParseNumber.getIntValue(TargetStatistics.RANKED, parseTextMap))
                .build();
    }


    private Map<TargetStatistics, String> getParseTexts(Elements elements, Document doc) throws SelectorQueryException {
        Map<TargetStatistics, String> parseTextMap = new HashMap<>();
        List<TargetStatistics> targetTypes = List.of(TargetStatistics.values());

        for (Element el : elements) {
            List<TargetStatistics> targetStatistics = findTargetTypes(el, targetTypes);

            for (TargetStatistics target : targetStatistics) {
                Element parentElement = el.parent();
                String parseText = extractData(doc, target, parentElement);
                parseTextMap.put(target, parseText);
            }
        }
        return parseTextMap;
    }

    private Connection.Response connectToUrl(String url) throws ScrapeConnectionError {
        try {
            Connection.Response response = Jsoup.connect(url).followRedirects(true).execute();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            throw new ScrapeConnectionError("Error scraping data from URL: " + url + ". The connection failed. Please check the url and try again later.");
        }
    }
    private Document jsoupParse(Connection.Response response) throws ScrapeConnectionError {
        String url = response.url().toString();
        try {
            return response.parse();
        } catch (IOException e) {
            e.printStackTrace();
            throw new ScrapeConnectionError("Error scraping data from URL: " + url + ". The connection failed. Please check the url and try again later.");
        }
    }


    private Elements queryElements(Document doc, Element parentElement, List<DocSelector> docSelectors) throws SelectorQueryException {

        for (DocSelector docSelector : docSelectors) {
            String selector = docSelector.getSelector();
            Elements element = docSelector.isParentSelector() ? parentElement.select(selector) : doc.select(selector);
            if (!element.isEmpty()) {
                return element;
            }
        }
        throw new SelectorQueryException("No elements found for the given selectors.");
    }
    private Elements queryElements(Elements parentElement, List<String> selectors) throws SelectorQueryException {
        for (String selector : selectors) {
            Elements element = parentElement.select(selector);
            if (!element.isEmpty()) {
                return element;
            }
        }
        throw new SelectorQueryException("No elements found for the given selectors.");
    }
    private List<TargetStatistics> findTargetTypes(Element el, List<TargetStatistics> targetTypes) {
        String elText = el.ownText().replaceAll(TargetStatistics.TEXT.getPattern(), "").toLowerCase();
        return targetTypes.stream().filter(targetType -> targetType.getType().equals(elText)).collect(Collectors.toList());
    }

    private String extractData(Document doc, TargetStatistics target, Element parentElement) throws SelectorQueryException {
        if (target.getSelectors() != null) {
            Element element = queryElements(doc, parentElement,target.getSelectors()).first();
            return extractText(target, element);
        }
        return extractText(target, parentElement);
    }

    private String extractText(TargetStatistics target, Element element) {
        if (target.getPattern() == null) {
            return element.ownText();
        }
        return Regex.matchAll(element.ownText(), target.getPattern());
    }

}

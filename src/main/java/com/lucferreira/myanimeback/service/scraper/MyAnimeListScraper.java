package com.lucferreira.myanimeback.service.scraper;

import com.lucferreira.myanimeback.exception.ArchiveScraperException;
import com.lucferreira.myanimeback.exception.ScrapeConnectionError;
import com.lucferreira.myanimeback.exception.ScrapeParseError;
import com.lucferreira.myanimeback.exception.SelectorQueryException;
import com.lucferreira.myanimeback.model.MediaState;
import com.lucferreira.myanimeback.util.Regex;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MyAnimeListScraper implements ArchiveScraper{



    public MediaState scrape(String url) throws ArchiveScraperException {
        Document doc = connectToUrl(url);
        Elements elements = queryElements(doc, TargetStatistics.TEXT.getSelectors());
        Map<TargetStatistics, String> parseTextMap = getParseTexts(elements,doc);
        Integer members = getNumericalValueForTargetStatistics (TargetStatistics.MEMBERS,parseTextMap);
        Double scoreValue = getNumericalValueForTargetStatistics (TargetStatistics.SCORE_VALUE,parseTextMap);
        Integer scoreVotes = getNumericalValueForTargetStatistics (TargetStatistics.SCORE_VOTES,parseTextMap);
        Integer favorites = getNumericalValueForTargetStatistics (TargetStatistics.FAVORITES,parseTextMap);
        Integer popularity = getNumericalValueForTargetStatistics (TargetStatistics.POPULARITY,parseTextMap);
        Integer ranked = getNumericalValueForTargetStatistics (TargetStatistics.RANKED,parseTextMap);

        return new MediaState.Builder()
                .archiveUrl(url)
                .members(members)
                .scoreValue(scoreValue)
                .totalVotes(scoreVotes)
                .favorites(favorites)
                .popularity(popularity).
                ranked(ranked).build();
    }

    private <T extends Number> T getNumericalValueForTargetStatistics (TargetStatistics target,  Map<TargetStatistics, String> parseTextMap) throws ScrapeParseError {
        String result = parseTextMap.get(target);
        if (result == null){
            return null;
        }
        try{
            if (target.equals(TargetStatistics.SCORE_VALUE)){
                return (T) Double.valueOf(result);
            }
            return (T) Integer.valueOf(result);
        }catch(NumberFormatException e){
            throw new ScrapeParseError("Error parsing the data for target " + target.name() + ": " + result + " is not a valid number.");
        }
    }


    private Map<TargetStatistics,String> getParseTexts(Elements elements,Document doc) throws SelectorQueryException {

        final Map<TargetStatistics,String> parseTextMap = new HashMap<>();

        List<TargetStatistics> targetTypes = List.of(TargetStatistics.values());

        for (Element el : elements) {
            List<TargetStatistics> targetStatistics = findTargetTypes(el, targetTypes);

            if (targetStatistics.size() > 0) {
                for (TargetStatistics target : targetStatistics){
                    Element parentElement = el.parent();
                    String parseText = extractData(doc, target, parentElement);
                    parseTextMap.put(target,parseText);
                }
            }
        }
        return parseTextMap;
    }

    private Document connectToUrl(String url) throws ScrapeConnectionError {
        try{
            return Jsoup.connect(url).get();
        }catch (IOException e){
            e.printStackTrace();
            throw new ScrapeConnectionError("Error scraping data from URL: " + url + ". The connection failed. Please check the url and try again later.");
        }
    }

    private Elements queryElements(Document doc, List<String> selectors) throws SelectorQueryException {
        for (String selector : selectors){
            Elements element =  doc.select(selector);;
            return element;
        }
        throw new SelectorQueryException("No elements found for the given selectors.");
    }

    private List<TargetStatistics> findTargetTypes(Element el, List<TargetStatistics> targetTypes) {
        String elText = el.ownText().replace(TargetStatistics.TEXT.getPattern(), "").toLowerCase();
        return targetTypes.stream().filter((targetType) -> targetType.getType().equals(elText)).collect(Collectors.toList());
    }

    private String extractData(Document doc, TargetStatistics target, Element parentElement) throws SelectorQueryException {
        if (target.getSelectors() != null) {
            Element element = queryElements(doc, target.getSelectors()).first();
            if (target.getPattern() == null) {
                return element.ownText();
            }
            return Regex.matchAll(element.ownText(), target.getPattern());
        }
        return Regex.matchAll(parentElement.ownText(), target.getPattern());
    }



}

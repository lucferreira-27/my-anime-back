package com.lucferreira.myanimeback.service.scraper.mal;

import com.lucferreira.myanimeback.exception.ArchiveScraperException;
import com.lucferreira.myanimeback.exception.ScrapeParseError;
import com.lucferreira.myanimeback.exception.SelectorQueryException;
import com.lucferreira.myanimeback.model.Record;
import com.lucferreira.myanimeback.model.TopList;
import com.lucferreira.myanimeback.model.TopListItem;
import com.lucferreira.myanimeback.service.scraper.PageScraper;
import com.lucferreira.myanimeback.service.scraper.ScrapeHelper;
import com.lucferreira.myanimeback.util.ParseNumber;
import com.lucferreira.myanimeback.util.ParseNumber2;
import com.lucferreira.myanimeback.util.Regex;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

@Service
public class TopScrape extends PageScraper<TopList> {
    @Autowired
    public TopScrape(ScrapeHelper scrapeHelper) {
        super(scrapeHelper);
    }

    public TopList scrape(String url) throws ArchiveScraperException{
        Connection.Response response = scrapeHelper.connectToUrl(url);
        String documentUrl = response.url().toString();
        Document doc = scrapeHelper.jsoupParse(response);

        Elements elements = scrapeHelper.queryElements(doc, null, TopListAnchors.TOP_LIST_INIT.getSelectors());

        TopList topList = processElements(elements, doc);

        return topList;
    }

    private TopList processElements(Elements elements, Document doc) throws SelectorQueryException, ScrapeParseError {
        List<TopListAnchors> topListAnchors = List.of(TopListAnchors.values());

        TopList topList = new TopList(
                TopList.TopType.ANIME,
                TopList.TopSubtype.ALL,
                null);
        for (Element element: elements) {
            TopListItem topListItem =  processAnchors(element, doc, topListAnchors);
            topList.addTopListItemToMap(topListItem);
        }
        return topList;
    }

    private TopListItem processAnchors(Element element, Document doc, List<TopListAnchors> topListAnchors) throws SelectorQueryException, ScrapeParseError {
        TopListItem.Builder itemBuilder = new TopListItem.Builder();
        Map<TopListAnchors, String> valuesMap =  new HashMap<>();
        for (TopListAnchors anchor: topListAnchors) {
            if(anchor == TopListAnchors.TOP_LIST_INIT){
                continue;
            }
            System.out.println(anchor);
            Elements selectElements = scrapeHelper.queryElements(doc,element,anchor.getSelectors());
            if(selectElements == null){
                continue;
            }
            String text = selectElements.text();
            if(anchor == TopListAnchors.TOP_LIST_INFORMATION){
                processTopListInformation(text, anchor,itemBuilder);
            }
            if(anchor == TopListAnchors.TOP_LIST_URL){
                String href = selectElements.attr("href");
                valuesMap.put(anchor,href);
                continue;
            }

            if(anchor.getPattern() == null){
                valuesMap.put(anchor,text);
                continue;
            }
            String result = Regex.match(text,anchor.getPattern());
            valuesMap.put(anchor,result);
        }
        return itemBuilder
                .scoreValue(ParseNumber2.getDoubleValue(TopListAnchors.TOP_LIST_SCORE,valuesMap))
                .ranked(ParseNumber2.getIntValue(TopListAnchors.TOP_LIST_RANK,valuesMap))
                .title(valuesMap.get(TopListAnchors.TOP_LIST_TITLE))
                .archiveUrl(valuesMap.get(TopListAnchors.TOP_LIST_URL))
                .build();

    }

    private TopListItem.Builder processTopListInformation(String text, TopListAnchors anchor, TopListItem.Builder itemBuilder) throws ScrapeParseError {
        if(anchor.getPattern() == null){
            return itemBuilder;
        }
        Optional<Matcher> optional = Regex.groupsName(text,anchor.getPattern());
        if(optional.isPresent()){
            Matcher matcher = optional.get();
            Map<String,String> map = new HashMap<>();

            String type = matcher. group("type");
            map.put("type",type);
            String count = matcher.group("count");
            map.put("count",count);
            String members = matcher.group("members");
            String startDate = matcher.group("startDate");
            map.put("startDate",startDate);
            String endDate = matcher.group("endDate");
            map.put("endDate",endDate);

            map.put("members",members);
            String textDate = startDate + " - " + endDate;
            return itemBuilder
                    .members(ParseNumber2.getIntValue("members", map))
                    .count(ParseNumber2.getIntValue("count", map))
                    .type(type)
                    .textDate(textDate);
        }
        return null;
    }





}

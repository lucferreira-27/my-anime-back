package com.lucferreira.myanimeback.service.scraper.mal.top;

import com.lucferreira.myanimeback.exception.ArchiveScraperException;
import com.lucferreira.myanimeback.exception.ScrapeParseError;
import com.lucferreira.myanimeback.exception.SelectorQueryException;
import com.lucferreira.myanimeback.model.TopList;
import com.lucferreira.myanimeback.model.TopListItem;
import com.lucferreira.myanimeback.service.scraper.DocElement;
import com.lucferreira.myanimeback.service.scraper.PageScraper;
import com.lucferreira.myanimeback.service.scraper.ScrapeHelper;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.util.*;

@Service
public class TopScrape extends PageScraper<TopList> {


    private final TopListItemFactory topListItemFactory;
    private final TopListFactory topListFactory;
    @Autowired
    public TopScrape(ScrapeHelper scrapeHelper, TopListItemFactory topListItemFactory, TopListFactory topListFactory) {
        super(scrapeHelper);
        this.topListItemFactory = topListItemFactory;
        this.topListFactory = topListFactory;
    }

    public TopList scrape(String url) throws ArchiveScraperException{
        Connection.Response response = scrapeHelper.connectToUrl(url);
        Document doc = scrapeHelper.jsoupParse(response);
        Optional<DocElement> optional = scrapeHelper.queryElements(doc, null, TopListAnchors.TOP_LIST_INIT.getSelectors());
        optional.orElseThrow(() -> new SelectorQueryException("No elements found for the given selectors."));
        DocElement docElement = optional.get();
        Elements elements = docElement.elements();
        ListElementID listElementID = new ListElementID(elements,doc,docElement.docSelector().getId());

        TopList topList = processElements(listElementID, doc);

        return topList;
    }

    private TopList processElements(ListElementID listElementID, Document doc) throws SelectorQueryException, ScrapeParseError {
        List<TopListAnchors> topListAnchors = List.of(TopListAnchors.values());

        TopList topList = topListFactory.createTopList(doc.baseUri());
        for (Element element: listElementID.elements()) {
            TopListItem topListItem =  topListItemFactory.processAnchors(element, listElementID, topListAnchors);
            topList.addTopListItemToMap(topListItem);
        }
        return topList;
    }








}

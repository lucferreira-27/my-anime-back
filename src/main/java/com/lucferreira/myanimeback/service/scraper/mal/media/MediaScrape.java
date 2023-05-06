package com.lucferreira.myanimeback.service.scraper.mal;

import com.lucferreira.myanimeback.exception.ArchiveScraperException;
import com.lucferreira.myanimeback.exception.SelectorQueryException;
import com.lucferreira.myanimeback.model.Record;
import com.lucferreira.myanimeback.service.scraper.DocElement;
import com.lucferreira.myanimeback.service.scraper.PageScraper;
import com.lucferreira.myanimeback.service.scraper.ScrapeHelper;
import com.lucferreira.myanimeback.util.ParseNumber;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class MediaScrape extends PageScraper<Record> {

    @Autowired
    public MediaScrape(ScrapeHelper scrapeHelper) {
        super(scrapeHelper);
    }

    public Record scrape(String url) throws ArchiveScraperException {
        Connection.Response response = scrapeHelper.connectToUrl(url);
        String documentUrl = response.url().toString();;
        Document doc = scrapeHelper.jsoupParse(response);
        Optional<DocElement> optional = scrapeHelper.queryElements(doc, null, TopListAnchors.TOP_LIST_INIT.getSelectors());
        optional.orElseThrow(() -> new SelectorQueryException("No elements found for the given selectors."));
        DocElement docElement = optional.get();
        Elements elements = docElement.getElements();
        Map<MediaAnchors, String> parseTextMap = getParseTexts(elements, doc);

        return new Record.Builder()
                .archiveUrl(documentUrl)
                .members(ParseNumber.getIntValue(MediaAnchors.MEDIA_MEMBERS, parseTextMap))
                .scoreValue(ParseNumber.getDoubleValue(MediaAnchors.MEDIA_SCORE_VALUE, parseTextMap))
                .totalVotes(ParseNumber.getIntValue(MediaAnchors.MEDIA_SCORE_VOTES, parseTextMap))
                .favorites(ParseNumber.getIntValue(MediaAnchors.MEDIA_FAVORITES, parseTextMap))
                .popularity(ParseNumber.getIntValue(MediaAnchors.MEDIA_POPULARITY, parseTextMap))
                .ranked(ParseNumber.getIntValue(MediaAnchors.MEDIA_RANKED, parseTextMap))
                .build();
    }

}

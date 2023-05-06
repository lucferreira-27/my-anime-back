package com.lucferreira.myanimeback.service.scraper.mal;

import com.lucferreira.myanimeback.exception.*;
import com.lucferreira.myanimeback.model.Record;
import com.lucferreira.myanimeback.model.TopList;
import com.lucferreira.myanimeback.service.scraper.ArchiveScraper;
import com.lucferreira.myanimeback.service.scraper.mal.media.MediaScrape;
import com.lucferreira.myanimeback.service.scraper.mal.top.TopScrape;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MyAnimeListScraper implements ArchiveScraper {

    private final MediaScrape mediaScrape;
    private final TopScrape topScrape;
    @Autowired
    public MyAnimeListScraper(MediaScrape mediaScrape, TopScrape topScrape) {
        this.mediaScrape = mediaScrape;
        this.topScrape = topScrape;
    }

    public Record mediaScrape(String url) throws ArchiveScraperException {
        Record record = mediaScrape.scrape(url);
        return record;
    }

    @Override
    public TopList topScrape(String url) throws ArchiveScraperException, IOException {
        TopList topList = topScrape.scrape(url);
        return topList;
    }


}

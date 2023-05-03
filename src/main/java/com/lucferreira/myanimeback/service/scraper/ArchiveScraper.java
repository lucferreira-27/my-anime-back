package com.lucferreira.myanimeback.service.scraper;

import com.lucferreira.myanimeback.exception.ArchiveScraperException;
import com.lucferreira.myanimeback.model.Record;
import com.lucferreira.myanimeback.model.TopList;

import java.io.IOException;

public interface ArchiveScraper {
    public Record mediaScrape(String url) throws ArchiveScraperException, IOException;
    public TopList topScrape(String url) throws ArchiveScraperException, IOException;
}

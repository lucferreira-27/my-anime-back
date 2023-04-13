package com.lucferreira.myanimeback.service.scraper;

import com.lucferreira.myanimeback.exception.ArchiveScraperException;
import com.lucferreira.myanimeback.model.Record;

import java.io.IOException;

public interface ArchiveScraper {
    public Record scrape(String url) throws ArchiveScraperException, IOException;
}

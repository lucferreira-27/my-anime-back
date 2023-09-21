package com.lucferreira.myanimeback.service.scraper;

import com.lucferreira.myanimeback.exception.ArchiveScraperException;
import com.lucferreira.myanimeback.model.record.MediaRecord;
import com.lucferreira.myanimeback.model.record.TopListRecord;

import java.io.IOException;

public interface ArchiveScraper {
    public MediaRecord mediaScrape(String url) throws ArchiveScraperException, IOException;
    public TopListRecord topScrape(String url) throws ArchiveScraperException, IOException;
}

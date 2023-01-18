package com.lucferreira.myanimeback.service.scraper;

import com.lucferreira.myanimeback.exception.ArchiveScraperException;
import com.lucferreira.myanimeback.exception.ScrapeConnectionError;
import com.lucferreira.myanimeback.exception.ScrapeParseError;
import com.lucferreira.myanimeback.exception.SelectorQueryException;
import com.lucferreira.myanimeback.model.MediaState;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ArchiveScraper {
    public MediaState scrape(String url) throws ArchiveScraperException;
}

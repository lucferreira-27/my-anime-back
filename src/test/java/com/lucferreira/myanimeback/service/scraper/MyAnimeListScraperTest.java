package com.lucferreira.myanimeback.service.scraper;

import static org.junit.jupiter.api.Assertions.*;

import com.lucferreira.myanimeback.exception.ArchiveScraperException;
import com.lucferreira.myanimeback.exception.ScrapeParseError;
import com.lucferreira.myanimeback.model.Record;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MyAnimeListScraperTest {

    private final MyAnimeListScraper scraper = new MyAnimeListScraper();

    @Test
    void scrape_ReturnsRecord_WhenValidUrlProvided() throws ArchiveScraperException {
        String url = "https://myanimelist.net/anime/5114/Fullmetal_Alchemist__Brotherhood";
        Record record = scraper.scrape(url);
        assertNotNull(record);
        assertEquals(url, record.getArchiveUrl());
        assertNotNull(record.getMembers());
        assertNotNull(record.getScoreValue());
        assertNotNull(record.getTotalVotes());
        assertNotNull(record.getFavorites());
        assertNotNull(record.getPopularity());
        assertNotNull(record.getRanked());
    }

    @Test
    void scrape_ThrowsArchiveScraperException_WhenInvalidUrlProvided() {
        String url = "https://myanimelist.net/anime/invalid_url";
        assertThrows(ArchiveScraperException.class, () -> scraper.scrape(url));
    }


}

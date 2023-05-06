package com.lucferreira.myanimeback.service.scraper;

import static org.junit.jupiter.api.Assertions.*;

import com.lucferreira.myanimeback.exception.ArchiveScraperException;
import com.lucferreira.myanimeback.model.Record;
import com.lucferreira.myanimeback.service.scraper.mal.media.MediaScrape;
import com.lucferreira.myanimeback.service.scraper.mal.MyAnimeListScraper;
import com.lucferreira.myanimeback.service.scraper.mal.top.TopScrape;
import org.junit.jupiter.api.Test;

class MyAnimeListScraperTest {

    private ScrapeHelper scrapeHelper = new ScrapeHelper();
    private MediaScrape mediaScrape = new MediaScrape(scrapeHelper);
    private TopScrape topScrape = new TopScrape(scrapeHelper,  null, null);

    private final MyAnimeListScraper scraper = new MyAnimeListScraper(mediaScrape, topScrape);

    @Test
    void mediaScrape_ReturnsRecord_WhenValidUrlProvided() throws ArchiveScraperException {
        String url = "https://myanimelist.net/anime/5114/Fullmetal_Alchemist__Brotherhood";
        Record record = scraper.mediaScrape(url);
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
    void mediaScrape_ThrowsArchiveScraperException_WhenInvalidUrlProvided() {
        String url = "https://myanimelist.net/anime/invalid_url";
        assertThrows(ArchiveScraperException.class, () -> scraper.mediaScrape(url));
    }


}

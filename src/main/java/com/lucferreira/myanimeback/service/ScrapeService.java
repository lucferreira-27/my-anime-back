package com.lucferreira.myanimeback.service;

import com.lucferreira.myanimeback.exception.SelectorQueryException;
import com.lucferreira.myanimeback.model.MediaState;
import com.lucferreira.myanimeback.service.scraper.MALScrapeParameters;
import com.lucferreira.myanimeback.service.scraper.MyAnimeListScraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Service
public class ScrapeService {

    @Autowired
    public ScrapeService(MyAnimeListScraper myAnimeListScraper){
        this.myAnimeListScraper = myAnimeListScraper;
    }
    private final MyAnimeListScraper myAnimeListScraper;



    public MediaState getStatistics(String url){
        try {
            MediaState mediaState = myAnimeListScraper.scrape(url);
            return  mediaState;
        } catch (SelectorQueryException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request, please try again later.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred, please try again later.");
        }
    }

}

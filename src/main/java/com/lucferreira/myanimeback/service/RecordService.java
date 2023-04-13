package com.lucferreira.myanimeback.service;

import com.lucferreira.myanimeback.exception.RecordException;
import com.lucferreira.myanimeback.model.Record;
import com.lucferreira.myanimeback.util.Regex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RecordService {
    @Autowired
    private ScrapeService scrapeService;

    public Record getRecord(String url){

        final String pattern = "^https:\\/\\/web\\.archive\\.org\\/web\\/" +
                "(?<date>\\d{14})\\/(https?:\\/\\/)?myanimelist\\.net\\/" +
                "(?<category>anime|manga)\\/" +
                "(?<id>\\d+)(?:\\/" +
                "(?<title>[\\w-]+))?";
        if(Regex.match(url,pattern) == null){
            final String errorMessage = "Invalid URL. Please provide a valid Archive MyAnimeList URL in the following format: " +
                    "https://web.archive.org/web/{timestamp}*/https://myanimelist.net/{category}/{id}/{title (optional)}";
            throw new RecordException(HttpStatus.BAD_REQUEST,errorMessage);
        }
        Record record = scrapeService.getStatistics(url);
        return record;
    }
}

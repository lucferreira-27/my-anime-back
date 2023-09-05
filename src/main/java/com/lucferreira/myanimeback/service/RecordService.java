package com.lucferreira.myanimeback.service;

import com.lucferreira.myanimeback.exception.RecordException;
import com.lucferreira.myanimeback.model.Record;
import com.lucferreira.myanimeback.model.TopList;
import com.lucferreira.myanimeback.util.Regex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class RecordService {
    @Autowired
    private ScrapeService scrapeService;

    public Record getMediaRecord(String url){

        final String pattern = "^https:\\/\\/web\\.archive\\.org\\/web\\/" +
                "(?<date>\\d{14})\\/(https?:\\/\\/)?myanimelist\\.net(:80)?\\/" +
                "(?<category>anime|manga)\\/" +
                "(?<id>\\d+)(?:\\/" +
                "(?<title>[\\w-]+))?";
        if(Regex.match(url,pattern) == null){
            final String errorMessage = "Invalid URL. Please provide a valid Archive MyAnimeList URL in the following format: " +
                    "https://web.archive.org/web/{timestamp}*/https://myanimelist.net/{category}/{id}/{title (optional)}";
            throw new RecordException(HttpStatus.BAD_REQUEST,errorMessage);
        }
        Record record = scrapeService.getMediaStatistics(url);
        return record;
    }

    public TopList getTopListRecord(String url){

        final String pattern = "https:\\/\\/web\\.archive\\.org\\/web\\/(?<date>\\d{14})\\/(https?:\\/\\/)?(www\\.)?myanimelist\\.net\\/(?<category>topanime.php|topmanga.php)";
        if(Regex.match(url,pattern) == null){
            final String errorMessage = "Invalid URL. Please provide a valid Archive MyAnimeList URL in the following format: " +
                    "https://web.archive.org/web/{timestamp}*/https://myanimelist.net/{topanime|topmanga}";
            throw new RecordException(HttpStatus.BAD_REQUEST,errorMessage);
        }
        TopList record = scrapeService.getTopListStatistics(url);
        return record;
    }
}

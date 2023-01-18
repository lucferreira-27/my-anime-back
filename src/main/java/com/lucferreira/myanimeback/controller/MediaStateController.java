package com.lucferreira.myanimeback.controller;

import com.lucferreira.myanimeback.model.MediaState;
import com.lucferreira.myanimeback.service.ScrapeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/state")
public class MediaStateController {
    @Autowired
    private ScrapeService scrapeService;

    @GetMapping("/search")
    public ResponseEntity<MediaState> getState(@RequestParam(value = "url", required = true) String url){

        MediaState mediaState = scrapeService.getStatistics(url);
        return ResponseEntity.ok(mediaState);
    }
}

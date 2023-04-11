package com.lucferreira.myanimeback.controller;

import com.lucferreira.myanimeback.exception.WaybackException;
import com.lucferreira.myanimeback.exception.WaybackTimestampParseException;
import com.lucferreira.myanimeback.service.ScrapeService;
import com.lucferreira.myanimeback.service.WaybackService;
import com.lucferreira.myanimeback.service.wayback.ResponseSnapshot;
import com.lucferreira.myanimeback.service.wayback.WaybackMachineClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/wayback/snapshot")
public class SnapshotController {

    @Autowired
    private WaybackService waybackService;
    @Autowired
    private ScrapeService scrapeService;
    @GetMapping("/search")
    public ResponseEntity<List<ResponseSnapshot>> searchByUrl(@RequestParam(value = "url", required = true) String url, Optional<String> timestamp) throws WaybackException {
        if(timestamp.isPresent()){
            String strTimestamp = timestamp.get();
            List<ResponseSnapshot> responseSnapshots = waybackService.getSnapshotByTimestamp(url, strTimestamp);
            return ResponseEntity.ok(responseSnapshots);
        }
        List<ResponseSnapshot> responseSnapshots   =  waybackService.getSnapshotList(url);
        return ResponseEntity.ok(responseSnapshots);
    }
}

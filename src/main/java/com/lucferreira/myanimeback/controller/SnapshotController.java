package com.lucferreira.myanimeback.controller;

import com.lucferreira.myanimeback.exception.WaybackException;
import com.lucferreira.myanimeback.service.wayback.ResponseSnapshot;
import com.lucferreira.myanimeback.service.wayback.WaybackMachineClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/wayback/snapshot")
public class SnapshotController {

    @Autowired
    private WaybackMachineClient waybackClient;
    @GetMapping("")
    public ResponseEntity<ResponseSnapshot> search(@RequestParam(value = "url", required = true) String url, Optional<String> first, Optional<String> timestamp) throws WaybackException {
        if (first.isPresent() && Boolean.valueOf(first.get())) {
            ResponseSnapshot responseSnapshot  = waybackClient.getFirstSnapshot(url);
            return ResponseEntity.ok(responseSnapshot);
        }
        ResponseSnapshot responseSnapshot  = timestamp.isPresent() ? waybackClient.getSnapshot(url,timestamp.get()) : waybackClient.getSnapshot(url);
        return ResponseEntity.ok(responseSnapshot);
    }
}

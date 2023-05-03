package com.lucferreira.myanimeback.controller;

import com.lucferreira.myanimeback.model.Record;
import com.lucferreira.myanimeback.model.TopList;
import com.lucferreira.myanimeback.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wayback/record")
public class RecordController {
    @Autowired
    private RecordService recordService;


    @GetMapping("/media")
    public ResponseEntity<Record> getMediaRecord(@RequestParam(value = "url", required = true) String url){
        Record record = recordService.getMediaRecord(url);
        return ResponseEntity.ok(record);
    }

    @GetMapping("/toplist")
    public ResponseEntity<TopList> getTopListRecord(@RequestParam(value = "url", required = true) String url){
        TopList record = recordService.getTopListRecord(url);
        return ResponseEntity.ok(record);
    }
}

package com.lucferreira.myanimeback.controller;

import com.lucferreira.myanimeback.model.Record;
import com.lucferreira.myanimeback.service.RecordService;
import com.lucferreira.myanimeback.service.ScrapeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/wayback/record")
public class RecordController {
    @Autowired
    private RecordService recordService;


    @GetMapping("")
    public ResponseEntity<Record> getRecord(@RequestParam(value = "url", required = true) String url){
        Record record = recordService.getRecord(url);
        return ResponseEntity.ok(record);
    }
}

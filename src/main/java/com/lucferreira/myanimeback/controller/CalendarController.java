package com.lucferreira.myanimeback.controller;

import com.lucferreira.myanimeback.exception.WaybackException;
import com.lucferreira.myanimeback.service.WaybackService;
import com.lucferreira.myanimeback.service.wayback.CalendarSimpleItem;
import com.lucferreira.myanimeback.service.wayback.ResponseSnapshot;
import com.lucferreira.myanimeback.service.wayback.Timestamp;
import com.lucferreira.myanimeback.service.wayback.WaybackMachineClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/wayback/calendar/")
public class CalendarController {
    @Autowired
    private WaybackService waybackService;
    @GetMapping("/list")
    public ResponseEntity<List<CalendarSimpleItem>> list(String url, Optional<Integer> year) throws WaybackException {
       List<CalendarSimpleItem> responseSnapshots = waybackService.calendarListByYear(url,year.get());
       return ResponseEntity.ok(responseSnapshots);

    }
}

package com.lucferreira.myanimeback.service;

import com.lucferreira.myanimeback.service.wayback.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WaybackService {

    final WaybackMachineClient waybackMachineClient;

    @Autowired
    public WaybackService(WaybackMachineClient waybackMachineClient) {
        this.waybackMachineClient = waybackMachineClient;
    }

    public ResponseSnapshot getSnapshot(String url){
        return getSnapshotByTimestamp(url,"");
    }
    public ResponseSnapshot getSnapshotByTimestamp(String url, String timestamp){
        return waybackMachineClient.getSnapshot(url,timestamp);
    }
    public List<CalendarSimpleItem> calendarListByYear(String url, int year){
        List<CalendarSimpleItem> results = waybackMachineClient.getSnapshotsByYear(url,year);
        return  results;
    }
}

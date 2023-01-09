package com.lucferreira.myanimeback.service.wayback;

import com.lucferreira.myanimeback.exception.WaybackException;
import com.lucferreira.myanimeback.exception.WaybackTimestampParseException;
import com.lucferreira.myanimeback.exception.WaybackUnavailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class WaybackMachineClient {
    private final String waybackUrl = "http://archive.org/wayback/available?url=";
    private final RestTemplate restTemplate;

    @Autowired
    public WaybackMachineClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }



    public List<ResponseSnapshot> getSnapshotsByYear(String url, int year) throws WaybackException {

        List<ResponseSnapshot> responseSnapshots = new ArrayList<>();

        String urlTemplate = "https://web.archive.org/__wb/calendarcaptures/2?url=%s&date=%d&groupby=day";
        String endpoint = String.format(urlTemplate,url,year);
        CalendarCaptures calendarCaptures = requestCalendarCaptures(endpoint);
        List<Timestamp> timestamps = calendarCaptures.completeAllItemDate(year);

        for (Timestamp timestamp :  timestamps) {
            ResponseSnapshot responseSnapshot = getSnapshot(url,timestamp.toString());
            responseSnapshots.add(responseSnapshot);
        }



        return null;
    }

    public ResponseSnapshot getFirstSnapshot(String url) throws WaybackException {
        var firstTimestampOptions = Arrays.asList("0","-1","1970"); // These timestamps will be used to search for the earliest snapshot of the given url
        for (String timestamp : firstTimestampOptions){
            try{
                ResponseSnapshot responseSnapshot = getSnapshot(url,timestamp);
                return  responseSnapshot;
            }catch (WaybackUnavailableException e){
                continue;
            }
        }
        throw new WaybackUnavailableException("The first snapshot for this url was not found");
    }

    public ResponseSnapshot getSnapshot(String url) throws WaybackException {
        String endpoint = "http://archive.org/wayback/available?url=" + url;
        ResponseSnapshot snapshot = requestSnapshot(endpoint);
        return snapshot;
    }

    public ResponseSnapshot getSnapshot(String url, String  timestamp) throws WaybackException {

        String endpoint = "http://archive.org/wayback/available?url=" + url + "&timestamp=" + timestamp;
        ResponseSnapshot snapshot = requestSnapshot(endpoint);
        return snapshot;
    }

    private ResponseSnapshot requestSnapshot(String endpoint) throws WaybackException {
        ResponseEntity<WaybackResponse> response = restTemplate.getForEntity(endpoint, WaybackResponse.class);
        WaybackResponse body = response.getBody();
        if (body.getArchivedSnapshots() == null) {
            throw new WaybackUnavailableException();
        }
        Closest closest = body.getArchivedSnapshots().getClosest();
        if (closest == null || !closest.isAvailable()) {
            throw new WaybackUnavailableException();
        }
        ResponseSnapshot snapshot = getResponseSnapshot(closest);
        return snapshot;
    }
    private CalendarCaptures requestCalendarCaptures(String endpoint) throws WaybackException {
        ResponseEntity<CalendarCaptures> response = restTemplate.getForEntity(endpoint, CalendarCaptures.class);
        CalendarCaptures calendarCaptures = response.getBody();
        return calendarCaptures;
    }
    public ResponseSnapshot getResponseSnapshot(Closest closest) throws WaybackTimestampParseException{
        String snapshotUrl = closest.getUrl();
        String snapshotTimestamp = closest.getTimestamp();
        String snapshotStatus = closest.getStatus();
        ResponseSnapshot snapshot = new ResponseSnapshot(snapshotUrl, snapshotTimestamp, snapshotStatus);
        return  snapshot;
    }
}

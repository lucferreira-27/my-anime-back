package com.lucferreira.myanimeback.service.wayback;

import com.lucferreira.myanimeback.exception.WaybackException;
import com.lucferreira.myanimeback.exception.WaybackTimestampParseException;
import com.lucferreira.myanimeback.exception.WaybackUnavailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class WaybackMachineClient {
    private final String waybackUrl = "http://archive.org/wayback/available?url=";
    private final RestTemplate restTemplate;

    @Autowired
    public WaybackMachineClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }



    public List<CalendarSimpleItem> getSnapshotsByYear(String url, int year) throws WaybackException {

        List<ResponseSnapshot> responseSnapshots = new ArrayList<>();

        String urlTemplate = "https://web.archive.org/__wb/calendarcaptures/2?url=%s&date=%d&groupby=day";
        String endpoint = String.format(urlTemplate,url,year);
        CalendarCaptures calendarCaptures = requestCalendarCaptures(endpoint);
        List<CalendarSimpleItem> calendarSimpleItems = calendarCaptures.completeAllItemDate(year);
        return calendarSimpleItems;

        /*
        for (Timestamp timestamp :  timestamps) {
            ResponseSnapshot responseSnapshot = getSnapshot(url,timestamp.toString());
            responseSnapshots.add(responseSnapshot);
        }

        return responseSnapshots;
     */
    }


    public ResponseSnapshot getFirstSnapshot(String url) throws WaybackException {
        // These timestamps will be used to search for the earliest snapshot of the given url
        var firstTimestampOptions = Arrays.asList("0","-1","1970");
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
        ResponseSnapshot snapshot = requestSnapshot(endpoint,"");
        return snapshot;
    }

    public ResponseSnapshot getSnapshot(String url, String  timestamp) throws WaybackException {

        String endpoint = "http://archive.org/wayback/available?url=" + url + (!timestamp.isEmpty() ? "&timestamp=" + timestamp : "");
        ResponseSnapshot snapshot = requestSnapshot(endpoint,timestamp);
        return snapshot;
    }

    private ResponseSnapshot requestSnapshot(String endpoint, String timestamp) throws WaybackException {


        ResponseEntity<WaybackResponse> response = restTemplate.getForEntity(endpoint, WaybackResponse.class);
        final HttpStatusCode statusCode = response.getStatusCode();
        final String serviceErrorMsg = String.format("Wayback Machine service is currently unavailable. Response status: %s",statusCode);

        if(response.getStatusCode().is5xxServerError() || response.getStatusCode().is4xxClientError()){
            throw new WaybackUnavailableException(serviceErrorMsg);
        }

        WaybackResponse body = response.getBody();
        if (body == null) {
           String unexpectedBodyMessage =
                    String.format("The request to the Wayback Machine service returned a null body. " +
                            "This may indicate that the service is currently unavailable or that the requested timestamp is not present in the archive. " +
                            "Please try again later or check the validity of the timestamp and endpoint. timestamp: %s, endpoint: %s"
                            ,timestamp.isEmpty() ? null : timestamp,endpoint);
            throw new WaybackUnavailableException(unexpectedBodyMessage);
        }
        final String notFoundErrorMsg = String.format("Timestamp '%s' not found in Wayback Machine's archive for endpoint: %s",timestamp, endpoint);

        if (body.getArchivedSnapshots() == null) {
            throw new WaybackUnavailableException(notFoundErrorMsg);
        }
        Closest closest = body.getArchivedSnapshots().getClosest();
        if (closest == null || !closest.isAvailable()) {
            throw new WaybackUnavailableException(notFoundErrorMsg);
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

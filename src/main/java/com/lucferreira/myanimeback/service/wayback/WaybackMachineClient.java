package com.lucferreira.myanimeback.service.wayback;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucferreira.myanimeback.exception.WaybackException;
import com.lucferreira.myanimeback.exception.WaybackTimestampParseException;
import com.lucferreira.myanimeback.exception.WaybackUnavailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.sql.Time;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WaybackMachineClient {
    private final RestTemplate restTemplate;

    @Autowired
    public WaybackMachineClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public Optional<List<ResponseSnapshot>> getTimeMap(String url) throws WaybackException {
        String endpoint = "https://web.archive.org/web/timemap/json?url=" + url;
        ArrayList<ArrayList<String>> body = fetchSnapshotBody(endpoint);

        if (body == null || body.size() == 0) {
            throw new WaybackUnavailableException("No snapshots found for the URL: " + url);
        }

        List<String> keys = body.get(0);
        List<Map<String, String>> snapshotList = createSnapshotList(body, keys);
        List<ResponseSnapshot> responseSnapshots = convertToResponseSnapshots(snapshotList);

        return Optional.of(responseSnapshots);
    }
    private Optional<ResponseSnapshot> getTimeMap(String url, String targetTimestamp) {
        Optional<List<ResponseSnapshot>> optional = getTimeMap(url);
        if (optional.isEmpty()) {
            return Optional.empty();
        }

        List<ResponseSnapshot> responseSnapshots = optional.get();
        Timestamp targetTimestampObj = Timestamp.valueOf(targetTimestamp);

        return responseSnapshots
                .stream()
                .min(Comparator.comparingLong(responseSnapshot -> getTimestampDifference(responseSnapshot.getTimestamp(), targetTimestampObj)));
    }

    private long getTimestampDifference(Timestamp timestamp1, Timestamp timestamp2) {
        long timestamp1Long = timestamp1.getDate().getTime();
        long timestamp2Long = timestamp2.getDate().getTime();

        return Math.abs(timestamp1Long - timestamp2Long);
    }


    public List<ResponseSnapshot> getSnapshotList(String url) throws WaybackException {

        Optional<List<ResponseSnapshot>> optional = getTimeMap(url);
        if(optional.isPresent()){
            List<ResponseSnapshot> responseSnapshots = optional.get();
            return responseSnapshots;
        }
        throw new WaybackException(HttpStatus.NOT_FOUND, "No snapshots found for the URL: " + url);
    }


    public List<ResponseSnapshot> getSnapshotsInRange(String url, String beginTimestamp, Optional<String> optionalEnd) throws WaybackException {
        Optional<List<ResponseSnapshot>> optional = getTimeMap(url);
        if (optional.isEmpty()) {
            throw new WaybackException(HttpStatus.NOT_FOUND, "No snapshots found for the URL within the specified time range: " + url);
        }
        List<ResponseSnapshot> responseSnapshots = optional.get();
        List<ResponseSnapshot> filteredSnapshots = filterSnapshots(responseSnapshots, beginTimestamp, optionalEnd);
        if (filteredSnapshots.isEmpty()) {
            throw new WaybackException(HttpStatus.NOT_FOUND, "No snapshots found for the URL within the specified time range: " + url);
        }
        return filteredSnapshots;
    }
    private List<ResponseSnapshot> filterSnapshots(List<ResponseSnapshot> responseSnapshots, String beginTimestamp, Optional<String> endTimestamp) {
        return responseSnapshots.stream()
                .filter(snapshot -> {
                    String timestamp = snapshot.getTimestamp().getOriginalValue();
                    return timestamp.compareTo(beginTimestamp) >= 0 && (endTimestamp.isEmpty()  || timestamp.compareTo(endTimestamp.get()) <= 0);
                })
                .collect(Collectors.toList());
    }

    private ResponseSnapshot getResponseSnapshot(Closest closest) throws WaybackTimestampParseException{
        String snapshotUrl = closest.getUrl();
        String snapshotTimestamp = closest.getTimestamp();
        String snapshotStatus = closest.getStatus();
        ResponseSnapshot snapshot = new ResponseSnapshot(snapshotUrl, snapshotTimestamp, snapshotStatus);
        return  snapshot;
    }

    private ArrayList fetchSnapshotBody(String endpoint) {
        ResponseEntity<ArrayList> response = restTemplate.getForEntity(endpoint, ArrayList.class);
        final HttpStatusCode statusCode = response.getStatusCode();
        final String serviceErrorMsg = String.format("Wayback Machine service is currently unavailable. Response status: %s",statusCode);
        if(response.getStatusCode().is5xxServerError() || response.getStatusCode().is4xxClientError()){
            throw new WaybackUnavailableException(serviceErrorMsg);
        }
        return response.getBody();
    }
    private List<Map<String, String>> createSnapshotList(ArrayList<ArrayList<String>> body, List<String> keys) {
        List<Map<String, String>> snapshotList = new ArrayList<>();
        for (int i = 1; i < body.size(); i++) {
            Map<String, String> snapshot = createSnapshot(keys, body.get(i));
            snapshotList.add(snapshot);
        }
        return snapshotList;
    }
    private Map<String, String> createSnapshot(List<String> keys, List<String> values) {
        Map<String, String> snapshot = new HashMap<>();
        for (int j = 0; j < keys.size(); j++) {
            snapshot.put(keys.get(j), values.get(j));
        }
        return snapshot;
    }
    private List<ResponseSnapshot> convertToResponseSnapshots(List<Map<String, String>> snapshotList) {
        List<ResponseSnapshot> responseSnapshots = new ArrayList<>();
        for (Map<String, String> snapshot : snapshotList) {
            String timestamp = snapshot.get("timestamp");
            String original = snapshot.get("original");
            String statusCode = snapshot.get("statuscode");
            String snapshotUrl = String.format("https://web.archive.org/web/%s/%s", timestamp, original);
            ResponseSnapshot responseSnapshot = new ResponseSnapshot(snapshotUrl, timestamp, statusCode);
            responseSnapshots.add(responseSnapshot);
        }
        return responseSnapshots;
    }
}

package com.lucferreira.myanimeback.service.wayback;

import com.lucferreira.myanimeback.exception.WaybackException;
import com.lucferreira.myanimeback.exception.WaybackUnavailableException;
import com.lucferreira.myanimeback.model.media.Media;
import com.lucferreira.myanimeback.model.snapshot.ResponseSnapshot;
import com.lucferreira.myanimeback.model.snapshot.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class WaybackSnapshotFetcher {
    private final String TIME_MAP_URL= "https://web.archive.org/web/timemap/json?url=";
    private final RestTemplate restTemplate;

    @Autowired
    public WaybackSnapshotFetcher(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    public Optional<List<ResponseSnapshot>> getTimeMap(String url, Long malId) throws WaybackException {
        String endpoint = TIME_MAP_URL + url;
        ArrayList<ArrayList<String>> body = fetchSnapshotBody(endpoint);

        if (body == null || body.size() == 0) {
            throw new WaybackUnavailableException("No snapshots found for the URL: " + url);
        }

        List<String> keys = body.get(0);
        List<Map<String, String>> snapshotList = createSnapshotList(body, keys);
        List<ResponseSnapshot> responseSnapshots = convertToResponseSnapshots(snapshotList,malId);

        return Optional.of(responseSnapshots);
    }
    public Optional<ResponseSnapshot> getTimeMap(String url, String targetTimestamp, Long malId) {
        Optional<List<ResponseSnapshot>> optional = getTimeMap(url,malId);
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
    private List<ResponseSnapshot> convertToResponseSnapshots(List<Map<String, String>> snapshotList, Long malId) {
        List<ResponseSnapshot> responseSnapshots = new ArrayList<>();
        for (Map<String, String> snapshot : snapshotList) {
            String timestamp = snapshot.get("timestamp");
            String original = snapshot.get("original");
            String statusCode = snapshot.get("statuscode");
            String snapshotUrl = String.format("https://web.archive.org/web/%s/%s", timestamp, original);
            ResponseSnapshot responseSnapshot = new ResponseSnapshot(snapshotUrl, timestamp, statusCode,malId);
            responseSnapshots.add(responseSnapshot);
        }
        return responseSnapshots;
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
}

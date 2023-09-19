package com.lucferreira.myanimeback.service.wayback;

import com.lucferreira.myanimeback.exception.WaybackException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class WaybackMachineClient {
    private final RestTemplate restTemplate;
    private final WaybackSnapshotFetcher snapshotFetcher;

    @Autowired
    public WaybackMachineClient(RestTemplate restTemplate, WaybackSnapshotFetcher snapshotFetcher) {
        this.restTemplate = restTemplate;
        this.snapshotFetcher = snapshotFetcher;
    }

    public List<ResponseSnapshot> getSnapshotList(String url) throws WaybackException {

        Optional<List<ResponseSnapshot>> optional = snapshotFetcher.getTimeMap(url);
        if(optional.isPresent()){
            List<ResponseSnapshot> responseSnapshots = optional.get();
            return responseSnapshots;
        }
        throw new WaybackException(HttpStatus.NOT_FOUND, "No snapshots found for the URL: " + url);
    }

    public List<ResponseSnapshot> getSnapshotsInRange(String url, String beginTimestamp, Optional<String> optionalEnd) throws WaybackException {
        Optional<List<ResponseSnapshot>> optional = snapshotFetcher.getTimeMap(url);
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

}

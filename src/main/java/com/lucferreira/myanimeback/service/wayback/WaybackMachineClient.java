package com.lucferreira.myanimeback.service.wayback;

import com.lucferreira.myanimeback.exception.WaybackException;
import com.lucferreira.myanimeback.model.media.Media;
import com.lucferreira.myanimeback.model.snapshot.ResponseSnapshot;

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

public List<ResponseSnapshot> getSnapshotList(Media media) throws WaybackException {
    var urlWithId = media.getMyanimelistUrl();
    var urlWithoutId = urlWithId.substring(0, urlWithId.lastIndexOf("/"));

    Optional<List<ResponseSnapshot>> optionalWithId = snapshotFetcher.getTimeMap(urlWithId, media.getMalId());
    Optional<List<ResponseSnapshot>> optionalWithoutId = snapshotFetcher.getTimeMap(urlWithoutId, media.getMalId());

    List<ResponseSnapshot> responseSnapshotsWithId = optionalWithId.orElse(Collections.emptyList());
    List<ResponseSnapshot> responseSnapshotsWithoutId = optionalWithoutId.orElse(Collections.emptyList());

    List<ResponseSnapshot> mergedSnapshots = new ArrayList<>();
    mergedSnapshots.addAll(responseSnapshotsWithId);
    mergedSnapshots.addAll(responseSnapshotsWithoutId);
    mergedSnapshots = mergedSnapshots.stream()
                                     .sorted(Comparator.comparing(snapshot -> snapshot.getTimestamp().getDate()))
                                     .collect(Collectors.toList());
    if (mergedSnapshots.isEmpty()) {
        throw new WaybackException(HttpStatus.NOT_FOUND, "No snapshots found for the URL: " + media.getMyanimelistUrl());
    }

    return mergedSnapshots;
}

    public List<ResponseSnapshot> getSnapshotsInRange(Media media, String beginTimestamp, Optional<String> optionalEnd)
            throws WaybackException {
        Optional<List<ResponseSnapshot>> optional = snapshotFetcher.getTimeMap(media.getMyanimelistUrl(),
                media.getMalId());
        if (optional.isEmpty()) {
            throw new WaybackException(HttpStatus.NOT_FOUND,
                    "No snapshots found for the URL within the specified time range: " + media.getMyanimelistUrl());
        }
        List<ResponseSnapshot> responseSnapshots = optional.get();
        List<ResponseSnapshot> filteredSnapshots = filterSnapshots(responseSnapshots, beginTimestamp, optionalEnd);
        if (filteredSnapshots.isEmpty()) {
            throw new WaybackException(HttpStatus.NOT_FOUND,
                    "No snapshots found for the URL within the specified time range: " + media.getMyanimelistUrl());
        }
        return filteredSnapshots;
    }

    private List<ResponseSnapshot> filterSnapshots(List<ResponseSnapshot> responseSnapshots, String beginTimestamp,
            Optional<String> endTimestamp) {
        return responseSnapshots.stream()
                .filter(snapshot -> {
                    String timestamp = snapshot.getTimestamp().getOriginalValue();
                    return timestamp.compareTo(beginTimestamp) >= 0
                            && (endTimestamp.isEmpty() || timestamp.compareTo(endTimestamp.get()) <= 0);
                })
                .collect(Collectors.toList());
    }

}

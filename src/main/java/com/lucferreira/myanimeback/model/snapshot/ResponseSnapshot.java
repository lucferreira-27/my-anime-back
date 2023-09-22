package com.lucferreira.myanimeback.model.snapshot;

import com.lucferreira.myanimeback.exception.WaybackTimestampParseException;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class ResponseSnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;
    private Long malId;
    @OneToOne(cascade = CascadeType.PERSIST)
    private Timestamp timestamp;
    private String snapshotStatus;
    private Boolean available = false;
    private Integer numberOfRequests = 0;

    public ResponseSnapshot() {

    }

    public ResponseSnapshot(String url, String timestamp, String snapshotStatus, Long malId) throws WaybackTimestampParseException {
        this.url = url;
        this.timestamp = new Timestamp(timestamp);
        this.snapshotStatus = snapshotStatus;
        this.malId = malId;
    }
    public Boolean getAvailable() {
        return available;
    }
    public void setAvailable(Boolean available) {
        this.available = available;
    }
    public Integer getNumberOfRequests() {
        return numberOfRequests;
    }
    public void setNumberOfRequests(Integer numberOfRequests) {
        this.numberOfRequests = numberOfRequests;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getSnapshotStatus() {
        return snapshotStatus;
    }

    public void setSnapshotStatus(String snapshotStatus) {
        this.snapshotStatus = snapshotStatus;
    }
    public Long getMalId() {
        return malId;
    }
    public void setMalId(Long malId) {
        this.malId = malId;
    }
}

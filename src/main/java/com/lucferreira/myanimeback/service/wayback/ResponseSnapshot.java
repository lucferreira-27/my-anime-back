package com.lucferreira.myanimeback.service.wayback;

import com.lucferreira.myanimeback.exception.WaybackTimestampParseException;


public class ResponseSnapshot {
    private String url;
    private Timestamp timestamp;
    private String snapshotStatus;

    ResponseSnapshot(String url, String timestamp, String snapshotStatus) throws WaybackTimestampParseException {
        this.url = url;
        this.timestamp = new Timestamp(timestamp);
        this.snapshotStatus = snapshotStatus;
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
}

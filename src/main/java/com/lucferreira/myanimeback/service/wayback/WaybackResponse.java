package com.lucferreira.myanimeback.service.wayback;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WaybackResponse {
    private String url;
    private ArchivedSnapshots archivedSnapshots;
    private String timestamp;
    @JsonProperty("archived_snapshots")
    public ArchivedSnapshots getArchivedSnapshots() {
        return archivedSnapshots;
    }
    public void setArchivedSnapshots(ArchivedSnapshots archived_snapshots) {
        this.archivedSnapshots = archived_snapshots;
    }
    @JsonProperty("url")
    public String getUrl() {
        return this.url; }
    public void setUrl(String url) {
        this.url = url; }

    @JsonProperty("timestamp")
    public String getTimestamp() {
        return this.timestamp; }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp; }

    @Override
    public String toString() {
        return "WaybackResponse{" +
                "archivedSnapshots=" + archivedSnapshots +
                ", url='" + url + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}

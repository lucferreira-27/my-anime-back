package com.lucferreira.myanimeback.model;

import com.lucferreira.myanimeback.model.snapshot.ResponseSnapshot;

public class ArchiveResponse {
    private int total;
    private int totalAvailable;
    private int totalUnavailable;
    private ResponseSnapshot first;
    private ResponseSnapshot last;
    private Boolean newMedia;
    private Boolean complete;
    private Long mediaId;

    public ResponseSnapshot getFirst() {
        return first;
    }
    public void setFirst(ResponseSnapshot first) {
        this.first = first;
    }
    public ResponseSnapshot getLast() {
        return last;
    }
    public void setLast(ResponseSnapshot last) {
        this.last = last;
    }
    public int getTotal() {
        return total;
    }
    public void setTotal(int total) {
        this.total = total;
    }
    public Boolean getNewMedia(){
        return newMedia;
    }
    public void setNewMedia(Boolean newMedia) {
        this.newMedia = newMedia;
    }
    public void setMediaId(Long mediaId) {
        this.mediaId = mediaId;
    }
    public Long getMediaId() {
        return mediaId;
    }
    public Boolean getComplete() {
        return complete;
    }
    public void setComplete(Boolean complete) {
        this.complete = complete;
    }
    public int getTotalAvailable() {
        return totalAvailable;
    }
    public void setTotalAvailable(int totalAvailable) {
        this.totalAvailable = totalAvailable;
    }
    public void setTotalUnavailable(int totalUnavailable) {
        this.totalUnavailable = totalUnavailable;
    }
    public int getTotalUnavailable() {
        return totalUnavailable;
    }

}

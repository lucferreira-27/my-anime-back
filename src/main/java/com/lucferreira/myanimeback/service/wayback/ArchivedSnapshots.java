package com.lucferreira.myanimeback.service.wayback;

import com.fasterxml.jackson.annotation.JsonProperty;

class ArchivedSnapshots {
    public Closest closest;
    @JsonProperty("closest")
    public Closest getClosest() {
        return closest;
    }

    public void setClosest(Closest closest) {
        this.closest = closest;
    }

    @Override
    public String toString() {
        return "ArchivedSnapshots{" +
                "closest=" + closest +
                '}';
    }
}


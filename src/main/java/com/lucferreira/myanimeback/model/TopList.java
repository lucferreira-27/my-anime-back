package com.lucferreira.myanimeback.model;

import com.lucferreira.myanimeback.service.wayback.Timestamp;

import java.util.*;

public class TopList {
    private final List<TopListItem> recordList = new ArrayList<>();
    private TopType topType;
    private TopSubtype topSubtype;
    private Timestamp timestamp;

    public TopList(TopType topType, TopSubtype topSubtype, Timestamp timestamp){
        this.topType = topType;
        this.topSubtype = topSubtype;
        this.timestamp = timestamp;
    }

    public void addTopListItemToMap(TopListItem record){
        if (record == null){
            throw new NullPointerException("The record cannot be null");
        }
        recordList.add(record);
    }

    public List<TopListItem> getRecordList() {
        return recordList;
    }

    public void setTopType(TopType topType) {
        this.topType = topType;
    }

    public TopType getTopType() {
        return topType;
    }

    public void setTopSubtype(TopSubtype topSubtype) {
        this.topSubtype = topSubtype;
    }

    public TopSubtype getTopSubtype() {
        return topSubtype;
    }

    public enum TopType {
        ANIME,
        MANGA
    }

    public enum TopSubtype {
        ALL,
        AIRING,
        UPCOMING,
        TV,
        MOVIE,
        OVA,
        ONA,
        SPECIAL,
        POPULAR,
        FAVORITE,
        MANGA,
        ONESHOTS,
        DOUJINSHI,
        LIGHT_NOVELS,
        NOVELS,
        MANHWA,
        MANHUA,
    }
}


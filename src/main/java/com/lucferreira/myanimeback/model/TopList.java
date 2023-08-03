package com.lucferreira.myanimeback.model;

import java.util.*;

public class TopList {
    private final List<TopListItem> recordList = new ArrayList<>();
    private TopType topType;
    private TopSubtype topSubtype;

    private String archiveUrl;

    public TopList(TopType topType, TopSubtype topSubtype, String archiveUrl){
        this.topType = topType;
        this.topSubtype = topSubtype;
        this.archiveUrl = archiveUrl;
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

    public String getArchiveUrl() {
        return archiveUrl;
    }

    public enum TopType {
        ANIME,
        MANGA
    }

    public enum TopSubtype {
        ALL(),
        AIRING("airing"),
        UPCOMING("upcoming"),
        TV("tv"),
        MOVIE("movie"),
        OVA("ova"),
        ONA("ona"),
        SPECIAL("special"),
        POPULAR("bypopularity"),
        FAVORITE("favorite"),
        MANGA("manga"),
        ONE_SHOTS("oneshots"),
        DOUJINSHI("doujin"),
        LIGHT_NOVELS("lightnovels"),
        NOVELS("novels"),
        MANHWA("manhwa"),
        MANHUA("manhua");

        private final List<String> possiblesName;


        TopSubtype(String... possiblesName) {
            this.possiblesName = Arrays.asList(possiblesName);
        }

        public List<String> getPossiblesName() {
            return possiblesName;
        }
    }
}


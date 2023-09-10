package com.lucferreira.myanimeback.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Record {
    private Double scoreValue;
    private Integer totalVotes;
    private Integer popularity;
    private Integer members;
    private Integer ranked;
    private Integer favorites;
    private String archiveUrl;

    private Date archiveDate;

    public Double getScoreValue() {
        return scoreValue;
    }

    public void setScoreValue(Double scoreValue) {
        this.scoreValue = scoreValue;
    }

    public Integer getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(Integer totalVotes) {
        this.totalVotes = totalVotes;
    }

    public Integer getPopularity() {
        return popularity;
    }

    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }

    public Integer getMembers() {
        return members;
    }

    public void setMembers(Integer members) {
        this.members = members;
    }

    public Integer getFavorites() {
        return favorites;
    }

    public void setFavorites(Integer favorites) {
        this.favorites = favorites;
    }

    public String getArchiveUrl() {
        return archiveUrl;
    }

    public void setRanked(Integer ranked) {
        this.ranked = ranked;
    }

    public Integer getRanked() {
        return ranked;
    }

    public void setArchiveUrl(String archiveUrl) {
        this.archiveUrl = archiveUrl;
    }

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")

    public Date getArchiveDate() {
        return archiveDate;
    }

    private Record(Double scoreValue, Integer totalVotes, Integer popularity, Integer ranked , Integer members, Integer favorites, String archiveUrl, Date archiveDate) {
        this.scoreValue = scoreValue;
        this.totalVotes = totalVotes;
        this.popularity = popularity;
        this.members = members;
        this.favorites = favorites;
        this.archiveUrl = archiveUrl;
        this.ranked = ranked;
        this.archiveDate = archiveDate;
    }

    public static class Builder {
        private Double scoreValue;
        private Integer totalVotes;
        private Integer popularity;
        private Integer members;
        private Integer favorites;
        private Integer ranked;
        private String archiveUrl;
        private Date archiveDate;
        public Builder scoreValue(Double scoreValue) {
            this.scoreValue = scoreValue;
            return this;
        }

        public Builder totalVotes(Integer totalVotes) {
            this.totalVotes = totalVotes;
            return this;
        }

        public Builder popularity(Integer popularity) {
            this.popularity = popularity;
            return this;
        }
        public Builder ranked(Integer ranked) {
            this.ranked = ranked;
            return this;
        }
        public Builder members(Integer members) {
            this.members = members;
            return this;
        }

        public Builder favorites(Integer favorites) {
            this.favorites = favorites;
            return this;
        }

        public Builder archiveUrl(String archiveUrl) {
            this.archiveUrl = archiveUrl;
            return this;
        }
        public Builder archiveDate(Date archiveDate) {
            this.archiveDate = archiveDate;
            return this;
        }
        public Record build() {
            return new Record(scoreValue, totalVotes, popularity, ranked,members, favorites, archiveUrl,archiveDate);
        }
    }
}

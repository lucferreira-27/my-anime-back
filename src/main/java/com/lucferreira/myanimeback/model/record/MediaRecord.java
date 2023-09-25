package com.lucferreira.myanimeback.model.record;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lucferreira.myanimeback.model.media.Media;
import com.lucferreira.myanimeback.model.snapshot.ResponseSnapshot;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.util.Date;

@Entity
public class MediaRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double scoreValue;
    private Integer totalVotes;
    private Integer popularity;
    private Integer members;
    private Integer ranked;
    private Integer favorites;
    @Column(unique = true)
    private String archiveUrl;
    private Date archiveDate;

    @ManyToOne
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private Media media;

    @OneToOne(cascade = CascadeType.MERGE)
    @JsonManagedReference
    private ResponseSnapshot responseSnapshot;

    public MediaRecord() {

    }

    private MediaRecord(Double scoreValue, Integer totalVotes, Integer popularity, Integer ranked, Integer members,
            Integer favorites, String archiveUrl, Date archiveDate, Media media) {
        this.scoreValue = scoreValue;
        this.totalVotes = totalVotes;
        this.popularity = popularity;
        this.members = members;
        this.favorites = favorites;
        this.archiveUrl = archiveUrl;
        this.ranked = ranked;
        this.archiveDate = archiveDate;
        this.media = media;
    }

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

    public void setArchiveDate(Date archiveDate) {
        this.archiveDate = archiveDate;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public ResponseSnapshot getResponseSnapshot() {
        return responseSnapshot;
    }

    public void setResponseSnapshot(ResponseSnapshot responseSnapshot) {
        this.responseSnapshot = responseSnapshot;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
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
        private Media media;

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

        public Builder media(Media media) {
            this.media = media;
            return this;
        }

        public MediaRecord build() {
            return new MediaRecord(scoreValue, totalVotes, popularity, ranked, members, favorites, archiveUrl,
                    archiveDate,
                    media);
        }
    }

}

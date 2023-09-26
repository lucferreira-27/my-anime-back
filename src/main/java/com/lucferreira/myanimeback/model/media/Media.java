package com.lucferreira.myanimeback.model.media;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.Date;
import java.util.List;

import com.lucferreira.myanimeback.model.record.MediaRecord;
@Entity
public class Media  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int members;
    private double score;
    private int scoreCount;
    private int rankedPosition;
    private int popularityPosition;
    private int favoritesCount;
    private String imageUrl;
    @Column(unique = true)
    private String myanimelistUrl;
    private String type;
    private Date startDate;
    private Date endDate;
    private String status;
    private Long malId;
    @OneToMany
    private List<MediaRecord> records;
    private Date lastUpdate;
    private boolean updated;
    private boolean busy;

    public Media(){

    }
    public Media(Long id, String name, int members, double score, int scoreCount, int rankedPosition, int popularityPosition, int favoritesCount, String imageUrl, String myanimelistUrl, String type, Date startDate, Date endDate, String status, String mediaType, Long malId) {
        this.id = id;
        this.name = name;
        this.members = members;
        this.score = score;
        this.scoreCount = scoreCount;
        this.rankedPosition = rankedPosition;
        this.popularityPosition = popularityPosition;
        this.favoritesCount = favoritesCount;
        this.imageUrl = imageUrl;
        this.myanimelistUrl = myanimelistUrl;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.mediaType = mediaType;
        this.malId = malId;
    }

    private String mediaType;

    public static Media fromForm(MediaForm mediaForm, Long id) {

        return new Media.Builder()
                .id(id)
                .name(mediaForm.getName())
                .members(mediaForm.getMembers())
                .score(mediaForm.getScore())
                .scoreCount(mediaForm.getScoreCount())
                .rankedPosition(mediaForm.getRankedPosition())
                .popularityPosition(mediaForm.getPopularityPosition())
                .favoritesCount(mediaForm.getFavoritesCount())
                .imageUrl(mediaForm.getImageUrl())
                .myanimelistUrl(mediaForm.getMyanimelistUrl())
                .type(mediaForm.getType())
                .malId(mediaForm.getMalId())
                .startDate(mediaForm.getStartDate())
                .endDate(mediaForm.getEndDate())
                .status(mediaForm.getStatus())
                .mediaType(mediaForm.getMediaType())
                .build();
    }
    public static Media fromForm(MediaForm mediaForm) {

        return new Media.Builder()
                .name(mediaForm.getName())
                .members(mediaForm.getMembers())
                .score(mediaForm.getScore())
                .scoreCount(mediaForm.getScoreCount())
                .rankedPosition(mediaForm.getRankedPosition())
                .popularityPosition(mediaForm.getPopularityPosition())
                .favoritesCount(mediaForm.getFavoritesCount())
                .imageUrl(mediaForm.getImageUrl())
                .myanimelistUrl(mediaForm.getMyanimelistUrl())
                .malId(mediaForm.getMalId())
                .type(mediaForm.getType())
                .startDate(mediaForm.getStartDate())
                .endDate(mediaForm.getEndDate())
                .status(mediaForm.getStatus())
                .mediaType(mediaForm.getMediaType())
                .build();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMembers() {
        return members;
    }

    public double getScore() {
        return score;
    }


    public int getScoreCount() {
        return scoreCount;
    }

    public int getRankedPosition() {
        return rankedPosition;
    }


    public int getPopularityPosition() {
        return popularityPosition;
    }

    public int getFavoritesCount() {
        return favoritesCount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getMyanimelistUrl() {
        return myanimelistUrl;
    }


    public String getType() {
        return type;
    }

    public Date getStartDate() {
        return startDate;
    }


    public Date getEndDate() {
        return endDate;
    }


    public String getStatus() {
        return status;
    }

    public String getMediaType() {
        return mediaType;
    }
    public Date getLastUpdate() {
        return lastUpdate;
    }
    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    public List<MediaRecord> getRecords() {
        return records;
    }
    public void setRecords(List<MediaRecord> records) {
        this.records = records;
    }
    public boolean isUpdated() {
        return updated;
    }
    public void setUpdated(boolean updated) {
        this.updated = updated;
    }
    public void setBusy(boolean busy) {
        this.busy = busy;
    }
    public boolean isBusy() {
        return busy;
    }

    public static MediaDto toDto(MediaForm mediaForm) {
        return new MediaDto.Builder()
                .name(mediaForm.getName())
                .members(mediaForm.getMembers())
                .score(mediaForm.getScore())
                .scoreCount(mediaForm.getScoreCount())
                .rankedPosition(mediaForm.getRankedPosition())
                .popularityPosition(mediaForm.getPopularityPosition())
                .favoritesCount(mediaForm.getFavoritesCount())
                .imageUrl(mediaForm.getImageUrl())
                .myanimelistUrl(mediaForm.getMyanimelistUrl())
                .type(mediaForm.getType())
                .startDate(mediaForm.getStartDate())
                .endDate(mediaForm.getEndDate())
                .status(mediaForm.getStatus())
                .build();
    }
    public static MediaDto toDto(Media media) {
        return new MediaDto.Builder()
                .id(media.getId())
                .name(media.getName())
                .members(media.getMembers())
                .score(media.getScore())
                .scoreCount(media.getScoreCount())
                .rankedPosition(media.getRankedPosition())
                .popularityPosition(media.getPopularityPosition())
                .favoritesCount(media.getFavoritesCount())
                .imageUrl(media.getImageUrl())
                .myanimelistUrl(media.getMyanimelistUrl())
                .type(media.getType())
                .startDate(media.getStartDate())
                .endDate(media.getEndDate())
                .mediaType(media.getMediaType())
                .status(media.getStatus())
                .build();
    }

    public static class Builder {

        private Long id;
        private String name;
        private int members;
        private double score;
        private int scoreCount;
        private int rankedPosition;
        private int popularityPosition;
        private int favoritesCount;
        private String imageUrl;
        private String myanimelistUrl;
        private String type;
        private Date startDate;
        private Date endDate;
        private String status;
        private String mediaType;
        private Long malId;

        public Media build() {
            return new Media(id, name, members, score, scoreCount, rankedPosition, popularityPosition, favoritesCount, imageUrl, myanimelistUrl, type, startDate, endDate, status, mediaType,malId);
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder members(int members) {
            this.members = members;
            return this;
        }

        public Builder score(double score) {
            this.score = score;
            return this;
        }

        public Builder scoreCount(int scoreCount) {
            this.scoreCount = scoreCount;
            return this;
        }

        public Builder rankedPosition(int rankedPosition) {
            this.rankedPosition = rankedPosition;
            return this;
        }

        public Builder popularityPosition(int popularityPosition) {
            this.popularityPosition = popularityPosition;
            return this;
        }

        public Builder favoritesCount(int favoritesCount) {
            this.favoritesCount = favoritesCount;
            return this;
        }

        public Builder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder myanimelistUrl(String myanimelistUrl) {
            this.myanimelistUrl = myanimelistUrl;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder startDate(Date startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder endDate(Date endDate) {
            this.endDate = endDate;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder mediaType(String mediaType) {
            this.mediaType = mediaType;
            return this;
        }

        public Builder id(long id) {
            this.id = id;
            return this;
        }
        public Builder malId(long malId){
            this.malId = malId;
            return this;

        }
    }

    public Long getMalId() {
        return malId;
    }
}

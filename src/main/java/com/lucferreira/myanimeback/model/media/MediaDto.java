package com.lucferreira.myanimeback.model.media;

import java.util.Date;

public class MediaDto {
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
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMembers() {
        return members;
    }

    public void setMembers(int members) {
        this.members = members;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getScoreCount() {
        return scoreCount;
    }

    public void setScoreCount(int scoreCount) {
        this.scoreCount = scoreCount;
    }

    public int getRankedPosition() {
        return rankedPosition;
    }

    public void setRankedPosition(int rankedPosition) {
        this.rankedPosition = rankedPosition;
    }

    public int getPopularityPosition() {
        return popularityPosition;
    }

    public void setPopularityPosition(int popularityPosition) {
        this.popularityPosition = popularityPosition;
    }

    public int getFavoritesCount() {
        return favoritesCount;
    }

    public void setFavoritesCount(int favoritesCount) {
        this.favoritesCount = favoritesCount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMyanimelistUrl() {
        return myanimelistUrl;
    }

    public void setMyanimelistUrl(String myanimelistUrl) {
        this.myanimelistUrl = myanimelistUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public static class Builder {
        private MediaDto mediaDto = new MediaDto();
        public MediaDto build() {
            return mediaDto;
        }

        public Builder name(String name) {
            mediaDto.setName(name);
            return this;
        }

        public Builder members(int members) {
            mediaDto.setMembers(members);
            return this;
        }

        public Builder score(double score) {
            mediaDto.setScore(score);
            return this;
        }

        public Builder scoreCount(int scoreCount) {
            mediaDto.setScoreCount(scoreCount);
            return this;
        }

        public Builder rankedPosition(int rankedPosition) {
            mediaDto.setRankedPosition(rankedPosition);
            return this;
        }

        public Builder popularityPosition(int popularityPosition) {
            mediaDto.setPopularityPosition(popularityPosition);
            return this;
        }

        public Builder favoritesCount(int favoritesCount) {
            mediaDto.setFavoritesCount(favoritesCount);
            return this;
        }

        public Builder imageUrl(String imageUrl) {
            mediaDto.setImageUrl(imageUrl);
            return this;
        }

        public Builder myanimelistUrl(String myanimelistUrl) {
            mediaDto.setMyanimelistUrl(myanimelistUrl);
            return this;
        }

        public Builder type(String type) {
            mediaDto.setType(type);
            return this;
        }

        public Builder startDate(Date startDate) {
            mediaDto.setStartDate(startDate);
            return this;
        }

        public Builder endDate(Date endDate) {
            mediaDto.setEndDate(endDate);
            return this;
        }

        public Builder status(String status) {
            mediaDto.setStatus(status);
            return this;
        }

        public Builder mediaType(String mediaType) {
            mediaDto.setMediaType(mediaType);
            return this;
        }

        public Builder id(long id) {
            mediaDto.setId(id);
            return this;
        }
    }
}

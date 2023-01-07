package com.lucferreira.myanimeback.model.media;

import java.util.Date;

public class MediaForm {
    private String name;
    private int members;
    private float score;
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

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
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

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getMediaType() {
        return mediaType;
    }
}

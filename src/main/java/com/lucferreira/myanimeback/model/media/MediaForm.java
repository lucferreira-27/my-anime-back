package com.lucferreira.myanimeback.model.media;

import java.util.Date;
import java.util.List;
import net.sandrohc.jikan.model.anime.Anime;
import net.sandrohc.jikan.model.manga.Manga;

public class MediaForm {
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
    private Long malId;
    private List<String> authors;

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

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getMediaType() {
        return mediaType;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public Long getMalId() {
        return malId;
    }

    public void setMalId(Long malId) {
        this.malId = malId;
    }

    public static MediaForm fromJikanMedia(Anime jikan) {
        return new MediaForm.Builder()
                .type("anime")
                .members(jikan.getMembers())
                .mediaType(jikan.getType().name())
                .name(jikan.getTitle())
                .startDate(Date.from(jikan.getAired().getFrom().toInstant()))
                .endDate(jikan.getAired().getTo() != null ? Date.from(jikan.getAired().getTo().toInstant()) : null)
                .status(jikan.getStatus().name())
                .score(jikan.getScore())
                .scoreCount(jikan.getScoredBy())
                .rankedPosition(jikan.getRank())
                .popularityPosition(jikan.getPopularity())
                .favoritesCount(jikan.getFavorites())
                .imageUrl(jikan.getImages().getJpg().getImageUrl())
                .myanimelistUrl(jikan.getUrl())
                .malId(Long.valueOf(jikan.getMalId()))
                .build();
    }

    public static MediaForm fromJikanMedia(Manga jikan) {
        return new MediaForm.Builder()
                .type("manga")
                .mediaType(jikan.getType().name())
                .startDate(Date.from(jikan.getPublished().getFrom().toInstant()))
                .endDate(Date.from(jikan.getPublished().getTo().toInstant()))
                .authors(jikan.getAuthors().stream().map(a -> a.name).toList())
                .score(jikan.getScore())
                .scoreCount(jikan.getScoredBy())
                .rankedPosition(jikan.getRank())
                .popularityPosition(jikan.getPopularity())
                .favoritesCount(jikan.getFavorites())
                .imageUrl(jikan.getImages().getJpg().getImageUrl())
                .myanimelistUrl(jikan.getUrl())
                .malId(Long.valueOf(jikan.getMalId()))
                .build();
    }

    public static class Builder {
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
        private List<String> authors;
        private String mediaType;
        private Long malId;

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

        public Builder authors(List<String> authors) {
            this.authors = authors;
            return this;
        }

        public Builder mediaType(String mediaType) {
            this.mediaType = mediaType;
            return this;
        }

        public Builder malId(Long malId) {
            this.malId = malId;
            return this;
        }

        public MediaForm build() {
            MediaForm mediaForm = new MediaForm();
            mediaForm.setName(name);
            mediaForm.setMembers(members);
            mediaForm.setScore(score);
            mediaForm.setScoreCount(scoreCount);
            mediaForm.setRankedPosition(rankedPosition);
            mediaForm.setPopularityPosition(popularityPosition);
            mediaForm.setFavoritesCount(favoritesCount);
            mediaForm.setImageUrl(imageUrl);
            mediaForm.setMyanimelistUrl(myanimelistUrl);
            mediaForm.setType(type);
            mediaForm.setStartDate(startDate);
            mediaForm.setEndDate(endDate);
            mediaForm.setStatus(status);
            mediaForm.setAuthors(authors);
            mediaForm.setMediaType(mediaType);
            mediaForm.setMalId(malId);
            return mediaForm;
        }
    }

}

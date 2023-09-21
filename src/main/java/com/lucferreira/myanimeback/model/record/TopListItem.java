package com.lucferreira.myanimeback.model.record;

public class TopListItem {
    private final String title;



    private final String textDate;
    private final Integer count;
    private final String type;
    private Double scoreValue;
    private Integer members;
    private Integer ranked;
    private String archiveUrl;

    public Double getScoreValue() {
        return scoreValue;
    }

    public void setScoreValue(Double scoreValue) {
        this.scoreValue = scoreValue;
    }


    public Integer getMembers() {
        return members;
    }

    public void setMembers(Integer members) {
        this.members = members;
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
    public String getTitle() {
        return title;
    }

    public String getTextDate() {
        return textDate;
    }

    public Integer getCount() {
        return count;
    }

    public String getType() {
        return type;
    }
    private TopListItem(String title, String type, Integer count,String textDate,Double scoreValue, Integer ranked , Integer members, String archiveUrl) {
        this.title = title;
        this.type = type;
        this.count = count;
        this.textDate = textDate;
        this.scoreValue = scoreValue;
        this.members = members;
        this.archiveUrl = archiveUrl;
        this.ranked = ranked;
    }

    public static class Builder {
        private String title;
        private String textDate;
        private Integer count;
        private String type;
        private Double scoreValue;
        private Integer members;
        private Integer ranked;
        private String archiveUrl;
        public Builder title(String title) {
            this.title = title;
            return this;
        }
        public Builder textDate(String textDate) {
            this.textDate = textDate;
            return this;
        }
        public Builder count(Integer count) {
            this.count = count;
            return this;
        }
        public Builder type(String type) {
            this.type = type;
            return this;
        }
        public Builder scoreValue(Double scoreValue) {
            this.scoreValue = scoreValue;
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

        public Builder archiveUrl(String archiveUrl) {
            this.archiveUrl = archiveUrl;
            return this;
        }

        public TopListItem build() {
            return new TopListItem(title,type,count,textDate,scoreValue, ranked,members, archiveUrl);
        }
    }
}

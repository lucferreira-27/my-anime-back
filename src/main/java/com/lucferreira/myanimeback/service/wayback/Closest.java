package com.lucferreira.myanimeback.service.wayback;


import com.fasterxml.jackson.annotation.JsonProperty;

public class Closest {
        private String status;
        private boolean available;
        private String url;
        private String timestamp;

        @JsonProperty("available")
        public boolean isAvailable() {
            return available;
        }

        public void setAvailable(boolean available) {
            this.available = available;
        }

        @JsonProperty("url")
        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @JsonProperty("timestamp")
        public String getTimestamp() {
                return timestamp;
            }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
        @JsonProperty("status")
        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return "Closest{" +
                    "available=" + available +
                    ", url='" + url + '\'' +
                    ", timestamp='" + timestamp + '\'' +
                    ", status='" + status + '\'' +
                    '}';
        }
    }


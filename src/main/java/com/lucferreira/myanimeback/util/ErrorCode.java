package com.lucferreira.myanimeback.util;

import org.springframework.web.server.ResponseStatusException;

public class ErrorCode {
    private ResponseStatusException statusException;
    public ErrorCode(ResponseStatusException statusException){
        this.statusException =statusException;
    }

    public Integer getCode() {
        return statusException.getStatusCode().value();
    }

    public String getReason() {
        if (statusException.getReason() == null || statusException.getReason().isEmpty()){
            return statusException.getMessage();
        }
        return statusException.getReason();
    }
}

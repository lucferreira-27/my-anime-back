package com.lucferreira.myanimeback.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class ArchiveScraperException extends Exception {

    public ArchiveScraperException(String reason) {
        super(reason);
    }
}

package com.lucferreira.myanimeback.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class WaybackException extends ResponseStatusException {
    public WaybackException(HttpStatusCode status, String msg){
        super(status,msg);
    }
    public WaybackException(HttpStatusCode status){
        super(status);
    }

}

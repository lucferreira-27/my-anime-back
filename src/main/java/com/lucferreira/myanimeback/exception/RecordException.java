package com.lucferreira.myanimeback.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class RecordException extends ResponseStatusException {
    public RecordException(HttpStatusCode status, String msg){
        super(status,msg);
    }
    public RecordException(HttpStatusCode status){
        super(status);
    }

}

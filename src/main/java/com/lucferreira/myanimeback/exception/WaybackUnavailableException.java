package com.lucferreira.myanimeback.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class WaybackUnavailableException extends WaybackException{


    public WaybackUnavailableException(HttpStatusCode status,String msg){
        super(status,msg);
    }
    public WaybackUnavailableException(String msg){
        super(HttpStatus.SERVICE_UNAVAILABLE,msg);
    }
}

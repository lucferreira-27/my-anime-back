package com.lucferreira.myanimeback.exception;

import org.springframework.http.HttpStatus;

public class WaybackTimestampParseException extends WaybackException{

    public WaybackTimestampParseException(String msg){
        super(HttpStatus.BAD_REQUEST,msg);
    }


}

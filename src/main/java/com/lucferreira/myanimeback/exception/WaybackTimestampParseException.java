package com.lucferreira.myanimeback.exception;

public class WaybackTimestampParseException extends WaybackException{

    public WaybackTimestampParseException(){

    }
    public WaybackTimestampParseException(String msg){
        super(msg);
    }

    public WaybackTimestampParseException(Exception e){
        super(e);
    }
}

package com.lucferreira.myanimeback.exception;

import com.lucferreira.myanimeback.service.wayback.WaybackResponse;

public class WaybackException extends Exception{
    public WaybackException(String msg){
        super(msg);
    }
    public WaybackException(Exception e){
        super(e);
    }
    public WaybackException(){
    }
}

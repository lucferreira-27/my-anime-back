package com.lucferreira.myanimeback.exception;

public class WaybackUnavailableException extends WaybackException{

    public WaybackUnavailableException(){

    }
    public WaybackUnavailableException(Exception e){
        super(e);
    }
    public WaybackUnavailableException(String msg){
        super(msg);
    }
}

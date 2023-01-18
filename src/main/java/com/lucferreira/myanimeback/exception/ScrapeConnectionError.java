package com.lucferreira.myanimeback.exception;

public class ScrapeConnectionError extends ArchiveScraperException{
    public ScrapeConnectionError(String reason){
        super(reason);
    }
}

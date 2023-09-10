package com.lucferreira.myanimeback.util;

import com.lucferreira.myanimeback.exception.ArchiveScraperException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateParse {
    public static Date stringToDate(String str) throws ArchiveScraperException {
        try{
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

            Date date = format.parse(str);
            return date;
        }catch (ParseException e){
            throw new ArchiveScraperException("Error on parse url date");
        }

    }
}

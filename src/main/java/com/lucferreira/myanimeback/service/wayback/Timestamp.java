package com.lucferreira.myanimeback.service.wayback;

import com.lucferreira.myanimeback.exception.WaybackException;
import com.lucferreira.myanimeback.exception.WaybackTimestampParseException;
import com.lucferreira.myanimeback.util.Regex;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class Timestamp {

    private Date date;
    private String originalValue;
    public Timestamp(String value) throws WaybackTimestampParseException {
        String pattern = "(\\d{4})(\\d{2})(\\d{2})";
        this.originalValue = Regex.match(value,pattern);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try{
            this.date = sdf.parse(originalValue);
        }catch (ParseException e){
            throw new WaybackTimestampParseException(String.format( "Unable to parse timestamp: '%s' format should be yyyyMMdd",value));
        }
    }

    public Date getDate(){
        return this.date;
    }
    private Date increaseDateByYear(int years){
        Calendar c = Calendar.getInstance();
        c.setTime(this.date);
        c.add(Calendar.YEAR, years);
        return c.getTime();
    }
    private Date increaseDateByMonth(int months){
        Calendar c = Calendar.getInstance();
        c.setTime(this.date);
        c.add(Calendar.MONTH, months);
        return c.getTime();
    }

    private Date increaseDateByDay(int days){
        Calendar c = Calendar.getInstance();
        c.setTime(this.date);
        c.add(Calendar.DATE, days);
        return c.getTime();
    }

    public String getOriginalValue() {
        return originalValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Timestamp timestamp = (Timestamp) o;
        return Objects.equals(date, timestamp.date) && Objects.equals(originalValue, timestamp.originalValue);
    }
    public static Timestamp valueOf(String value){
        return new Timestamp(value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, originalValue);
    }

    @Override
    public String toString() {
        return originalValue;
    }
}

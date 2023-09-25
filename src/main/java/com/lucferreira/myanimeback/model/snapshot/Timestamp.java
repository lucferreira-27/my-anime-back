package com.lucferreira.myanimeback.model.snapshot;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lucferreira.myanimeback.exception.WaybackTimestampParseException;
import com.lucferreira.myanimeback.util.Regex;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Entity
public class Timestamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date date;
    private String originalValue;

    public Timestamp() {

    }

    public Timestamp(String value) throws WaybackTimestampParseException {
        this.originalValue = value;
        this.date = parseDate(value);
    }

    private static Date parseDate(String value) throws WaybackTimestampParseException {
        try {

            String pattern = "^(\\d{4})(\\d{0,2}){0,5}$";
            String originalValue = Regex.match(value, pattern);
            String format;
            switch (originalValue.length()) {
                case 4:
                    format = "yyyy";
                    break;
                case 6:
                    format = "yyyyMM";
                    break;
                case 8:
                    format = "yyyyMMdd";
                    break;
                case 10:
                    format = "yyyyMMddHH";
                    break;
                case 12:
                    format = "yyyyMMddHHmm";
                    break;
                case 14:
                    format = "yyyyMMddHHmmss";
                    break;
                default:
                    throw new ParseException("Invalid timestamp format", 0);
            }
            return new SimpleDateFormat(format).parse(originalValue);
        } catch (ParseException e) {
            throw new WaybackTimestampParseException(
                    String.format("Unable to parse timestamp: '%s' format should be yyyyMMdd, yyyyMM or yyyy", value));
        }
    }

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    public Date getDate() {
        return this.date;
    }

    public String getOriginalValue() {
        return originalValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Timestamp timestamp = (Timestamp) o;
        return Objects.equals(date, timestamp.date) && Objects.equals(originalValue, timestamp.originalValue);
    }

    public static Timestamp valueOf(String value) {
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

    public static boolean isSameDay(Timestamp timestamp, Timestamp timestamp2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(timestamp.getDate());
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(timestamp2.getDate());
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }
}

package com.lucferreira.myanimeback.service.wayback;

import com.lucferreira.myanimeback.exception.WaybackTimestampParseException;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class CalendarCaptures {
    List<CalendarSimpleItem> calendarItemList = new ArrayList<>();

    public List<CalendarSimpleItem> getCalendarItemList() {
        return calendarItemList;
    }

    public void setCalendarItemList(List<CalendarSimpleItem> calendarItemList) {
        this.calendarItemList = calendarItemList;
    }

    public List<Timestamp>  completeAllItemDate(int year) throws WaybackTimestampParseException {
        List<Timestamp> timestamps = new ArrayList<>();
        for (CalendarSimpleItem simpleItem : calendarItemList) {
            timestamps.add(simpleItem.completeTimestamp(year));
        }
        return timestamps;
    }
}

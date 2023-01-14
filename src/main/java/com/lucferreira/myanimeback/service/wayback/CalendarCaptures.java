package com.lucferreira.myanimeback.service.wayback;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lucferreira.myanimeback.exception.WaybackTimestampParseException;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CalendarCaptures {
    @JsonProperty("items")
    private ArrayList<ArrayList<String>> items;

    @JsonProperty("items")
    public ArrayList<ArrayList<String>> getItems() {
        return items;
    }
    @JsonProperty("items")
    public void setItems(ArrayList<ArrayList<String>> items) {
        this.items = items;
    }

    private List<CalendarSimpleItem> calendarItemList = new ArrayList<>();

    public List<CalendarSimpleItem> getCalendarItemList() {
        return calendarItemList;
    }

    public void setCalendarItemList(List<CalendarSimpleItem> calendarItemList) {
        this.calendarItemList = calendarItemList;
    }

    public List<CalendarSimpleItem>  completeAllItemDate(int year) throws WaybackTimestampParseException {
        List<CalendarSimpleItem> calendarSimpleItems = new ArrayList<>();
        if (items == null || items.size() <= 0) {
            return Arrays.asList();
        }
        for (ArrayList<String> item : items) {
            String dayMonth = item.get(0);
            if (dayMonth.chars().anyMatch(c -> !Character.isDigit(c))){
                continue;
            }
            String strTimestamp = dayMonth;
            if (dayMonth.length() <= 3){
                strTimestamp = "0".concat(dayMonth);
            }
            calendarSimpleItems.add(new CalendarSimpleItem(new Timestamp(year + strTimestamp),Integer.parseInt(item.get(2))));
        }
        return calendarSimpleItems;
    }
}

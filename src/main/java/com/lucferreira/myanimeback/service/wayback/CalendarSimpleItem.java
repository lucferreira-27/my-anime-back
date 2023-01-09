package com.lucferreira.myanimeback.service.wayback;

import com.lucferreira.myanimeback.exception.WaybackTimestampParseException;

public class CalendarSimpleItem {
    int dayAndMothDate; // [0]
    int amountOfSnapshots; // [2]


    public int getAmountOfSnapshots() {
        return amountOfSnapshots;
    }

    public void setAmountOfSnapshots(int amountOfSnapshots) {
        this.amountOfSnapshots = amountOfSnapshots;
    }

    public int getDayAndMothDate() {
        return dayAndMothDate;
    }

    public void setDayAndMothDate(int dayAndMothDate) {
        this.dayAndMothDate = dayAndMothDate;
    }

    public Timestamp completeTimestamp(int year) throws WaybackTimestampParseException {
           String strDate = Integer.toString(year + dayAndMothDate);
           return new Timestamp(strDate);
    }
    /*

    [
        120, - 01/02
        200, - \./
        1 - amount of snapshots available in this day
    ],


     */
}

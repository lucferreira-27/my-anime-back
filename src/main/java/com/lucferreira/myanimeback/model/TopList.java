package com.lucferreira.myanimeback.model;

import java.util.HashMap;
import java.util.Map;

public class TopRecord {
    private Map<Integer,Record> recordMap = new HashMap<>();

    public Map<Integer, Record> getRecordMap() {
        return recordMap;
    }
}

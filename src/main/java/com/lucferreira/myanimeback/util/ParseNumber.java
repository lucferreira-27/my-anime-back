package com.lucferreira.myanimeback.util;

import com.lucferreira.myanimeback.exception.ScrapeParseError;
import com.lucferreira.myanimeback.service.scraper.mal.MediaAnchors;

import java.util.Map;

public class ParseNumber<T> {


    public static  Integer getIntValue(MediaAnchors target, Map<MediaAnchors, String> parseTextMap) throws ScrapeParseError {
        return getNumericalValue(target, parseTextMap, Integer::valueOf);
    }

    public static  Double getDoubleValue(MediaAnchors target, Map<MediaAnchors, String> parseTextMap) throws ScrapeParseError {
        return getNumericalValue(target, parseTextMap, Double::valueOf);
    }

    public static  <T extends Number> T getNumericalValue(MediaAnchors target, Map<MediaAnchors, String> parseTextMap, NumberParser<T> parser) throws ScrapeParseError {
        String result = parseTextMap.get(target);
        if (result == null || result.isEmpty()) {
            return null;
        }
        try {
            return parser.parse(result);
        } catch (NumberFormatException e) {
            throw new ScrapeParseError("Error parsing the data for target " + target.name() + ": " + result + " is not a valid number.");
        }
    }

    @FunctionalInterface
    private interface NumberParser<T extends Number> {
        T parse(String s) throws NumberFormatException;
    }
}

package com.lucferreira.myanimeback.util;

import com.lucferreira.myanimeback.exception.ScrapeParseError;
import com.lucferreira.myanimeback.service.scraper.TargetStatistics;

import java.util.Map;

public class ParseNumber {
    public static  Integer getIntValue(TargetStatistics target, Map<TargetStatistics, String> parseTextMap) throws ScrapeParseError {
        return getNumericalValue(target, parseTextMap, Integer::valueOf);
    }

    public static  Double getDoubleValue(TargetStatistics target, Map<TargetStatistics, String> parseTextMap) throws ScrapeParseError {
        return getNumericalValue(target, parseTextMap, Double::valueOf);
    }

    public static  <T extends Number> T getNumericalValue(TargetStatistics target, Map<TargetStatistics, String> parseTextMap, NumberParser<T> parser) throws ScrapeParseError {
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

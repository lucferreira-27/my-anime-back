package com.lucferreira.myanimeback.util;

import com.lucferreira.myanimeback.exception.ScrapeParseError;
import com.lucferreira.myanimeback.service.scraper.mal.MediaAnchors;

import java.util.Map;

public class ParseNumber2<E> {


    public static <E> Integer getIntValue(E target, Map<E, String> parseTextMap) throws ScrapeParseError {
        return getNumericalValue(target, parseTextMap, (value) ->{
            if(value.contains(",")){
                value = value.replaceAll(",","");
            }
            return Integer.valueOf(value);
        });
    }

    public static <E>  Double getDoubleValue(E target, Map<E, String> parseTextMap) throws ScrapeParseError {
        return getNumericalValue(target, parseTextMap, Double::valueOf);
    }


    public static <E,T extends Number> T getNumericalValue(E target, Map<E, String> parseTextMap, NumberParser<T> parser) throws ScrapeParseError {
        String result = parseTextMap.get(target);
        if (result == null || result.isEmpty()) {
            return null;
        }
        try {

            return parser.parse(result);
        } catch (NumberFormatException e) {
            throw new ScrapeParseError("Error parsing the data for target " + target.toString() + ": " + result + " is not a valid number.");
        }
    }

    @FunctionalInterface
    public interface NumberParser<T extends Number> {
        T parse(String s) throws NumberFormatException;
    }
}

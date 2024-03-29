package com.lucferreira.myanimeback.util;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Regex {



    public static String match(String value, String pattern){
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(value);
        if (m.find()) {
            return m.group(0);
        }
        return null;
    }
    public static String matchAll(String value, String pattern){
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(value);
        List<String> results = new ArrayList<>();
        while (m.find()) {
            results.add(m.group(0));
        }
        return results.stream().collect(Collectors.joining());
    }
    public static Map<Integer,String> groups(String value, String pattern){
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(value);
        Map<Integer,String> matchGroups = new HashMap<>();
        if (m.find()){
            for (int i = 0; i <= m.groupCount(); i++) {
                matchGroups.put(i, m.group(i));
            }
        }
        return matchGroups;
    }
    public static Optional<Matcher> groupsName(String value, String pattern){
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(value);
        if (m.matches()) {
            return Optional.of(m);
        }
        return Optional.empty();
    }
}

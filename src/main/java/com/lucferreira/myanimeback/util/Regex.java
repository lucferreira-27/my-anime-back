package com.lucferreira.myanimeback.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {



    public static String match(String value, String pattern){
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(value);
        if (m.find()) {
            return m.group(0);
        }
        return null;
    }
    public static Map<String,String> groups(String value, String pattern){
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(value);
        Map<String,String> matchGroups = new HashMap<>();
        if (m.find()){
            for (int i = 0; i <= m.groupCount(); i++) {
                matchGroups.put(Integer.toString(i), m.group(i));
            }
        }
        return matchGroups;
    }

}

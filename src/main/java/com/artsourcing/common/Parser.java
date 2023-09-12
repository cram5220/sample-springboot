package com.artsourcing.common;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

import java.util.HashMap;
import java.util.Map;

public class Parser {
    public static Map<String,Object> jsonText2Map(String text){
        Map<String, Object> mapData = new HashMap<String,Object>();
        try {
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(text);
            mapData.putAll(json);
        } catch (ParseException e) {
            throw new RuntimeException("json parse 에 실패했습니다.");
        }

        return mapData;
    }
}

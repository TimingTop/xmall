package com.timing.xmall.xorder.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;

public class JsonUtil {

    public static String readJsonFile(String fileName) {
        try {
            File jsonFile = new File(fileName);

            Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(reader);

            return jsonNode.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String toJsonString(Object object) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            String s = objectMapper.writeValueAsString(object);
            return s;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T parseObject(String jsonStr, Class<T> tClass) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            T t = objectMapper.readValue(jsonStr, tClass);
            return t;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}

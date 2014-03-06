package com.zizibujuan.android.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

/**
 * json工具类
 *
 * @author jinzw
 * @since 0.0.1
 */
public abstract class JsonUtil {

    private static final class MapTypeToken extends TypeToken<Map<String, Object>>{}
    private static final class ListMapTypeToken extends TypeToken<List<Map<String, Object>>>{}

    private static MapTypeToken mapTypeToken = new MapTypeToken();
    private static ListMapTypeToken listMapTypeToken = new ListMapTypeToken();
    private static Gson json = new GsonBuilder().create();

    public static String toJson(Object src){
        return json.toJson(src);
    }

    public static Map<String, Object> fromJsonObject(InputStream inputStream){
        String jsonString = getJsonString(inputStream);
        if(jsonString != null){
            return fromJsonObject(jsonString);
        }
        return null;
    }

    public static Map<String, Object> fromJsonObject(String jsonString){
        return json.fromJson(jsonString, mapTypeToken.getType());
    }

    private static String getJsonString(InputStream inputStream){
        StringWriter sw = new StringWriter();

        try {
            IOUtils.copy(inputStream, sw, "UTF-8");
            return sw.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Map<String, Object>> fromJsonArray(InputStream inputStream){
        String jsonString = getJsonString(inputStream);
        if(jsonString != null){
            return fromJsonArray(jsonString);
        }
        return null;
    }

    public static List<Map<String, Object>> fromJsonArray(String jsonString){
        return json.fromJson(jsonString, listMapTypeToken.getType());
    }

}

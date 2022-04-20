package com.smile.order.utils;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

/**
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-13 3:19 下午
 */
public class JsonUtils {
    public static final Gson GSON_PRETTY = new GsonBuilder().setPrettyPrinting().create();
    public static final Gson GSON_DEFAULT = new GsonBuilder().setPrettyPrinting().create();
    public static Gson gson = new Gson();

    public static <T> T fromJson(String json, Class<T> classOfT) throws JsonSyntaxException {
        return gson.fromJson(json, classOfT);
    }

    public static <T> T fromJson(String json, Type typeOfT) throws JsonSyntaxException {
        return gson.fromJson(json, typeOfT);
    }

    public static String toJson(Object obj) {
        return toJsonWithPretty(obj, false);
    }

    public static String toJsonWithPretty(Object obj, boolean pretty) {
        if (pretty) {
            return GSON_PRETTY.toJson(obj);
        } else {
            return GSON_DEFAULT.toJson(obj);
        }
    }
}

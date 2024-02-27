package com.sap.cloud.lm.sl.common.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.common.message.Messages;
import com.sap.cloud.lm.sl.common.model.json.MapWithNumbersAdapterFactory;

public class JsonUtil {

    private static final int MAX_LENGTH = 128;

    public static Map<String, Object> convertJsonToMap(InputStream json) throws ParsingException {
        return convertJsonToMap(json, new TypeToken<Map<String, Object>>() {
        }.getType());
    }

    public static <K, V> Map<K, V> convertJsonToMap(InputStream json, Type type) throws ParsingException {
        return fromJson(json, type, new HashMap<K, V>(), Messages.CANNOT_CONVERT_JSON_STREAM_TO_MAP);
    }

    public static Map<String, Object> convertJsonToMap(String json) throws ParsingException {
        return convertJsonToMap(json, new TypeToken<Map<String, Object>>() {
        }.getType());
    }

    public static <K, V> Map<K, V> convertJsonToMap(String json, Type type) throws ParsingException {
        return fromJson(json, type, new HashMap<K, V>(), Messages.CANNOT_CONVERT_JSON_STRING_TO_MAP);
    }

    public static List<Object> convertJsonToList(InputStream json) throws ParsingException {
        return convertJsonToList(json, new TypeToken<List<Object>>() {
        }.getType());
    }

    public static <T> List<T> convertJsonToList(InputStream json, Type type) throws ParsingException {
        return fromJson(json, type, new ArrayList<T>(), Messages.CANNOT_CONVERT_JSON_STREAM_TO_LIST);
    }

    public static List<Object> convertJsonToList(String json) throws ParsingException {
        return convertJsonToList(json, new TypeToken<List<Object>>() {
        }.getType());
    }

    public static <T> List<T> convertJsonToList(String json, Type type) throws ParsingException {
        return fromJson(json, type, new ArrayList<T>(), Messages.CANNOT_CONVERT_JSON_STRING_TO_LIST);
    }

    private static <T> T fromJson(String json, Type type, T defaultValue, String errorMessage) {
        if (json == null || json.isEmpty()) {
            return defaultValue;
        }
        try {
            Gson gson = new GsonBuilder().registerTypeAdapterFactory(new MapWithNumbersAdapterFactory())
                                         .create();
            return gson.fromJson(json, type);
        } catch (Exception e) {
            throw new ParsingException(e, errorMessage, json.substring(0, Math.min(json.length(), MAX_LENGTH)));
        }
    }

    private static <T> T fromJson(InputStream json, Type type, T defaultValue, String errorMessage) {
        if (json == null) {
            return defaultValue;
        }
        try {
            Gson gson = new GsonBuilder().registerTypeAdapterFactory(new MapWithNumbersAdapterFactory())
                                         .create();
            return gson.fromJson(new InputStreamReader(json, StandardCharsets.UTF_8), type);
        } catch (Exception e) {
            throw new ParsingException(e, errorMessage, json);
        }
    }

    public static <T> String toJson(T obj, boolean prettyPrinting) {
        return toJson(obj, prettyPrinting, false, false);
    }

    public static <T> String toJson(T obj) {
        return toJson(obj, false);
    }

    public static <T> String toJson(T obj, boolean prettyPrinting, boolean enableExpose, boolean disableHtmlEscaping) {
        GsonBuilder builder = new GsonBuilder();
        if (disableHtmlEscaping) {
            builder.disableHtmlEscaping();
        }
        if (prettyPrinting) {
            builder.setPrettyPrinting();
        }
        if (enableExpose) {
            builder.excludeFieldsWithoutExposeAnnotation();
        }
        return builder.create()
                      .toJson(obj);
    }

    public static <T> T fromJson(String json, Type type) throws ParsingException {
        try {
            return new Gson().fromJson(json, type);
        } catch (JsonSyntaxException e) {
            throw new ParsingException(e,
                                       Messages.CANNOT_PARSE_JSON_STRING_TO_TYPE,
                                       json.substring(0, Math.min(json.length(), MAX_LENGTH)),
                                       type.toString());
        }
    }

    public static <T> byte[] toBinaryJson(Object obj) {
        return new GsonBuilder().create()
                                .toJson(obj)
                                .getBytes(StandardCharsets.UTF_8);
    }

    public static <T> T fromBinaryJson(byte[] binaryJson, Class<T> classOfT) {
        try {
            return new Gson().fromJson(new String(binaryJson, StandardCharsets.UTF_8), classOfT);
        } catch (JsonSyntaxException e) {
            throw new ParsingException(e);
        }
    }

}

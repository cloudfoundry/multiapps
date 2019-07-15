package com.sap.cloud.lm.sl.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.common.message.Messages;

public class JsonUtil {

    private static final int MAX_LENGTH = 128;
    private static final ObjectMapper OBJECT_MAPPER = createObjectMapper();

    private static ObjectMapper createObjectMapper() {
        return new ObjectMapper().setVisibility(PropertyAccessor.ALL, Visibility.NONE)
            .setVisibility(PropertyAccessor.FIELD, Visibility.ANY)
            .setSerializationInclusion(Include.NON_NULL)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public static Map<String, Object> convertJsonToMap(InputStream json) throws ParsingException {
        return convertJsonToMap(json, new TypeReference<Map<String, Object>>() {
        });
    }

    public static Map<String, Object> convertJsonToMap(String json) throws ParsingException {
        return convertJsonToMap(json, new TypeReference<Map<String, Object>>() {
        });
    }

    public static <K, V> Map<K, V> convertJsonToMap(InputStream json, TypeReference<Map<K, V>> typeReference) throws ParsingException {
        return fromJson(json, typeReference, new HashMap<>(), Messages.CANNOT_CONVERT_JSON_STREAM_TO_MAP);
    }

    public static <K, V> Map<K, V> convertJsonToMap(String json, TypeReference<Map<K, V>> typeReference) throws ParsingException {
        return fromJson(json, typeReference, new HashMap<>(), Messages.CANNOT_CONVERT_JSON_STRING_TO_MAP);
    }

    public static List<Object> convertJsonToList(InputStream json) throws ParsingException {
        return convertJsonToList(json, new TypeReference<List<Object>>() {
        });
    }

    public static List<Object> convertJsonToList(String json) throws ParsingException {
        return convertJsonToList(json, new TypeReference<List<Object>>() {
        });
    }

    public static <T> List<T> convertJsonToList(InputStream json, TypeReference<List<T>> typeReference) throws ParsingException {
        return fromJson(json, typeReference, new ArrayList<>(), Messages.CANNOT_CONVERT_JSON_STREAM_TO_LIST);
    }

    public static <T> List<T> convertJsonToList(String json, TypeReference<List<T>> typeReference) throws ParsingException {
        return fromJson(json, typeReference, new ArrayList<>(), Messages.CANNOT_CONVERT_JSON_STRING_TO_LIST);
    }

    private static <T> T fromJson(InputStream json, TypeReference<T> typeReference, T defaultValue, String errorMessage) {
        if (json == null) {
            return defaultValue;
        }
        try {
            return getObjectMapper().readValue(json, typeReference);
        } catch (Exception e) {
            throw new ParsingException(e, errorMessage, json);
        }
    }

    private static <T> T fromJson(String json, TypeReference<T> typeReference, T defaultValue, String errorMessage) {
        if (StringUtils.isEmpty(json)) {
            return defaultValue;
        }
        try {
            return getObjectMapper().readValue(json, typeReference);
        } catch (Exception e) {
            throw new ParsingException(e, errorMessage, json.substring(0, Math.min(json.length(), MAX_LENGTH)));
        }
    }

    public static byte[] toJsonBinary(Object object) {
        String jsonString = toJson(object);
        return jsonString.getBytes(StandardCharsets.UTF_8);
    }

    public static String toJson(Object object) {
        return toJson(object, false);
    }

    public static String toJson(Object object, boolean indentedOutput) {
        try {
            return getObjectWriter(indentedOutput).writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public static <T> T fromJsonBinary(byte[] json, TypeReference<T> typeReference) {
        return fromJson(toString(json), typeReference);
    }

    public static <T> T fromJsonBinary(byte[] json, Class<T> classOfT) {
        return fromJson(toString(json), classOfT);
    }

    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        ObjectMapper objectMapper = getObjectMapper();
        JavaType type = objectMapper.getTypeFactory()
            .constructType(typeReference);
        return fromJson(objectMapper, json, type);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        ObjectMapper objectMapper = getObjectMapper();
        JavaType type = objectMapper.getTypeFactory()
            .constructType(classOfT);
        return fromJson(objectMapper, json, type);
    }

    private static <T> T fromJson(ObjectMapper objectMapper, String json, JavaType javaType) {
        try {
            return objectMapper.readValue(json, javaType);
        } catch (IOException e) {
            throw new ParsingException(e, Messages.CANNOT_PARSE_JSON_STRING_TO_TYPE, json.substring(0, Math.min(json.length(), MAX_LENGTH)),
                javaType);
        }
    }

    public static ObjectWriter getObjectWriter(boolean indentedOutput) {
        ObjectMapper objectMapper = getObjectMapper();
        if (indentedOutput) {
            return objectMapper.writerWithDefaultPrettyPrinter();
        }
        return objectMapper.writer();
    }

    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    private static String toString(byte[] binary) {
        return new String(binary, StandardCharsets.UTF_8);
    }

}

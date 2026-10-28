package org.cloudfoundry.multiapps.common.util;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.cloudfoundry.multiapps.common.Messages;
import org.cloudfoundry.multiapps.common.ParsingException;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.ObjectWriter;
import tools.jackson.databind.cfg.EnumFeature;
import tools.jackson.databind.json.JsonMapper;

public class JsonUtil {

    private static final int MAX_LENGTH = 128;
    private static final ObjectMapper OBJECT_MAPPER = createObjectMapper();
    private static final ObjectMapper OBJECT_MAPPER_WITH_NULLS = createObjectMapperWithNulls();

    private JsonUtil() {
    }

    private static ObjectMapper createObjectMapper() {
        return JsonMapper.builder()
                         .changeDefaultVisibility(vc -> vc.withVisibility(PropertyAccessor.ALL, Visibility.NONE)
                                                          .withVisibility(PropertyAccessor.FIELD, Visibility.ANY))
                         .changeDefaultPropertyInclusion(incl -> incl.withValueInclusion(Include.NON_NULL)
                                                                     .withContentInclusion(Include.NON_NULL))
                         .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                         // Preserve Jackson 2 behavior: these defaults flipped in Jackson 3 and would change the
                         // byte output / read leniency of already-persisted DB-column and Flowable-variable JSON.
                         .disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
                         .enable(MapperFeature.ALLOW_FINAL_FIELDS_AS_MUTATORS)
                         .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
                         .enable(MapperFeature.EXTERNAL_TYPE_ID_ALWAYS_VISIBLE)
                         // Jackson 3 defaults to (de)serializing enums via toString(); Jackson 2 used name(). Several
                         // domain enums override toString() to lowercase, so keep name() to stay wire/persistence
                         // compatible with data written by the Jackson 2 version of the service.
                         .disable(EnumFeature.WRITE_ENUMS_USING_TO_STRING)
                         .disable(EnumFeature.READ_ENUMS_USING_TO_STRING)
                         .disable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS)
                         .disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
                         .build();
    }

    private static ObjectMapper createObjectMapperWithNulls() {
        // Visibilities are required to parse properly "immutables" objects
        return JsonMapper.builder()
                         .changeDefaultVisibility(vc -> vc.withVisibility(PropertyAccessor.ALL, Visibility.NONE)
                                                          .withVisibility(PropertyAccessor.FIELD, Visibility.ANY))
                         .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                         // Preserve Jackson 2 behavior: these defaults flipped in Jackson 3 and would change the
                         // byte output / read leniency of already-persisted DB-column and Flowable-variable JSON.
                         .disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
                         .enable(MapperFeature.ALLOW_FINAL_FIELDS_AS_MUTATORS)
                         .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
                         .enable(MapperFeature.EXTERNAL_TYPE_ID_ALWAYS_VISIBLE)
                         // Jackson 3 defaults to (de)serializing enums via toString(); Jackson 2 used name(). Several
                         // domain enums override toString() to lowercase, so keep name() to stay wire/persistence
                         // compatible with data written by the Jackson 2 version of the service.
                         .disable(EnumFeature.WRITE_ENUMS_USING_TO_STRING)
                         .disable(EnumFeature.READ_ENUMS_USING_TO_STRING)
                         .disable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS)
                         .disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
                         .build();
    }

    public static Map<String, Object> convertJsonToMap(InputStream json) {
        return convertJsonToMap(json, JsonSerializationStrategy.DEFAULT);
    }

    public static Map<String, Object> convertJsonToMap(InputStream json, JsonSerializationStrategy jsonSerializationStrategy) {
        return convertJsonToMap(json, jsonSerializationStrategy, new TypeReference<>() {
        });
    }

    public static Map<String, Object> convertJsonToMap(String json) {
        return convertJsonToMap(json, JsonSerializationStrategy.DEFAULT);
    }

    public static Map<String, Object> convertJsonToMap(String json, JsonSerializationStrategy jsonSerializationStrategy) {
        return convertJsonToMap(json, jsonSerializationStrategy, new TypeReference<>() {
        });
    }

    public static <K, V> Map<K, V> convertJsonToMap(InputStream json, TypeReference<Map<K, V>> typeReference) {
        return convertJsonToMap(json, JsonSerializationStrategy.DEFAULT, typeReference);
    }

    public static <K, V> Map<K, V> convertJsonToMap(InputStream json, JsonSerializationStrategy jsonSerializationStrategy,
                                                    TypeReference<Map<K, V>> typeReference) {
        return fromJson(json, jsonSerializationStrategy, typeReference, new HashMap<>(), Messages.CANNOT_CONVERT_JSON_STREAM_TO_MAP);
    }

    public static <K, V> Map<K, V> convertJsonToMap(String json, TypeReference<Map<K, V>> typeReference) {
        return convertJsonToMap(json, JsonSerializationStrategy.DEFAULT, typeReference);
    }

    public static <K, V> Map<K, V> convertJsonToMap(String json, JsonSerializationStrategy jsonSerializationStrategy,
                                                    TypeReference<Map<K, V>> typeReference) {
        return fromJson(json, jsonSerializationStrategy, typeReference, new HashMap<>(), Messages.CANNOT_CONVERT_JSON_STRING_TO_MAP);
    }

    public static List<Object> convertJsonToList(InputStream json) {
        return convertJsonToList(json, JsonSerializationStrategy.DEFAULT);
    }

    public static List<Object> convertJsonToList(InputStream json, JsonSerializationStrategy jsonSerializationStrategy) {
        return convertJsonToList(json, jsonSerializationStrategy, new TypeReference<>() {
        });
    }

    public static List<Object> convertJsonToList(String json) {
        return convertJsonToList(json, JsonSerializationStrategy.DEFAULT);
    }

    public static List<Object> convertJsonToList(String json, JsonSerializationStrategy jsonSerializationStrategy) {
        return convertJsonToList(json, jsonSerializationStrategy, new TypeReference<>() {
        });
    }

    public static <T> List<T> convertJsonToList(InputStream json, TypeReference<List<T>> typeReference) {
        return convertJsonToList(json, JsonSerializationStrategy.DEFAULT, typeReference);
    }

    public static <T> List<T> convertJsonToList(InputStream json, JsonSerializationStrategy jsonSerializationStrategy,
                                                TypeReference<List<T>> typeReference) {
        return fromJson(json, jsonSerializationStrategy, typeReference, new ArrayList<>(), Messages.CANNOT_CONVERT_JSON_STREAM_TO_LIST);
    }

    public static <T> List<T> convertJsonToList(String json, TypeReference<List<T>> typeReference) {
        return convertJsonToList(json, JsonSerializationStrategy.DEFAULT, typeReference);
    }

    public static <T> List<T> convertJsonToList(String json, JsonSerializationStrategy jsonSerializationStrategy,
                                                TypeReference<List<T>> typeReference) {
        return fromJson(json, jsonSerializationStrategy, typeReference, new ArrayList<>(), Messages.CANNOT_CONVERT_JSON_STRING_TO_LIST);
    }

    private static <T> T fromJson(InputStream json, JsonSerializationStrategy jsonSerializationStrategy, TypeReference<T> typeReference,
                                  T defaultValue, String errorMessage) {
        if (json == null) {
            return defaultValue;
        }
        try {
            return getObjectMapper(jsonSerializationStrategy).readValue(json, typeReference);
        } catch (JacksonException e) {
            throw new ParsingException(e, errorMessage, e.getMessage(), json);
        }
    }

    private static <T> T fromJson(String json, JsonSerializationStrategy jsonSerializationStrategy, TypeReference<T> typeReference,
                                  T defaultValue, String errorMessage) {
        if (StringUtils.isEmpty(json)) {
            return defaultValue;
        }
        try {
            return getObjectMapper(jsonSerializationStrategy).readValue(json, typeReference);
        } catch (JacksonException e) {
            throw new ParsingException(e, errorMessage, e.getMessage(), json.substring(0, Math.min(json.length(), MAX_LENGTH)));
        }
    }

    public static byte[] toJsonBinary(Object object) {
        return toJsonBinary(object, JsonSerializationStrategy.DEFAULT);
    }

    public static byte[] toJsonBinary(Object object, JsonSerializationStrategy jsonSerializationStrategy) {
        String jsonString = toJson(object, jsonSerializationStrategy);
        return jsonString.getBytes(StandardCharsets.UTF_8);
    }

    public static String toJson(Object object) {
        return toJson(object, JsonSerializationStrategy.DEFAULT);
    }

    public static String toJson(Object object, JsonSerializationStrategy jsonSerializationStrategy) {
        return toJson(object, jsonSerializationStrategy, false);
    }

    public static String toJson(Object object, boolean indentedOutput) {
        return toJson(object, JsonSerializationStrategy.DEFAULT, indentedOutput);
    }

    public static String toJson(Object object, JsonSerializationStrategy jsonSerializationStrategy, boolean indentedOutput) {
        try {
            return getObjectWriter(jsonSerializationStrategy, indentedOutput).writeValueAsString(object);
        } catch (JacksonException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public static <T> T fromJsonBinary(byte[] json, TypeReference<T> typeReference) {
        return fromJson(toString(json), JsonSerializationStrategy.DEFAULT, typeReference);
    }

    public static <T> T fromJsonBinary(byte[] json, JsonSerializationStrategy jsonSerializationStrategy, TypeReference<T> typeReference) {
        return fromJson(toString(json), jsonSerializationStrategy, typeReference);
    }

    public static <T> T fromJsonBinary(byte[] json, Class<T> classOfT) {
        return fromJsonBinary(json, JsonSerializationStrategy.DEFAULT, classOfT);
    }

    public static <T> T fromJsonBinary(byte[] json, JsonSerializationStrategy jsonSerializationStrategy, Class<T> classOfT) {
        return fromJson(toString(json), jsonSerializationStrategy, classOfT);
    }

    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        return fromJson(json, JsonSerializationStrategy.DEFAULT, typeReference);
    }

    public static <T> T fromJson(String json, JsonSerializationStrategy jsonSerializationStrategy, TypeReference<T> typeReference) {
        ObjectMapper objectMapper = getObjectMapper(jsonSerializationStrategy);
        JavaType type = objectMapper.getTypeFactory()
                                    .constructType(typeReference);
        return fromJson(objectMapper, json, type);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return fromJson(json, JsonSerializationStrategy.DEFAULT, classOfT);
    }

    public static <T> T fromJson(String json, JsonSerializationStrategy jsonSerializationStrategy, Class<T> classOfT) {
        ObjectMapper objectMapper = getObjectMapper(jsonSerializationStrategy);
        JavaType type = objectMapper.getTypeFactory()
                                    .constructType(classOfT);
        return fromJson(objectMapper, json, type);
    }

    private static <T> T fromJson(ObjectMapper objectMapper, String json, JavaType javaType) {
        try {
            return objectMapper.readValue(json, javaType);
        } catch (JacksonException e) {
            throw new ParsingException(e,
                                       Messages.CANNOT_PARSE_JSON_STRING_TO_TYPE,
                                       json.substring(0, Math.min(json.length(), MAX_LENGTH)),
                                       javaType);
        }
    }

    private static ObjectWriter getObjectWriter(JsonSerializationStrategy jsonSerializationStrategy, boolean indentedOutput) {
        ObjectMapper objectMapper = getObjectMapper(jsonSerializationStrategy);
        if (indentedOutput) {
            return objectMapper.writerWithDefaultPrettyPrinter();
        }
        return objectMapper.writer();
    }

    public static ObjectMapper getObjectMapper(JsonSerializationStrategy jsonSerializationStrategy) {
        switch (jsonSerializationStrategy) {
            case DEFAULT:
                return OBJECT_MAPPER;
            case ALLOW_NULLS:
                return OBJECT_MAPPER_WITH_NULLS;
        }
        throw new IllegalStateException(MessageFormat.format(Messages.INVALID_JSON_SERIALIZATION_STRATEGY_PROVIDED_0,
                                                             jsonSerializationStrategy));
    }

    private static String toString(byte[] binary) {
        return new String(binary, StandardCharsets.UTF_8);
    }

}

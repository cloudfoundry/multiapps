package org.cloudfoundry.multiapps.common.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.IOUtils;
import org.cloudfoundry.multiapps.common.Messages;
import org.cloudfoundry.multiapps.common.ParsingException;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.type.TypeReference;

class JsonUtilTest {

    @Test
    void testConvertJsonToMapWithInvalidJsonInputStream() {
        String invalidJson = "{]";
        InputStream invalidJsonInputStream = IOUtils.toInputStream(invalidJson, Charset.defaultCharset());

        ParsingException resultException = assertThrows(ParsingException.class, () -> JsonUtil.convertJsonToMap(invalidJsonInputStream));

        String errorMessage = resultException.getCause()
                                             .getMessage();
        ParsingException expectedException = new ParsingException(resultException.getCause(),
                                                                  Messages.CANNOT_CONVERT_JSON_STREAM_TO_MAP,
                                                                  errorMessage,
                                                                  invalidJsonInputStream);
        assertEquals(expectedException.getMessage(), resultException.getMessage());
    }

    @Test
    void testConvertJsonToMapWithInvalidJsonString() {
        String invalidJson = "{]";

        ParsingException resultException = assertThrows(ParsingException.class, () -> JsonUtil.convertJsonToMap(invalidJson));

        String errorMessage = resultException.getCause()
                                             .getMessage();

        ParsingException expectedException = new ParsingException(resultException.getCause(),
                                                                  Messages.CANNOT_CONVERT_JSON_STRING_TO_MAP,
                                                                  errorMessage,
                                                                  invalidJson);
        assertEquals(expectedException.getMessage(), resultException.getMessage());
    }

    @Test
    void test1() {
        Foo foo = new Foo(Map.of("test1", createTestProperties()));

        String json = JsonUtil.toJson(foo, true);
        System.out.println(json);

        foo = JsonUtil.fromJson(json, Foo.class);

        Map<String, Object> actualProperties = foo.getProperties()
                                                  .get("test1");

        assertTestProperties(actualProperties);
    }

    @Test
    void test2() {
        Map<String, Object> properties = new TreeMap<>();
        properties.put("test1", createTestProperties());
        Bar bar = new Bar(properties);

        String json = JsonUtil.toJson(bar, true);
        System.out.println(json);

        bar = JsonUtil.fromJson(json, Bar.class);

        @SuppressWarnings("unchecked")
        Map<String, Object> actualProperties = ((Map<String, Object>) bar.getProperties()
                                                                         .get("test1"));

        assertTestProperties(actualProperties);
    }

    @Test
    void test3() {
        String json = JsonUtil.toJson(createTestProperties(), true);
        System.out.println(json);

        Map<String, Object> actualProperties = JsonUtil.convertJsonToMap(json);

        assertTestProperties(actualProperties);
    }

    @Test
    void testWithNullMap() {
        Foo foo = new Foo(null);

        String fooJson = JsonUtil.toJson(foo);
        String barJson = "null";

        Map<String, Object> fooMap = JsonUtil.convertJsonToMap(fooJson);
        Map<String, Object> barMap = JsonUtil.convertJsonToMap(barJson);

        assertTrue(fooMap.isEmpty());
        assertNull(barMap);
    }

    @Test
    void testConvertJsonToMap_withValidInputStream() {
        String json = "{\"key1\":\"value1\", \"key2\":\"value2\"}";
        InputStream jsonInputStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("key1", "value1");
        expectedMap.put("key2", "value2");

        Map<String, String> result = JsonUtil.convertJsonToMap(jsonInputStream, new TypeReference<Map<String, String>>() {
        });

        assertEquals(expectedMap, result);
    }

    @Test
    void testConvertJsonToMap_withValidString() {
        String json = "{\"key1\":\"value1\", \"key2\":\"value2\"}";
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("key1", "value1");
        expectedMap.put("key2", "value2");

        Map<String, String> result = JsonUtil.convertJsonToMap(json, new TypeReference<Map<String, String>>() {
        });
        assertEquals(expectedMap, result);
    }

    @Test
    void testConvertJsonToList_withValidInputStream() {
        String json = "[\"value1\", \"value2\"]";
        InputStream jsonInputStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        List<Object> expectedList = Arrays.asList("value1", "value2");

        List<Object> result = JsonUtil.convertJsonToList(jsonInputStream);

        assertEquals(expectedList, result);
    }

    @Test
    void testConvertJsonToList_withValidString() {
        String json = "[\"value1\", \"value2\"]";
        List<Object> expectedList = Arrays.asList("value1", "value2");

        List<Object> result = JsonUtil.convertJsonToList(json);

        assertEquals(expectedList, result);
    }

    @Test
    void testToJsonBinary_withValidObject() {
        String json = "{\"key\":\"value\"}";
        Map<String, String> object = new HashMap<>();
        object.put("key", "value");

        byte[] result = JsonUtil.toJsonBinary(object);

        assertArrayEquals(json.getBytes(StandardCharsets.UTF_8), result);
    }

    @Test
    void testFromJsonBinary_withValidBinary() {
        String json = "{\"key\":\"value\"}";
        byte[] jsonBinary = json.getBytes(StandardCharsets.UTF_8);
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("key", "value");

        Map<String, String> result = JsonUtil.fromJsonBinary(jsonBinary, new TypeReference<Map<String, String>>() {
        });

        assertEquals(expectedMap, result);
    }

    @Test
    void testFromJsonBinary_withEmptyBinary() {
        byte[] emptyBinary = new byte[0];
        assertThrows(ParsingException.class, () -> JsonUtil.fromJsonBinary(emptyBinary, new TypeReference<Map<String, String>>() {
        }));
    }

    @Test
    void testConvertJsonToMap_withInvalidJson_shouldThrowParsingException() {
        String invalidJson = "invalid-json";
        InputStream jsonInputStream = new ByteArrayInputStream(invalidJson.getBytes(StandardCharsets.UTF_8));

        assertThrows(ParsingException.class, () -> {
            JsonUtil.convertJsonToMap(jsonInputStream, new TypeReference<Map<String, String>>() {
            });
        });
    }

    @Test
    void testConvertJsonToList_withInvalidJson_shouldThrowParsingException() {
        String invalidJson = "invalid-json";
        InputStream jsonInputStream = new ByteArrayInputStream(invalidJson.getBytes(StandardCharsets.UTF_8));

        assertThrows(ParsingException.class, () -> {
            JsonUtil.convertJsonToList(jsonInputStream);
        });
    }

    private Map<String, Object> createTestProperties() {
        Map<String, Object> testProperties1 = new TreeMap<>();
        testProperties1.put("host", "localhost");
        testProperties1.put("port", 30030);
        testProperties1.put("long-value", (long) Integer.MAX_VALUE * 10);
        testProperties1.put("double-value", 1.5);
        Map<String, Object> testProperties2 = new TreeMap<>();
        testProperties2.put("port", 50000);
        testProperties2.put("long-value", (long) Integer.MAX_VALUE * 10);
        testProperties2.put("double-value", 1.5);
        testProperties1.put("test", testProperties2);
        return testProperties1;
    }

    @SuppressWarnings("unchecked")
    private void assertTestProperties(Map<String, Object> actualProperties) {
        assertTrue(actualProperties.get("host") instanceof String);
        assertTrue(actualProperties.get("port") instanceof Integer);
        assertTrue(actualProperties.get("long-value") instanceof Long);
        assertTrue(actualProperties.get("double-value") instanceof Double);
        assertTrue(((Map<String, Object>) actualProperties.get("test")).get("port") instanceof Integer);
        assertTrue(((Map<String, Object>) actualProperties.get("test")).get("long-value") instanceof Long);
        assertTrue(((Map<String, Object>) actualProperties.get("test")).get("double-value") instanceof Double);
    }

    private static class Foo {

        private Map<String, Map<String, Object>> properties;

        @SuppressWarnings("unused")
        // Required by Jackson.
        public Foo() {
            this(Collections.emptyMap());
        }

        public Foo(Map<String, Map<String, Object>> properties) {
            this.properties = properties;
        }

        public Map<String, Map<String, Object>> getProperties() {
            return properties;
        }

    }

    private static class Bar {

        private Map<String, Object> properties;

        @SuppressWarnings("unused")
        // Required by Jackson.
        public Bar() {
            this(Collections.emptyMap());
        }

        public Bar(Map<String, Object> properties) {
            this.properties = properties;
        }

        public Map<String, Object> getProperties() {
            return properties;
        }

    }

}

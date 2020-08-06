package org.cloudfoundry.multiapps.common.util;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.IOUtils;
import org.cloudfoundry.multiapps.common.Messages;
import org.cloudfoundry.multiapps.common.ParsingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class JsonUtilTest {

    @Test
    void testConvertJsonToMapWithInvalidJsonInputStream() {
        String invalidJson = "{]";
        InputStream invalidJsonInputStream = IOUtils.toInputStream(invalidJson, Charset.defaultCharset());

        ParsingException resultException = Assertions.assertThrows(ParsingException.class,
                                                                   () -> JsonUtil.convertJsonToMap(invalidJsonInputStream));

        String expectedMessage = Messages.CANNOT_CONVERT_JSON_STREAM_TO_MAP + ": " + resultException.getCause()
                                                                                                    .getMessage();
        ParsingException expectedException = new ParsingException(resultException.getCause(), expectedMessage, invalidJsonInputStream);
        Assertions.assertEquals(expectedException.getMessage(), resultException.getMessage());
    }

    @Test
    void testConvertJsonToMapWithInvalidJsonString() {
        String invalidJson = "{]";

        ParsingException resultException = Assertions.assertThrows(ParsingException.class, () -> JsonUtil.convertJsonToMap(invalidJson));

        String expectedMessage = Messages.CANNOT_CONVERT_JSON_STRING_TO_MAP + ": " + resultException.getCause()
                                                                                                    .getMessage();

        ParsingException expectedException = new ParsingException(resultException.getCause(), expectedMessage, invalidJson);
        Assertions.assertEquals(expectedException.getMessage(), resultException.getMessage());
    }

    @Test
    void test1() throws Exception {
        Foo foo = new Foo(MapUtil.asMap("test1", createTestProperties()));

        String json = JsonUtil.toJson(foo, true);
        System.out.println(json);

        foo = JsonUtil.fromJson(json, Foo.class);

        Map<String, Object> actualProperties = foo.getProperties()
                                                  .get("test1");

        assertTestProperties(actualProperties);
    }

    @Test
    void test2() throws Exception {
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
    void test3() throws Exception {
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

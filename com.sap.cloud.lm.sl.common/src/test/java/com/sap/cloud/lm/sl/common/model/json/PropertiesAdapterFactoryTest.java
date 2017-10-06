package com.sap.cloud.lm.sl.common.model.json;

import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

import com.google.gson.annotations.JsonAdapter;
import com.sap.cloud.lm.sl.common.util.JsonUtil;
import com.sap.cloud.lm.sl.common.util.MapUtil;

public class PropertiesAdapterFactoryTest {

    @SuppressWarnings("unchecked")
    @Test
    public void test1() throws Exception {
        Foo foo = new Foo(MapUtil.asMap("test1", createTestProperties()));

        String json = JsonUtil.toJson(foo, true);
        // System.out.println(json);

        foo = JsonUtil.fromJson(json, Foo.class);

        Map<String, Object> nestedProperties = ((Map<String, Object>) foo.getProperties().get("test1"));
        assertTrue(nestedProperties.get("port") instanceof Integer);
        assertTrue(((Map<String, Object>) nestedProperties.get("test")).get("port") instanceof Integer);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void test2() throws Exception {
        Map<String, Object> properties = new TreeMap<String, Object>();
        properties.put("test1", createTestProperties());
        Bar bar = new Bar(properties);

        String json = JsonUtil.toJson(bar, true);
        // System.out.println(json);

        bar = JsonUtil.fromJson(json, Bar.class);

        Map<String, Object> nestedProperties = ((Map<String, Object>) bar.getProperties().get("test1"));
        assertTrue(nestedProperties.get("port") instanceof Integer);
        assertTrue(((Map<String, Object>) nestedProperties.get("test")).get("port") instanceof Integer);
    }

    private Map<String, Object> createTestProperties() {
        Map<String, Object> testProperties1 = new TreeMap<String, Object>();
        testProperties1.put("host", "localhost");
        testProperties1.put("port", 30030);
        Map<String, Object> testProperties2 = new TreeMap<String, Object>();
        testProperties2.put("port", 50000);
        testProperties1.put("test", testProperties2);
        return testProperties1;
    }

    private static class Foo {

        @JsonAdapter(PropertiesAdapterFactory.class)
        private Map<String, Map<String, Object>> properties;

        public Foo(Map<String, Map<String, Object>> properties) {
            this.properties = properties;
        }

        public Map<String, Map<String, Object>> getProperties() {
            return properties;
        }

    }

    private static class Bar {

        @JsonAdapter(PropertiesAdapterFactory.class)
        private Map<String, Object> properties;

        public Bar(Map<String, Object> properties) {
            this.properties = properties;
        }

        public Map<String, Object> getProperties() {
            return properties;
        }

    }

}

package org.cloudfoundry.multiapps.common.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.cloudfoundry.multiapps.common.ContentException;
import org.junit.jupiter.api.Test;

class MapUtilTest {

    public static final Map<String, Object> TEST_PARAMETERS = new HashMap<>();

    static {
        TEST_PARAMETERS.put("trueFlag", true);
        TEST_PARAMETERS.put("falseFlag", false);
        TEST_PARAMETERS.put("emptyFlag", null);
        TEST_PARAMETERS.put("incorrectTypeFlag1", "false");
        TEST_PARAMETERS.put("incorrectTypeFlag2", "1");
    }

    @Test
    void testParseBooleanFlag() {
        assertTrue(MapUtil.parseBooleanFlag(TEST_PARAMETERS, "trueFlag", true));
        assertTrue(MapUtil.parseBooleanFlag(TEST_PARAMETERS, "trueFlag", false));
        assertTrue(MapUtil.parseBooleanFlag(TEST_PARAMETERS, "notPresentFlag", true));
        assertTrue(MapUtil.parseBooleanFlag(TEST_PARAMETERS, "emptyFlag", true));

        assertFalse(MapUtil.parseBooleanFlag(TEST_PARAMETERS, "falseFlag", true));
        assertFalse(MapUtil.parseBooleanFlag(TEST_PARAMETERS, "notPresentFlag", false));
        assertFalse(MapUtil.parseBooleanFlag(TEST_PARAMETERS, "emptyFlag", false));

        assertThrows(ContentException.class, () -> MapUtil.parseBooleanFlag(TEST_PARAMETERS, "incorrectTypeFlag1", true));
        assertThrows(ContentException.class, () -> MapUtil.parseBooleanFlag(TEST_PARAMETERS, "incorrectTypeFlag2", true));
    }

    @Test
    void testCast_withNullMap() {
        Map<Object, Object> result = MapUtil.cast(null);

        assertNull(result);
    }

    @Test
    void testCast_withEmptyMap() {
        Map<Object, Object> emptyMap = Collections.emptyMap();

        Map<Object, Object> result = MapUtil.cast(emptyMap);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testAddNonNull_withNonNullValue() {
        Map<String, String> map = new HashMap<>();
        String key = "key1";
        String value = "value1";

        MapUtil.addNonNull(map, key, value);

        assertEquals(1, map.size());
        assertEquals(value, map.get(key));
    }

    @Test
    void testAddNonNull_withNullValue() {
        Map<String, String> map = new HashMap<>();
        String key = "key1";
        String value = null;

        MapUtil.addNonNull(map, key, value);

        assertTrue(map.isEmpty());
    }

    @Test
    void testMergeSafely_withBothMapsNonNull() {
        Map<String, String> original = new HashMap<>();
        original.put("key1", "value1");
        Map<String, String> override = new HashMap<>();
        override.put("key2", "value2");
        override.put("key1", "value2");

        Map<String, String> result = MapUtil.mergeSafely(original, override);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("value2", result.get("key1"));
        assertEquals("value2", result.get("key2"));
    }

    @Test
    void testMergeSafely_withOriginalNull() {
        Map<String, String> override = new HashMap<>();
        override.put("key1", "value1");

        Map<String, String> result = MapUtil.mergeSafely(null, override);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("value1", result.get("key1"));
    }

    @Test
    void testMergeSafely_withOverrideNull() {
        Map<String, String> original = new HashMap<>();
        original.put("key1", "value1");

        Map<String, String> result = MapUtil.mergeSafely(original, null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("value1", result.get("key1"));
    }

    @Test
    void testMergeSafely_withBothMapsNull() {
        Map<String, String> result = MapUtil.mergeSafely(null, null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}

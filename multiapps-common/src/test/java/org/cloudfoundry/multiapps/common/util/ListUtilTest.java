package org.cloudfoundry.multiapps.common.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ListUtilTest {

    @Test
    void testCast_withValidList() {
        int testNumber = 123;
        double testDouble = 45.67;
        List<Object> originalList = Arrays.asList("String", testNumber, testDouble);

        List<String> castList = ListUtil.cast(originalList);

        assertNotNull(castList);
        assertEquals(3, castList.size());
        assertEquals("String", castList.get(0));
        assertEquals(testNumber, castList.get(1));
        assertEquals(testDouble, castList.get(2));
    }

    @Test
    void testCast_withNullList() {
        List<Object> result = ListUtil.cast(null);

        assertNull(result);
    }

    @Test
    void testCast_withEmptyList() {
        List<Object> emptyList = Collections.emptyList();
        List<Object> result = ListUtil.cast(emptyList);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testAsList_withNonNullItem() {
        String item = "Test Item";
        List<String> result = ListUtil.asList(item);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(item, result.get(0));
    }

    @Test
    void testAsList_withNullItem() {
        List<String> result = ListUtil.asList(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}

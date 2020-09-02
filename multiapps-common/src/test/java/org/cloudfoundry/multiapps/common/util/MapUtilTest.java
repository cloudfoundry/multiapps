package org.cloudfoundry.multiapps.common.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
}

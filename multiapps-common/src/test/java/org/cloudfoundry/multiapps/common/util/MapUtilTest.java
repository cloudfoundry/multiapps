package org.cloudfoundry.multiapps.common.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.cloudfoundry.multiapps.common.ContentException;
import org.junit.jupiter.api.Test;

public class MapUtilTest {

    public static final Map<String, Object> testParameters = new HashMap<String, Object>() {
        {
            put("trueFlag", new Boolean(true));
            put("falseFlag", new Boolean(false));
            put("emptyFlag", null);
            put("incorrectTypeFlag1", "false");
            put("incorrectTypeFlag2", "1");
        }
    };

    @Test
    public void testParseBooleanFlag() {
        assertTrue(MapUtil.parseBooleanFlag(testParameters, "trueFlag", true));
        assertTrue(MapUtil.parseBooleanFlag(testParameters, "trueFlag", false));
        assertTrue(MapUtil.parseBooleanFlag(testParameters, "notPresentFlag", true));
        assertTrue(MapUtil.parseBooleanFlag(testParameters, "emptyFlag", true));

        assertFalse(MapUtil.parseBooleanFlag(testParameters, "falseFlag", true));
        assertFalse(MapUtil.parseBooleanFlag(testParameters, "notPresentFlag", false));
        assertFalse(MapUtil.parseBooleanFlag(testParameters, "emptyFlag", false));

        assertThrows(ContentException.class, () -> MapUtil.parseBooleanFlag(testParameters, "incorrectTypeFlag1", true));
        assertThrows(ContentException.class, () -> MapUtil.parseBooleanFlag(testParameters, "incorrectTypeFlag2", true));
    }
}

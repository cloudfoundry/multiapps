package com.sap.cloud.lm.sl.mta.util;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.common.util.TestUtil;
import com.sap.cloud.lm.sl.common.util.YamlUtil;

public class YamlUtilTest {

    @Nested
    static class WithValidDescriptor {

        private static final String DESCRIPTOR = "mtad.yaml";

        @Test
        public void testWithStream() {
            InputStream descriptorStream = TestUtil.getResourceAsInputStream(DESCRIPTOR, getClass());
            assertNotNull(YamlUtil.convertYamlToMap(descriptorStream));
        }

        @Test
        public void testWithString() {
            String descriptorString = TestUtil.getResourceAsString(DESCRIPTOR, getClass());
            assertNotNull(YamlUtil.convertYamlToMap(descriptorString));
        }

    }

    @Nested
    static class WithDescriptorContainingSecurityViolations {

        private static final String DESCRIPTOR = "mtad-with-security-violation.yaml";
        private static final String EXPECTED_EXCEPTION_MESSAGE = "could not determine a constructor for the tag tag:yaml.org,2002:javax.script.ScriptEngineManager";

        @Test
        public void testWithStream() {
            InputStream descriptorStream = TestUtil.getResourceAsInputStream(DESCRIPTOR, getClass());
            ParsingException parsingException = assertThrows(ParsingException.class, () -> YamlUtil.convertYamlToMap(descriptorStream));
            validate(parsingException);
        }

        @Test
        public void testWithString() {
            String descriptorString = TestUtil.getResourceAsString(DESCRIPTOR, getClass());
            ParsingException parsingException = assertThrows(ParsingException.class, () -> YamlUtil.convertYamlToMap(descriptorString));
            validate(parsingException);
        }

        private void validate(ParsingException parsingException) {
            Throwable cause = parsingException.getCause();
            assertTrue(cause.getMessage()
                            .contains(EXPECTED_EXCEPTION_MESSAGE));
        }

    }

}

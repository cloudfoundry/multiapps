package com.sap.cloud.lm.sl.mta.util;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.common.util.TestUtil;
import com.sap.cloud.lm.sl.common.util.YamlParser;

public class YamlParserTest {

    @Nested
    static class WithValidDescriptor {

        private static final String DESCRIPTOR = "mtad.yaml";

        @Test
        public void testWithStream() {
            YamlParser yamlParser = new YamlParser();
            InputStream descriptorStream = TestUtil.getResourceAsInputStream(DESCRIPTOR, getClass());
            assertNotNull(yamlParser.convertYamlToMap(descriptorStream));
        }

        @Test
        public void testWithString() {
            YamlParser yamlParser = new YamlParser();
            String descriptorString = TestUtil.getResourceAsString(DESCRIPTOR, getClass());
            assertNotNull(yamlParser.convertYamlToMap(descriptorString));
        }

    }

    @Nested
    static class WithDescriptorContainingSecurityViolations {

        private static final String DESCRIPTOR = "mtad-with-security-violation.yaml";
        private static final String EXPECTED_EXCEPTION_MESSAGE = "could not determine a constructor for the tag tag:yaml.org,2002:javax.script.ScriptEngineManager";

        @Test
        public void testWithStream() {
            YamlParser yamlParser = new YamlParser();
            InputStream descriptorStream = TestUtil.getResourceAsInputStream(DESCRIPTOR, getClass());
            ParsingException parsingException = assertThrows(ParsingException.class, () -> yamlParser.convertYamlToMap(descriptorStream));
            validate(parsingException);
        }

        @Test
        public void testWithString() {
            YamlParser yamlParser = new YamlParser();
            String descriptorString = TestUtil.getResourceAsString(DESCRIPTOR, getClass());
            ParsingException parsingException = assertThrows(ParsingException.class, () -> yamlParser.convertYamlToMap(descriptorString));
            validate(parsingException);
        }

        private void validate(ParsingException parsingException) {
            Throwable cause = parsingException.getCause();
            assertTrue(cause.getMessage()
                            .contains(EXPECTED_EXCEPTION_MESSAGE));
        }

    }

}

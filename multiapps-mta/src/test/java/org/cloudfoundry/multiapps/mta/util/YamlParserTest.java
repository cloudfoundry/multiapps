package org.cloudfoundry.multiapps.mta.util;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;

import org.cloudfoundry.multiapps.common.ParsingException;
import org.cloudfoundry.multiapps.common.test.TestUtil;
import org.cloudfoundry.multiapps.common.util.YamlParser;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class YamlParserTest {

    private YamlParser yamlParser = new YamlParser();

    @Nested
    class WithValidDescriptor {

        private static final String DESCRIPTOR = "mtad.yaml";

        @Test
        void testWithStream() {
            InputStream descriptorStream = TestUtil.getResourceAsInputStream(DESCRIPTOR, getClass());
            assertNotNull(yamlParser.convertYamlToMap(descriptorStream));
        }

        @Test
        void testWithString() {
            String descriptorString = TestUtil.getResourceAsString(DESCRIPTOR, getClass());
            assertNotNull(yamlParser.convertYamlToMap(descriptorString));
        }

    }

    @Nested
    class WithDescriptorContainingSecurityViolations {

        private static final String DESCRIPTOR = "mtad-with-security-violation.yaml";
        private static final String EXPECTED_EXCEPTION_MESSAGE = "Global tag is not allowed: tag:yaml.org,2002:javax.script.ScriptEngineManager";

        @Test
        void testWithStream() {
            InputStream descriptorStream = TestUtil.getResourceAsInputStream(DESCRIPTOR, getClass());
            ParsingException parsingException = assertThrows(ParsingException.class, () -> yamlParser.convertYamlToMap(descriptorStream));
            validate(parsingException);
        }

        @Test
        void testWithString() {
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

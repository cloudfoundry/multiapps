package com.sap.cloud.lm.sl.mta.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.common.util.TestUtil;

public class YamlUtilTest {

    @Nested
    class WithValidDescriptor {

        private static final String DESCRIPTOR = "mtad.yaml";

        @Test
        void testWithStream() {
            InputStream descriptorStream = TestUtil.getResourceAsInputStream(DESCRIPTOR, getClass());
            assertNotNull(YamlUtil.convertYamlToMap(descriptorStream));
        }

        @Test
        void testWithString() {
            String descriptorString = TestUtil.getResourceAsString(DESCRIPTOR, getClass());
            assertNotNull(YamlUtil.convertYamlToMap(descriptorString));
        }

        @Test
        void testConvertToYamlToMap() {
            Map<String, Object> initialMap = YamlUtil.convertYamlToMap(TestUtil.getResourceAsInputStream(DESCRIPTOR, getClass()));
            String convertedToYaml = YamlUtil.convertToYaml(initialMap);
            Yaml yaml = new Yaml(new SafeConstructor(new LoaderOptions()), new YamlRepresenter(new DumperOptions()));
            Map<String, Object> afterConvertionMap = yaml.load(convertedToYaml);

            assertEquals(initialMap, afterConvertionMap);
        }

    }

    @Nested
    class WithDescriptorContainingSecurityViolations {

        private static final String DESCRIPTOR = "mtad-with-security-violation.yaml";
        private static final String EXPECTED_EXCEPTION_MESSAGE = "Global tag is not allowed: tag:yaml.org,2002:javax.script.ScriptEngineManager";

        @Test
        void testWithStream() {
            InputStream descriptorStream = TestUtil.getResourceAsInputStream(DESCRIPTOR, getClass());
            ParsingException parsingException = assertThrows(ParsingException.class, () -> YamlUtil.convertYamlToMap(descriptorStream));
            validate(parsingException);
        }

        @Test
        void testWithString() {
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

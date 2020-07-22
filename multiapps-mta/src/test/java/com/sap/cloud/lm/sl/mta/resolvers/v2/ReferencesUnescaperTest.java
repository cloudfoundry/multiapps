package com.sap.cloud.lm.sl.mta.resolvers.v2;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.sap.cloud.lm.sl.common.util.TestUtil;
import com.sap.cloud.lm.sl.common.util.Tester;
import com.sap.cloud.lm.sl.common.util.Tester.Expectation;
import com.sap.cloud.lm.sl.mta.handlers.DescriptorParserFacade;
import com.sap.cloud.lm.sl.mta.model.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.resolvers.ReferencesUnescaper;

public class ReferencesUnescaperTest {

    private Tester tester = Tester.forClass(getClass());
    private ReferencesUnescaper referencesUnescaper = new ReferencesUnescaper();

    public static Stream<Arguments> testUnescaping() {
        return Stream.of(
// @formatter:off
            Arguments.of("mtad-with-escaped-placeholders.yaml", new Expectation(Expectation.Type.JSON, "result-from-unescaping-placeholders.json")),
            Arguments.of("mtad-with-escaped-references.yaml", new Expectation(Expectation.Type.JSON, "result-from-unescaping-references.json"))
// @formatter:on
        );
    }

    @ParameterizedTest
    @MethodSource
    public void testUnescaping(String descriptorResource, Expectation expectation) {
        DeploymentDescriptor descriptor = parseDeploymentDescriptor(descriptorResource);
        tester.test(() -> {
            referencesUnescaper.unescapeReferences(descriptor);
            return descriptor;
        }, expectation);
    }

    private DeploymentDescriptor parseDeploymentDescriptor(String descriptorResource) {
        String yaml = TestUtil.getResourceAsString(descriptorResource, getClass());
        return new DescriptorParserFacade().parseDeploymentDescriptor(yaml);
    }

}

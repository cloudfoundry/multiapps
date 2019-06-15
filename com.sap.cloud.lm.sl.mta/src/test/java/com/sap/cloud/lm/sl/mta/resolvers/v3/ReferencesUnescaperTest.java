package com.sap.cloud.lm.sl.mta.resolvers.v3;

import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

import com.sap.cloud.lm.sl.common.util.Tester.Expectation;

public class ReferencesUnescaperTest extends com.sap.cloud.lm.sl.mta.resolvers.v2.ReferencesUnescaperTest {

    public static Stream<Arguments> testUnescaping() {
        return Stream.of(
// @formatter:off
            Arguments.of("mtad-with-escaped-placeholders.yaml", new Expectation(Expectation.Type.JSON, "result-from-unescaping-placeholders.json")),
            Arguments.of("mtad-with-escaped-references.yaml", new Expectation(Expectation.Type.JSON, "result-from-unescaping-references.json"))
// @formatter:on
        );
    }

}

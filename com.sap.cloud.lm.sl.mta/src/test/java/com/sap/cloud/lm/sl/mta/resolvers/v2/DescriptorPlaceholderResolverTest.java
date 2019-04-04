package com.sap.cloud.lm.sl.mta.resolvers.v2;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.sap.cloud.lm.sl.common.util.TestUtil;
import com.sap.cloud.lm.sl.common.util.TestUtil.Expectation;
import com.sap.cloud.lm.sl.mta.handlers.DescriptorParserFacade;
import com.sap.cloud.lm.sl.mta.model.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.resolvers.ResolverBuilder;

@RunWith(Parameterized.class)
public class DescriptorPlaceholderResolverTest {

    protected final String descriptorLocation;
    protected final Expectation expectation;

    protected DescriptorPlaceholderResolver resolver;

    @Parameters
    public static Iterable<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            // (00)
            {
                "mtad-with-placeholders-in-requires-dependency.yaml", new Expectation(Expectation.Type.RESOURCE, "result-from-placeholders-in-requires-dependency.json"),
            },
            // (01)
            {
                "mtad-with-placeholders-in-provides-dependency.yaml", new Expectation(Expectation.Type.RESOURCE, "result-from-placeholders-in-provides-dependency.json"),
            },
            // (02)
            {
                "mtad-with-placeholders-in-resource.yaml", new Expectation(Expectation.Type.RESOURCE, "result-from-placeholders-in-resource.json"),
            },
            // (03)
            {
                "mtad-with-placeholders-in-module.yaml", new Expectation(Expectation.Type.RESOURCE, "result-from-placeholders-in-module.json"),
            },
            // (04)
            {
                "mtad-with-placeholders-in-general-parameters.yaml", new Expectation(Expectation.Type.RESOURCE, "result-from-placeholders-in-general-parameters.json"),
            },
            // (05)
            {
                "mtad-with-concatenated-placeholders.yaml", new Expectation(Expectation.Type.RESOURCE, "result-from-concatenated-parameters.json"),
            },
            // (06)
            {
                "mtad-with-unresolvable-requires-dependency-properties.yaml", new Expectation(Expectation.Type.EXCEPTION, "Unable to resolve \"foo#bar#non-existing\""),
            },
            // (07)
            {
                "mtad-with-unresolvable-requires-dependency-parameters.yaml", new Expectation(Expectation.Type.EXCEPTION, "Unable to resolve \"foo#bar#non-existing\""),
            },
            // (08)
            {
                "mtad-with-unresolvable-general-parameters.yaml", new Expectation(Expectation.Type.EXCEPTION, "Unable to resolve \"non-existing\""),
            },
            // (09)
            {
                "mtad-with-unresolvable-module-properties.yaml", new Expectation(Expectation.Type.EXCEPTION, "Unable to resolve \"foo#non-existing\""),
            },
            // (10)
            {
                "mtad-with-unresolvable-module-parameters.yaml", new Expectation(Expectation.Type.EXCEPTION, "Unable to resolve \"foo#non-existing\""),
            },
            // (11)
            {
                "mtad-preservation-of-types.yaml", new Expectation(Expectation.Type.RESOURCE, "result-preservation-of-types.json"),
            },
            // (12)
            {
                "mtad-with-placeholder-in-a-nested-structure.yaml", new Expectation(Expectation.Type.RESOURCE, "result-from-placeholder-in-a-nested-structure.json"),
            },
            // (13)
            {
                "mtad-with-circular-reference-in-requires-dependency.yaml", new Expectation(Expectation.Type.EXCEPTION, "Circular reference detected in \"foo#bar#a\""),
            },
            // (14)
            {
                "mtad-with-circular-reference-in-resource.yaml", new Expectation(Expectation.Type.EXCEPTION, "Circular reference detected in \"bar#a\""),
            },
            // (15)
            {
                "mtad-with-circular-reference-in-module.yaml", new Expectation(Expectation.Type.EXCEPTION, "Circular reference detected in \"foo#a\""),
            },
            // (16)
            {
                "mtad-with-complex-circular-reference.yaml", new Expectation(Expectation.Type.EXCEPTION, "Circular reference detected in \"a\""),
            },
            // (17)
            {
                "mtad-with-repeating-placeholder.yaml", new Expectation(Expectation.Type.RESOURCE, "result-from-repeating-placeholder.json"),
            },
// @formatter:on
        });
    }

    public DescriptorPlaceholderResolverTest(String descriptorLocation, Expectation expectation) {
        this.descriptorLocation = descriptorLocation;
        this.expectation = expectation;
    }

    @Before
    public void initialize() throws Exception {
        DeploymentDescriptor deploymentDescriptor = parseDeploymentDescriptor();
        this.resolver = createDescriptorPlaceholderResolver(deploymentDescriptor);
    }

    protected DeploymentDescriptor parseDeploymentDescriptor() {
        DescriptorParserFacade parser = new DescriptorParserFacade();
        InputStream descriptor = getClass().getResourceAsStream(descriptorLocation);
        return parser.parseDeploymentDescriptor(descriptor);
    }

    protected DescriptorPlaceholderResolver createDescriptorPlaceholderResolver(DeploymentDescriptor deploymentDescriptor) {
        return new DescriptorPlaceholderResolver(deploymentDescriptor, new ResolverBuilder(), new ResolverBuilder(),
            Collections.emptyMap());
    }

    @Test
    public void testResolve() {
        TestUtil.test(() -> resolver.resolve(), expectation, getClass());
    }

}

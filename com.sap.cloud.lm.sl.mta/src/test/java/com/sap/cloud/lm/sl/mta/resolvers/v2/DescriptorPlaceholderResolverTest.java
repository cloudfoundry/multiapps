package com.sap.cloud.lm.sl.mta.resolvers.v2;

import java.io.InputStream;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.sap.cloud.lm.sl.common.util.JsonUtil;
import com.sap.cloud.lm.sl.common.util.TestUtil;
import com.sap.cloud.lm.sl.common.util.TestUtil.Expectation;
import com.sap.cloud.lm.sl.mta.handlers.DescriptorParserFacade;
import com.sap.cloud.lm.sl.mta.model.SystemParameters;
import com.sap.cloud.lm.sl.mta.model.v2.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.resolvers.ResolverBuilder;

@RunWith(Parameterized.class)
public class DescriptorPlaceholderResolverTest {

    protected static final String SYSTEM_PARAMETERS_LOCATION = "system-parameters.json";

    protected final String descriptorLocation;
    protected final Expectation expectation;

    protected DescriptorPlaceholderResolver resolver;

    @Parameters
    public static Iterable<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            // (00) Placeholder in requires dependency:
            {
                "mtad-01.yaml", new Expectation(Expectation.Type.RESOURCE, "result-01.json"),
            },
            // (01) Concatenation of placeholders:
            {
                "mtad-02.yaml", new Expectation(Expectation.Type.RESOURCE, "result-02.json"),
            },
            // (02) Placeholder in extension descriptor properties:
            {
                "mtad-03.yaml", new Expectation(Expectation.Type.RESOURCE, "result-03.json"),
            },
            // (03) Placeholder in module:
            {
                "mtad-04.yaml", new Expectation(Expectation.Type.RESOURCE, "result-04.json"),
            },
            // (04) Placeholder in resource:
            {
                "mtad-05.yaml", new Expectation(Expectation.Type.RESOURCE, "result-05.json"),
            },
            // (05) Placeholder in provided dependency:
            {
                "mtad-06.yaml", new Expectation(Expectation.Type.RESOURCE, "result-06.json"),
            },
            // (06) Unable to resolve placeholder in requires dependency:
            {
                "mtad-07.yaml", new Expectation(Expectation.Type.EXCEPTION, "Unable to resolve \"pricing#pricing-db#non-existing\""),
            },
            // (07) Unable to resolve placeholder in extension descriptor properties:
            {
                "mtad-08.yaml", new Expectation(Expectation.Type.EXCEPTION, "Unable to resolve \"non-existing\""),
            },
            // (08) Unable to resolve placeholder in module:
            {
                "mtad-09.yaml", new Expectation(Expectation.Type.EXCEPTION, "Unable to resolve \"pricing#non-existing\""),
            },
            // (09) Placeholder in untyped resource:
            {
                "mtad-10.yaml", new Expectation(Expectation.Type.RESOURCE, "result-10.json"),
            },
            // (12) Placeholder in requires dependency (default-uri):
            {
                "mtad-13.yaml", new Expectation(Expectation.Type.RESOURCE, "result-13.json"),
            },
            // (13) Placeholders in a map in resource parameters:
            {
                "mtad-14.yaml", new Expectation(Expectation.Type.RESOURCE, "result-14.json"),
            },
            // (14) Placeholders in a list in a map in resource parameters:
            {
                "mtad-15.yaml", new Expectation(Expectation.Type.RESOURCE, "result-15.json"),
            },
            // (15) Circular reference in a module parameter:
            {
                "mtad-16.yaml", new Expectation(Expectation.Type.EXCEPTION, "Circular reference detected in \"foo#bar#test_1\""),
            },
            // (16) Circular reference in a module parameter:
            {
                "mtad-17.yaml", new Expectation(Expectation.Type.EXCEPTION, "Circular reference detected in \"foo#test_1\""),
            },
            // (17) Global MTA parameters to be resolved in lower scopes:
            {
                "mtad-18.yaml", new Expectation(Expectation.Type.RESOURCE, "result-16.json"),  
            },
            // (18) The same placeholder occurs multiple times in the same parameter:
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
        SystemParameters systemParameters = JsonUtil.fromJson(TestUtil.getResourceAsString(SYSTEM_PARAMETERS_LOCATION, getClass()),
            SystemParameters.class);
        this.resolver = createDescriptorPlaceholderResolver(deploymentDescriptor, systemParameters);
    }

    protected DeploymentDescriptor parseDeploymentDescriptor() {
        DescriptorParserFacade parser = new DescriptorParserFacade();
        InputStream descriptor = getClass().getResourceAsStream(descriptorLocation);
        return parser.parseDeploymentDescriptor(descriptor);
    }

    protected DescriptorPlaceholderResolver createDescriptorPlaceholderResolver(DeploymentDescriptor deploymentDescriptor,
        SystemParameters systemParameters) {
        return new DescriptorPlaceholderResolver(deploymentDescriptor, systemParameters, new ResolverBuilder(), new ResolverBuilder());
    }

    @Test
    public void testResolve() {
        TestUtil.test(() -> resolver.resolve(), expectation, getClass());
    }

}

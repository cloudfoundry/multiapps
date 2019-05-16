package com.sap.cloud.lm.sl.mta.resolvers.v2;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.sap.cloud.lm.sl.common.util.Callable;
import com.sap.cloud.lm.sl.common.util.Tester;
import com.sap.cloud.lm.sl.common.util.Tester.Expectation;
import com.sap.cloud.lm.sl.mta.MtaTestUtil;
import com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorParser;
import com.sap.cloud.lm.sl.mta.model.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.resolvers.ResolverBuilder;

@RunWith(Parameterized.class)
public class DescriptorReferenceResolverTest {

    private final Tester tester = Tester.forClass(getClass());

    private final String mergedDescriptorLocation;
    private final Expectation expectation;

    private DeploymentDescriptor mergedDescriptor;
    private DescriptorReferenceResolver resolver;

    @Parameters
    public static Iterable<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            // (0) Resolve references to string properties:
            {
                "merged-01.yaml", new Expectation(Expectation.Type.JSON, "resolved-01.yaml.json"),
            },
            // (1) Resolve references to object properties:
            {
                "merged-02.yaml", new Expectation(Expectation.Type.JSON, "resolved-02.yaml.json"),
            },
            // (2) Test alternative grouping of properties when there is no corresponding requires dependency:
            {
                "merged-05.yaml", new Expectation(Expectation.Type.EXCEPTION, "Module \"foo\" does not contain a required dependency for \"bar\", but contains references to its properties"),
            },
            // (3) Attempt to resolve references to non-existing properties:
            {
                "merged-03.yaml", new Expectation(Expectation.Type.EXCEPTION, "Unable to resolve \"foo#baz#non-existing\""),
            },
            // (4) Test support for partial plugins:
            {
                "merged-04.yaml", new Expectation(Expectation.Type.JSON, "resolved-04.yaml.json"),
            },
            // (5) TODO: Add description here.
            {
                "merged-06.yaml", new Expectation(Expectation.Type.JSON, "resolved-05.yaml.json"),
            },
            // (6) The same reference occurs multiple times in the same property:
            {
                "mtad-with-repeating-reference.yaml", new Expectation(Expectation.Type.JSON, "result-from-repeating-reference.json"),
            },
// @formatter:on
        });
    }

    public DescriptorReferenceResolverTest(String mergedDescriptorLocation, Expectation expectation) {
        this.mergedDescriptorLocation = mergedDescriptorLocation;
        this.expectation = expectation;
    }

    @Before
    public void setUp() throws Exception {
        mergedDescriptor = MtaTestUtil.loadDeploymentDescriptor(mergedDescriptorLocation, new DescriptorParser(), getClass());
        resolver = new DescriptorReferenceResolver(mergedDescriptor, new ResolverBuilder(), new ResolverBuilder());
    }

    @Test
    public void testResolve() {
        tester.test(new Callable<DeploymentDescriptor>() {
            @Override
            public DeploymentDescriptor call() throws Exception {
                return resolver.resolve();
            }
        }, expectation);
    }

}

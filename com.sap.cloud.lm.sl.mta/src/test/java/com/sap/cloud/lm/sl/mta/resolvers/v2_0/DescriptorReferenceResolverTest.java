package com.sap.cloud.lm.sl.mta.resolvers.v2_0;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.sap.cloud.lm.sl.common.util.Callable;
import com.sap.cloud.lm.sl.common.util.TestUtil;
import com.sap.cloud.lm.sl.mta.MtaTestUtil;
import com.sap.cloud.lm.sl.mta.handlers.v2_0.DescriptorParser;
import com.sap.cloud.lm.sl.mta.model.v2_0.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.resolvers.ResolverBuilder;

@RunWith(Parameterized.class)
public class DescriptorReferenceResolverTest {

    private final String mergedDescriptorLocation;
    private final String expected;

    private DeploymentDescriptor mergedDescriptor;
    private DescriptorReferenceResolver resolver;

    @Parameters
    public static Iterable<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            // (0) Resolve references to string properties:
            {
                "merged-01.yaml", "R:resolved-01.yaml.json",
            },
            // (1) Resolve references to object properties:
            {
                "merged-02.yaml", "R:resolved-02.yaml.json",
            },
            // (2) Test alternative grouping of properties when there is no corresponding requires dependency:
            {
                "merged-05.yaml", "E:Module \"foo\" does not contain a required dependency for \"bar\", but contains references to its properties",
            },
            // (3) Attempt to resolve references to non-existing properties:
            {
                "merged-03.yaml", "E:Unable to resolve \"foo#baz#non-existing\"",
            },
            // (4) Test support for partial plugins:
            {
                "merged-04.yaml", "R:resolved-04.yaml.json",
            },
            // (5) TODO: Add description here.
            {
                "merged-06.yaml", "R:resolved-05.yaml.json",
            },
            // (6) The same reference occurs multiple times in the same property:
            {
                "mtad-with-repeating-reference.yaml", "R:result-from-repeating-reference.json",
            },
// @formatter:on
        });
    }

    public DescriptorReferenceResolverTest(String mergedDescriptorLocation, String expected) {
        this.mergedDescriptorLocation = mergedDescriptorLocation;
        this.expected = expected;
    }

    @Before
    public void setUp() throws Exception {
        mergedDescriptor = (DeploymentDescriptor) MtaTestUtil.loadDeploymentDescriptor(mergedDescriptorLocation, new DescriptorParser(),
            getClass());
        resolver = new DescriptorReferenceResolver(mergedDescriptor, new ResolverBuilder(), new ResolverBuilder());
    }

    @Test
    public void testResolve() {
        TestUtil.test(new Callable<DeploymentDescriptor>() {
            @Override
            public DeploymentDescriptor call() throws Exception {
                return resolver.resolve();
            }
        }, expected, getClass());
    }

}

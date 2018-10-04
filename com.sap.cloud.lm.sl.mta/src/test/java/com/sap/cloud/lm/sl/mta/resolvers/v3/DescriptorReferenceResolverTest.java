package com.sap.cloud.lm.sl.mta.resolvers.v3;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.sap.cloud.lm.sl.common.util.Callable;
import com.sap.cloud.lm.sl.common.util.TestUtil;
import com.sap.cloud.lm.sl.mta.MtaTestUtil;
import com.sap.cloud.lm.sl.mta.handlers.v3.DescriptorParser;
import com.sap.cloud.lm.sl.mta.model.v3.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.resolvers.ResolverBuilder;
import com.sap.cloud.lm.sl.mta.resolvers.v3.DescriptorReferenceResolver;

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
            // (0) Resolve references in resources:
            {
                "merged-01.yaml", "R:resolved-01.yaml.json",
            },
            // (1) Resolve references in resources - cyclic dependencies & corner cases:
            {
                "merged-02.yaml", "R:resolved-02.yaml.json",
            },
            // (2) Test error reporting on failure to resolve value:
            {
                "merged-03.yaml", "E:Unable to resolve \"baz##non-existing\"",
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
        resolver = new DescriptorReferenceResolver(mergedDescriptor, new ResolverBuilder(), new ResolverBuilder(), new ResolverBuilder());
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

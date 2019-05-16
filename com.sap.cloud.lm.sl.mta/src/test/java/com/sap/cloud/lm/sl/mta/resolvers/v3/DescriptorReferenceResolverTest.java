package com.sap.cloud.lm.sl.mta.resolvers.v3;

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
import com.sap.cloud.lm.sl.mta.handlers.v3.DescriptorParser;
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
            // (0) Resolve references in resources:
            {
                "merged-01.yaml", new Expectation(Expectation.Type.JSON, "resolved-01.yaml.json"),
            },
            // (1) Resolve references in resources - cyclic dependencies & corner cases:
            {
                "merged-02.yaml", new Expectation(Expectation.Type.JSON, "resolved-02.yaml.json"),
            },
            // (2) Test error reporting on failure to resolve value:
            {
                "merged-03.yaml", new Expectation(Expectation.Type.EXCEPTION, "Unable to resolve \"baz##non-existing\""),
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
        resolver = new DescriptorReferenceResolver(mergedDescriptor, new ResolverBuilder(), new ResolverBuilder(), new ResolverBuilder());
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

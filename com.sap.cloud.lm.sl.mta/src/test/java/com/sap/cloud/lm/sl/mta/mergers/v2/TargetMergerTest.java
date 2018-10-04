package com.sap.cloud.lm.sl.mta.mergers.v2;

import java.util.Arrays;
import java.util.Collection;

import org.junit.runners.Parameterized.Parameters;

import com.sap.cloud.lm.sl.mta.handlers.v1.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.mergers.v2.TargetMerger;
import com.sap.cloud.lm.sl.mta.model.v1.Target;

public class TargetMergerTest extends com.sap.cloud.lm.sl.mta.mergers.v1.TargetMergerTest {

    @Parameters
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            // (0) There are properties in every deployment descriptor and target  platform component:
            {
                "mtad-00.yaml", "target-00.json", "R:result-target-00.json",
            },
            // (1) Some deployment descriptor components do not have a corresponding deploy target component:
            {
                "mtad-01.yaml", "target-01.json", "R:result-target-01.json",
            },
            // (2) Some deploy target properties override properties from the deployment descriptor:
            {
                "mtad-00.yaml", "target-02.json", "R:result-target-02.json",
            },
// @formatter:on
        });
    }

    public TargetMergerTest(String deploymentDescriptorYamlLocation, String targetJsonLocation, String expectedJsonLocation) {
        super(deploymentDescriptorYamlLocation, targetJsonLocation, expectedJsonLocation);
    }

    @Override
    protected int getMajorSchemaVersion() {
        return 2;
    }

    @Override
    protected TargetMerger getTargetMerger(Target target, DescriptorHandler handler) {
        return new com.sap.cloud.lm.sl.mta.mergers.v2.TargetMerger((com.sap.cloud.lm.sl.mta.model.v2.Target) target,
            (com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorHandler) handler);
    }

}

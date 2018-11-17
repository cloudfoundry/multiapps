package com.sap.cloud.lm.sl.mta.handlers.v3;

import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.sap.cloud.lm.sl.common.util.TestUtil.Expectation;

@RunWith(value = Parameterized.class)
public class DescriptorValidatorTest extends com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorValidatorTest {

    @BeforeClass
    public static void setPlatformsInformation() {
        targetName = "CF-QUAL";
        platformsLocation = "/mta/sample/v3/platforms-01.json";
        targetsLocation = "/mta/sample/v3/targets-01.json";
    }

    @Parameters
    public static Iterable<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            // (00) Valid deployment and extension descriptors without metadata:
            {
                "/mta/sample/v3/mtad-01.yaml", new String[] { "/mta/sample/v3/config-01.mtaext" }, null, null,
                new Expectation[] {
                    new Expectation(""),
                    new Expectation(""),
                    new Expectation(Expectation.Type.DO_NOT_RUN, null),
                },
            },
            // (01) Deploy target not listed in merged descriptor (there are other platforms listed) without metadata:
            {
                null, null, "/mta/sample/v3/merged-01.yaml", new String[] { "CF-TEST", "CF-PROD", },
                new Expectation[] {
                    new Expectation(Expectation.Type.DO_NOT_RUN, null),
                    new Expectation(Expectation.Type.DO_NOT_RUN, null),
                    new Expectation(Expectation.Type.EXCEPTION, "Deploy target \"CF-QUAL\" not listed in extension descriptor chain"),
                },
            },
            // (02) With no extension descriptors and non-modifiable value:
            {
                "/mta/sample/v3/mtad-02.yaml", new String[] { }, null, null,
                new Expectation[] {
                    new Expectation(Expectation.Type.EXCEPTION, "The parameter \"web-server#test\" is not optional and has no value."),
                    new Expectation(Expectation.Type.DO_NOT_RUN, null),
                    new Expectation(Expectation.Type.DO_NOT_RUN, null),
                },
            },
            // (03) With unknown module in ext descriptor:
            {
                "/mta/sample/v3/mtad-02.yaml", new String[] { "/mta/sample/v3/config-02.mtaext" }, null, null,
                new Expectation[] {
                    new Expectation(Expectation.Type.DO_NOT_RUN, null),
                    new Expectation(Expectation.Type.EXCEPTION, "Unknown module \"web-serverx\" in extension descriptor \"com.sap.mta.sample.v3.config-02\""),
                    new Expectation(Expectation.Type.DO_NOT_RUN, null),
                },
            },
            // (04) With non-modifiable property in the descriptor:
            {
                "/mta/sample/v3/mtad-02.yaml", new String[] { "/mta/sample/v3/config-03.mtaext" }, null, null,
                new Expectation[] {
                    new Expectation(Expectation.Type.DO_NOT_RUN, null),
                    new Expectation(Expectation.Type.EXCEPTION, "Cannot modify property \"web-server#test\" in extension descriptor \"com.sap.mta.sample.v3.config-03\""),
                    new Expectation(Expectation.Type.DO_NOT_RUN, null),
                },
            },
            // (05) With empty parameter in the deployment descriptor which cannot be overwritten:
            {
                "/mta/sample/v3/mtad-02.yaml", new String[] { "/mta/sample/v3/config-04.mtaext" }, null, null,
                new Expectation[] {
                    new Expectation(Expectation.Type.EXCEPTION, "The parameter \"web-server#test\" is not optional and has no value."),
                    new Expectation(Expectation.Type.DO_NOT_RUN, null),
                    new Expectation(Expectation.Type.DO_NOT_RUN, null),
                },
            },
            // (06) With overwriteable parameter in the deployment descriptor:
            {
                "/mta/sample/v3/mtad-03.yaml", new String[] { "/mta/sample/v3/config-05.mtaext" }, null, null,
                new Expectation[] {
                    new Expectation(""),
                    new Expectation(Expectation.Type.EXCEPTION, "Cannot modify parameter \"web-server#test\" in extension descriptor \"com.sap.mta.sample.v3.config-05\""),
                    new Expectation(Expectation.Type.DO_NOT_RUN, null),
                },
            },
            // (07) With non-overwritable parameter in the merged descriptor:
            {
                null, null, "/mta/sample/v3/merged-03.yaml", new String[] { "CF-TEST", "CF-QUAL", },
                new Expectation[] {
                    new Expectation(Expectation.Type.DO_NOT_RUN, null),
                    new Expectation(Expectation.Type.DO_NOT_RUN, null),
                    new Expectation(Expectation.Type.EXCEPTION, "The parameter \"web-server#test\" is not optional and has no value."),
                },
            },
            // (08) With overwriteable parameter in the descriptor:
            {
                "/mta/sample/v3/mtad-04.yaml", new String[] { "/mta/sample/v3/config-06.mtaext" }, null, null,
                new Expectation[] {
                    new Expectation(""),
                    new Expectation(Expectation.Type.EXCEPTION, "Cannot modify parameter \"web-server#test\" in extension descriptor \"com.sap.mta.sample.v3.config-05\""),
                    new Expectation(Expectation.Type.DO_NOT_RUN, null),
                },
            },
            // (09) Merged descriptor contains properties and parameters with empty values (but not null):
            {
                null, null, "/mta/sample/v3/merged-06.yaml", new String[] {},
                new Expectation[] {
                    new Expectation(Expectation.Type.DO_NOT_RUN, null),
                    new Expectation(Expectation.Type.DO_NOT_RUN, null),
                    new Expectation(""), },
            },
// @formatter:on
        });
    }

    public DescriptorValidatorTest(String deploymentDescriptorLocation, String[] extensionDescriptorLocations,
        String mergedDescriptorLocation, String[] targets, Expectation[] expectations) {
        super(deploymentDescriptorLocation, extensionDescriptorLocations, mergedDescriptorLocation, targets, expectations);
    }

    @Override
    protected DescriptorParser getDescriptorParser() {
        return new DescriptorParser();
    }

    @Override
    protected ConfigurationParser getConfigurationParser() {
        return new ConfigurationParser();
    }

    @Override
    protected DescriptorValidator createValidator() {
        return new DescriptorValidator();
    }

}

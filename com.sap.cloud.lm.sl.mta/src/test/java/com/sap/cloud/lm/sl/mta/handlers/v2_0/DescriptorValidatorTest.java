package com.sap.cloud.lm.sl.mta.handlers.v2_0;

import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class DescriptorValidatorTest extends com.sap.cloud.lm.sl.mta.handlers.v1_0.DescriptorValidatorTest {

    @BeforeClass
    public static void setTargetsInformation() {
        targetName = "CF-QUAL";
        platformsLocation = "/mta/sample/v2_0/platforms-01.json";
        targetsLocation = "/mta/sample/v2_0/targets-01.json";
    }

    @Parameters
    public static Iterable<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
                // (00) Valid deployment and extension descriptors:
                {
                    "/mta/sample/v2_0/mtad-01.yaml", new String[] { "/mta/sample/v2_0/config-01.mtaext" }, null, null, new String[] { "", "", "S", },
                },
                // (01) Deploy target not listed in merged descriptor (there are other platforms listed):
                {
                    null, null, "/mta/sample/v2_0/merged-01.yaml", new String[] { "CF-TEST", "CF-PROD", }, new String[] { "S", "S", "E:Deploy target \"CF-QUAL\" not listed in extension descriptor chain", },
                },
                // (02) Deploy target not listed in merged descriptor (there aren't any other platforms listed):
                {
                    null, null, "/mta/sample/v2_0/merged-01.yaml", new String[] {}, new String[] { "S", "S", "", },
                },
                // (03) Unresolved properties in merged descriptor:
                {
                    null, null, "/mta/sample/v2_0/merged-03.yaml", new String[] { "CF-QUAL", }, new String[] { "S", "S", "E:Unresolved mandatory properties: [competitor-data#test, pricing#internal-odata#test, pricing-db#pricing-db-service#test, pricing-db#test]", },
                },
                // (04) Unknown module in extension descriptor:
                {
                    "/mta/sample/v2_0/mtad-01.yaml", new String[] { "/mta/sample/v2_0/config-02.mtaext" }, null, null, new String[] { "S", "E:Unknown module \"web-serverx\" in extension descriptor \"com.sap.mta.sample.v2.config-02\"", "S", },
                },
                // (05) Unknown provided dependency in extension descriptor:
                {
                    "/mta/sample/v2_0/mtad-01.yaml", new String[] { "/mta/sample/v2_0/config-03.mtaext" }, null, null, new String[] { "S", "E:Unknown provided dependency \"internal-odatax\" for module \"pricing\" in extension descriptor \"com.sap.mta.sample.v2.config-03\"", "S", },
                },
                // (06) Unknown resource in extension descriptor:
                {
                    "/mta/sample/v2_0/mtad-01.yaml", new String[] { "/mta/sample/v2_0/config-04.mtaext" }, null, null, new String[] { "", "E:Unknown resource \"internal-odata-servicex\" in extension descriptor \"com.sap.mta.sample.v2.config-04\"", "S", },
                },
                // (07) Unsupported resource type in deployment descriptor:
                {
                    "/mta/sample/v2_0/mtad-02.yaml", null, null, null, new String[] { "E:Unsupported resource type \"com.sap.hana.hdi-containerx\" for platform type \"CLOUD-FOUNDRY\"", "S", "S", },
                },
                // (08) Unresolved required dependency in deployment descriptor:
                {
                    "/mta/sample/v2_0/mtad-03.yaml", null, null, null, new String[] { "E:Unresolved required dependency \"internal-odatax\" for module \"web-server\"", "S", "S", },
                },
                // (09) Unsupported module type in deployment descriptor:
                {
                    "/mta/sample/v2_0/mtad-04.yaml", null, null, null, new String[] { "E:Unsupported module type \"com.sap.static-contentx\" for platform type \"CLOUD-FOUNDRY\"", "S", "S", },
                },
                // (10) Unresolved parameters in merged descriptor:
                {
                    null, null, "merged-01.yaml", new String[] {}, new String[] { "S", "S", "E:Unresolved mandatory parameters: [baz#bar#test-5, baz#test-3, caz#test-6, foo#test-1, test-7]", },
                },
                // (11) Unknown required dependency in extension descriptor:
                {
                    "/mta/sample/v2_0/mtad-01.yaml", new String[] { "/mta/sample/v2_0/config-09.mtaext", }, null, null, new String[] { "S", "E:Unknown required dependency \"pricing-dbx\" for module \"pricing\" in extension descriptor \"com.sap.mta.sample.v2.config-05\"", "S", },
                },
                // (12) Extension descriptor attempts to modify properties:
                {
                    "/mta/sample/v2_0/mtad-01.yaml", new String[] { "/mta/sample/v2_0/config-07.mtaext", }, null, null, new String[]{"S", "E:Cannot modify property \"web-server#docu-url\" in extension descriptor \"com.sap.mta.sample.v2.config-07\"", "S"},
                },
                // (13) Extension descriptor attempts to modify parameters:
                {
                    "/mta/sample/v2_0/mtad-01.yaml", new String[] { "/mta/sample/v2_0/config-10.mtaext", }, null, null,new String[]{"S", "E:Cannot modify parameter \"web-server#host\" in extension descriptor \"com.sap.mta.sample.v2.config-10\"", "S"},
                },
                // (14) Extension descriptor attempts to modify parameters:
                {
                    "/mta/sample/v2_0/mtad-01.yaml", new String[] { "/mta/sample/v2_0/config-11.mtaext", }, null, null, new String[]{"S", "E:Cannot modify parameter \"com.sap.releaseMetadataRefGuid\" in extension descriptor \"com.sap.mta.sample.v2.config-11\"", "S"},
                },
                // (15) Merged descriptor contains properties and parameters with empty values (but not null):
                {
                    null, null, "/mta/sample/v2_0/merged-06.yaml", new String[] {}, new String[] { "S", "S", "" },
                },
// @formatter:on
        });
    }

    public DescriptorValidatorTest(String deploymentDescriptorLocation, String[] extensionDescriptorLocations,
        String mergedDescriptorLocation, String[] targets, String[] expected) {
        super(deploymentDescriptorLocation, extensionDescriptorLocations, mergedDescriptorLocation, targets, expected);
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

package com.sap.cloud.lm.sl.mta.handlers.v1;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.sap.cloud.lm.sl.common.util.Runnable;
import com.sap.cloud.lm.sl.common.util.TestUtil;
import com.sap.cloud.lm.sl.common.util.TestUtil.Expectation;
import com.sap.cloud.lm.sl.mta.MtaTestUtil;
import com.sap.cloud.lm.sl.mta.model.v1.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v1.ExtensionDescriptor;
import com.sap.cloud.lm.sl.mta.model.v1.Platform;

@RunWith(Parameterized.class)
public class DescriptorValidatorTest {

    protected static String platformLocation;

    private final String deploymentDescriptorLocation;
    private final String[] extensionDescriptorLocations;
    private final String mergedDescriptorLocation;
    private final Expectation[] expectations;

    private DeploymentDescriptor deploymentDescriptor;
    private List<ExtensionDescriptor> extensionDescriptors;
    private DeploymentDescriptor mergedDescriptor;
    private Platform platform;

    private DescriptorValidator validator;

    @BeforeClass
    public static void setPlatformsInformation() {
        platformLocation = "/mta/sample/v1/platform-01.json";
    }

    @Parameters
    public static Iterable<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            // (0) Valid deployment and extension descriptors:
            {
                "/mta/sample/v1/mtad-01.yaml", new String[] { "/mta/sample/v1/config-01.mtaext" }, null,
                new Expectation[] {
                    new Expectation(null),
                    new Expectation(null),
                    new Expectation(Expectation.Type.SKIP, null),
                },
            },
            // (1) Deploy target not listed in merged descriptor (there aren't any other targets listed):
            {
                null, null, "/mta/sample/v1/merged-01.yaml",
                new Expectation[] {
                    new Expectation(Expectation.Type.SKIP, null),
                    new Expectation(Expectation.Type.SKIP, null),
                    new Expectation(null),
                },
            },
            // (2) Unresolved properties in merged descriptor:
            {
                null, null, "/mta/sample/v1/merged-03.yaml",
                new Expectation[] {
                    new Expectation(Expectation.Type.SKIP, null),
                    new Expectation(Expectation.Type.SKIP, null),
                    new Expectation(Expectation.Type.EXCEPTION, "Unresolved mandatory properties: [competitor-data#test, pricing#internal-odata#test, pricing-db#test, test]"),
                },
            },
            // (3) Unknown module in extension descriptor:
            {
                "/mta/sample/v1/mtad-01.yaml", new String[] { "/mta/sample/v1/config-02.mtaext" }, null,
                new Expectation[] {
                    new Expectation(Expectation.Type.SKIP, null),
                    new Expectation(Expectation.Type.EXCEPTION, "Unknown module \"web-serverx\" in extension descriptor \"com.sap.mta.sample.config-02\""),
                    new Expectation(Expectation.Type.SKIP, null),
                },
            },
            // (4) Unknown provided dependency in extension descriptor:
            {
                "/mta/sample/v1/mtad-01.yaml", new String[] { "/mta/sample/v1/config-03.mtaext" }, null,
                new Expectation[] {
                    new Expectation(Expectation.Type.SKIP, null),
                    new Expectation(Expectation.Type.EXCEPTION, "Unknown provided dependency \"internal-odatax\" for module \"pricing\" in extension descriptor \"com.sap.mta.sample.config-03\""),
                    new Expectation(Expectation.Type.SKIP, null),
                },
            },
            // (5) Unknown resource in extension descriptor:
            {
                "/mta/sample/v1/mtad-01.yaml", new String[] { "/mta/sample/v1/config-04.mtaext" }, null,
                new Expectation[] {
                    new Expectation(null),
                    new Expectation(Expectation.Type.EXCEPTION, "Unknown resource \"internal-odata-servicex\" in extension descriptor \"com.sap.mta.sample.config-04\""),
                    new Expectation(Expectation.Type.SKIP, null),
                },
            },
            // (6) Unsupported resource type in deployment descriptor:
            {
                "/mta/sample/v1/mtad-02.yaml", null, null,
                new Expectation[] {
                    new Expectation(Expectation.Type.EXCEPTION, "Unsupported resource type \"com.sap.hana.hdi-containerx\" for platform type \"CLOUD-FOUNDRY\""),
                    new Expectation(Expectation.Type.SKIP, null),
                    new Expectation(Expectation.Type.SKIP, null),
                },
            },
            // (7) Unresolved required dependency in deployment descriptor:
            {
                "/mta/sample/v1/mtad-03.yaml", null, null,
                new Expectation[] {
                    new Expectation(Expectation.Type.EXCEPTION, "Unresolved required dependency \"internal-odatax\" for module \"web-server\""),
                    new Expectation(Expectation.Type.SKIP, null),
                    new Expectation(Expectation.Type.SKIP, null),
                },
            },
            // (8) Unsupported module type in deployment descriptor:
            {
                "/mta/sample/v1/mtad-04.yaml", null, null,
                new Expectation[] {
                    new Expectation(Expectation.Type.EXCEPTION, "Unsupported module type \"com.sap.static-contentx\" for platform type \"CLOUD-FOUNDRY\""),
                    new Expectation(Expectation.Type.SKIP, null),
                    new Expectation(Expectation.Type.SKIP, null),
                },
            },
            // (9) Merged descriptor contains properties with empty values (but not null):
            {
                null, null, "/mta/sample/v1/merged-06.yaml",
                new Expectation[] {
                    new Expectation(Expectation.Type.SKIP, null),
                    new Expectation(Expectation.Type.SKIP, null),
                    new Expectation(null),
                },
            },
// @formatter:on
        });
    }

    public DescriptorValidatorTest(String deploymentDescriptorLocation, String[] extensionDescriptorLocations,
        String mergedDescriptorLocation, Expectation[] expectations) {
        this.deploymentDescriptorLocation = deploymentDescriptorLocation;
        this.extensionDescriptorLocations = extensionDescriptorLocations;
        this.mergedDescriptorLocation = mergedDescriptorLocation;
        this.expectations = expectations;
    }

    @Before
    public void setUp() throws Exception {
        DescriptorParser descriptorParser = getDescriptorParser();
        if (deploymentDescriptorLocation != null) {
            deploymentDescriptor = MtaTestUtil.loadDeploymentDescriptor(deploymentDescriptorLocation, descriptorParser, getClass());
        }
        if (extensionDescriptorLocations != null) {
            extensionDescriptors = MtaTestUtil.loadExtensionDescriptors(extensionDescriptorLocations, descriptorParser, getClass());
        }
        if (mergedDescriptorLocation != null) {
            mergedDescriptor = MtaTestUtil.loadDeploymentDescriptor(mergedDescriptorLocation, descriptorParser, getClass());
        }

        ConfigurationParser configurationParser = getConfigurationParser();

        platform = MtaTestUtil.loadPlatform(platformLocation, configurationParser, getClass());

        validator = createValidator();
    }

    protected DescriptorParser getDescriptorParser() {
        return new DescriptorParser();
    }

    protected ConfigurationParser getConfigurationParser() {
        return new ConfigurationParser();
    }

    protected DescriptorValidator createValidator() {
        return new DescriptorValidator();
    }

    @Test
    public void testValidateDeploymentDescriptor() throws Exception {
        TestUtil.test(new Runnable() {
            @Override
            public void run() throws Exception {
                validator.validateDeploymentDescriptor(deploymentDescriptor, platform);
            }
        }, expectations[0]);
    }

    @Test
    public void testValidateExtensionDescriptors() throws Exception {
        TestUtil.test(new Runnable() {
            @Override
            public void run() throws Exception {
                validator.validateExtensionDescriptors(extensionDescriptors, deploymentDescriptor);
            }
        }, expectations[1]);

    }

    @Test
    public void testValidateMergedDescriptor() throws Exception {
        TestUtil.test(new Runnable() {
            @Override
            public void run() throws Exception {
                validator.validateMergedDescriptor(mergedDescriptor);
            }
        }, expectations[2]);
    }

}

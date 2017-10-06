package com.sap.cloud.lm.sl.mta.handlers.v1_0;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.sap.cloud.lm.sl.common.util.Pair;
import com.sap.cloud.lm.sl.common.util.Runnable;
import com.sap.cloud.lm.sl.common.util.TestUtil;
import com.sap.cloud.lm.sl.mta.MtaTestUtil;
import com.sap.cloud.lm.sl.mta.model.v1_0.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v1_0.ExtensionDescriptor;
import com.sap.cloud.lm.sl.mta.model.v1_0.Target;
import com.sap.cloud.lm.sl.mta.model.v1_0.Platform;

@RunWith(value = Parameterized.class)
public class DescriptorValidatorTest {

    protected static String targetsLocation;
    protected static String platformsLocation;
    protected static String targetName;

    private final String deploymentDescriptorLocation;
    private final String[] extensionDescriptorLocations;
    private final String mergedDescriptorLocation;
    private final String[] targets;
    private final String[] expected;

    private DeploymentDescriptor deploymentDescriptor;
    private List<ExtensionDescriptor> extensionDescriptors;
    private DeploymentDescriptor mergedDescriptor;
    private Platform platform;
    private Target target;

    private DescriptorValidator validator;

    @BeforeClass
    public static void setPlatformsInformation() {
        targetName = "CF-QUAL";
        platformsLocation = "/mta/sample/v1_0/platforms-01.json";
        targetsLocation = "/mta/sample/v1_0/targets-01.json";
    }

    @Parameters
    public static Iterable<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            // (0) Valid deployment and extension descriptors:
            {
                "/mta/sample/v1_0/mtad-01.yaml", new String[] { "/mta/sample/v1_0/config-01.mtaext" }, null, null, new String[] { "", "", "S", },
            },
            // (1) Deploy Target not listed in merged descriptor (there are other targets listed):
            {
                null, null, "/mta/sample/v1_0/merged-01.yaml", new String[] { "CF-TEST", "CF-PROD", }, new String[] { "S", "S", "E:Deploy target \"CF-QUAL\" not listed in extension descriptor chain", },
            },
            // (2) Deploy target not listed in merged descriptor (there aren't any other targets listed):
            {
                null, null, "/mta/sample/v1_0/merged-01.yaml", new String[] {}, new String[] { "S", "S", "", },
            },
            // (3) Unresolved properties in merged descriptor:
            {
                null, null, "/mta/sample/v1_0/merged-03.yaml", new String[] { "CF-QUAL", }, new String[] { "S", "S", "E:Unresolved mandatory properties: [competitor-data#test, pricing#internal-odata#test, pricing-db#test, test]", },
            },
            // (4) Unknown module in extension descriptor:
            {
                "/mta/sample/v1_0/mtad-01.yaml", new String[] { "/mta/sample/v1_0/config-02.mtaext" }, null, null, new String[] { "S", "E:Unknown module \"web-serverx\" in extension descriptor \"com.sap.mta.sample.config-02\"", "S", },
            },
            // (5) Unknown provided dependency in extension descriptor:
            {
                "/mta/sample/v1_0/mtad-01.yaml", new String[] { "/mta/sample/v1_0/config-03.mtaext" }, null, null, new String[] { "S", "E:Unknown provided dependency \"internal-odatax\" for module \"pricing\" in extension descriptor \"com.sap.mta.sample.config-03\"", "S", },
            },
            // (6) Unknown resource in extension descriptor:
            {
                "/mta/sample/v1_0/mtad-01.yaml", new String[] { "/mta/sample/v1_0/config-04.mtaext" }, null, null, new String[] { "", "E:Unknown resource \"internal-odata-servicex\" in extension descriptor \"com.sap.mta.sample.config-04\"", "S", },
            },
            // (7) Unsupported resource type in deployment descriptor:
            {
                "/mta/sample/v1_0/mtad-02.yaml", null, null, null, new String[] { "E:Unsupported resource type \"com.sap.hana.hdi-containerx\" for platform type \"CLOUD-FOUNDRY\"", "S", "S", },
            },
            // (8) Unresolved required dependency in deployment descriptor:
            {
                "/mta/sample/v1_0/mtad-03.yaml", null, null, null, new String[] { "E:Unresolved required dependency \"internal-odatax\" for module \"web-server\"", "S", "S", },
            },
            // (9) Unsupported module type in deployment descriptor:
            {
                "/mta/sample/v1_0/mtad-04.yaml", null, null, null, new String[] { "E:Unsupported module type \"com.sap.static-contentx\" for platform type \"CLOUD-FOUNDRY\"", "S", "S", },
            },
            // (10) Merged descriptor contains properties with empty values (but not null):
            {
                null, null, "/mta/sample/v1_0/merged-06.yaml", new String[] {}, new String[] { "S", "S", "" },
            },
// @formatter:on
        });
    }

    public DescriptorValidatorTest(String deploymentDescriptorLocation, String[] extensionDescriptorLocations,
        String mergedDescriptorLocation, String[] targets, String[] expected) {
        this.deploymentDescriptorLocation = deploymentDescriptorLocation;
        this.extensionDescriptorLocations = extensionDescriptorLocations;
        this.mergedDescriptorLocation = mergedDescriptorLocation;
        this.targets = targets;
        this.expected = expected;
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

        DescriptorHandler handler = new DescriptorHandler();

        target = handler.findTarget(MtaTestUtil.loadTargets(targetsLocation, configurationParser, getClass()), targetName);

        platform = handler.findPlatform(MtaTestUtil.loadPlatforms(platformsLocation, configurationParser, getClass()),
            target.getType());

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
        }, expected[0]);
    }

    @Test
    public void testValidateExtensionDescriptors() throws Exception {
        TestUtil.test(new Runnable() {
            @Override
            public void run() throws Exception {
                validator.validateExtensionDescriptors(extensionDescriptors, deploymentDescriptor);
            }
        }, expected[1]);

    }

    @Test
    public void testValidateMergedDescriptor() throws Exception {
        TestUtil.test(new Runnable() {
            @Override
            public void run() throws Exception {
                validator.validateMergedDescriptor(new Pair<DeploymentDescriptor, List<String>>(mergedDescriptor, Arrays.asList(targets)),
                    target);
            }
        }, expected[2]);
    }


}

package com.sap.cloud.lm.sl.mta.builders.v1_0;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.sap.cloud.lm.sl.common.util.Callable;
import com.sap.cloud.lm.sl.common.util.TestUtil;
import com.sap.cloud.lm.sl.mta.MtaTestUtil;
import com.sap.cloud.lm.sl.mta.handlers.v1_0.ConfigurationParser;
import com.sap.cloud.lm.sl.mta.handlers.v1_0.DescriptorParser;
import com.sap.cloud.lm.sl.mta.model.v1_0.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v1_0.Module;
import com.sap.cloud.lm.sl.mta.model.v1_0.Platform;
import com.sap.cloud.lm.sl.mta.model.v1_0.Resource;
import com.sap.cloud.lm.sl.mta.model.v1_0.Target;

@RunWith(Parameterized.class)
public class PropertiesChainBuilderTest {

    protected final String deploymentDescriptorLocation;
    protected final String targetLocation;
    protected final String platformLocation;
    protected final String[] expected;

    protected PropertiesChainBuilder builder;
    protected List<String> moduleNames;
    protected List<String> resourceNames;

    @Parameters
    public static Iterable<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            // (0) All module and resource types are present in platform and platform type:
            {
                "mtad-01.yaml", "platform-01.json", "target-01.json", new String[] { "R:module-chain-01.json", "R:module-chain-without-dependencies-01.json", "R:resource-chain-01.json", "R:resource-type-chain-01.json", },
            },
            // (1) No module and resource types in platform and platform type:
            {
                "mtad-01.yaml", "platform-02.json", "target-02.json", new String[] { "R:module-chain-02.json", "R:module-chain-without-dependencies-02.json", "R:resource-chain-02.json", "R:resource-type-chain-02.json", },
            },
            // (2) Some module and resource types are present in platform and platform type:
            {
                "mtad-01.yaml", "platform-03.json", "target-03.json", new String[] { "R:module-chain-03.json", "R:module-chain-without-dependencies-03.json", "R:resource-chain-03.json", "R:resource-type-chain-03.json", },
            },
            // (3) No platform and platform type:
            {
                "mtad-01.yaml", null, null, new String[] { "R:module-chain-04.json", "R:module-chain-without-dependencies-04.json", "R:resource-chain-04.json", "R:resource-type-chain-04.json", },
            },
// @formatter:on
        });
    }

    public PropertiesChainBuilderTest(String deploymentDescriptorLocation, String platformTypeLocation, String platformLocation,
        String[] expected) {
        this.deploymentDescriptorLocation = deploymentDescriptorLocation;
        this.targetLocation = platformLocation;
        this.platformLocation = platformTypeLocation;
        this.expected = expected;
    }

    @Before
    public void setUp() throws Exception {
        DescriptorParser descriptorParser = getDescriptorParser();

        DeploymentDescriptor deploymentDescriptor = MtaTestUtil.loadDeploymentDescriptor(deploymentDescriptorLocation, descriptorParser,
            getClass());
        resourceNames = getResourceNames(deploymentDescriptor.getResources1_0());
        moduleNames = getModuleNames(deploymentDescriptor.getModules1_0());

        ConfigurationParser configurationParser = getConfigurationParser();

        Target target = null;
        if (targetLocation != null) {
            target = MtaTestUtil.loadTargets(targetLocation, configurationParser, getClass())
                .get(0);
        }

        Platform platform = null;
        if (platformLocation != null) {
            platform = MtaTestUtil.loadPlatforms(platformLocation, configurationParser, getClass())
                .get(0);
        }

        builder = createPropertiesChainBuilder(deploymentDescriptor, platform, target);
    }

    protected ConfigurationParser getConfigurationParser() {
        return new ConfigurationParser();
    }

    protected DescriptorParser getDescriptorParser() {
        return new DescriptorParser();
    }

    private List<String> getResourceNames(List<Resource> resources) {
        List<String> resourceNames = new ArrayList<String>();
        for (Resource resource : resources) {
            resourceNames.add(resource.getName());
        }
        return resourceNames;
    }

    private List<String> getModuleNames(List<Module> modules) {
        List<String> moduleNames = new ArrayList<String>();
        for (Module module : modules) {
            moduleNames.add(module.getName());
        }
        return moduleNames;
    }

    protected PropertiesChainBuilder createPropertiesChainBuilder(DeploymentDescriptor deploymentDescriptor, Platform platform,
        Target target) {
        return new PropertiesChainBuilder(deploymentDescriptor, target, platform);
    }

    @Test
    public void testBuildModuleChain() {
        TestUtil.test(new Callable<List<List<Map<String, Object>>>>() {
            @Override
            public List<List<Map<String, Object>>> call() throws Exception {
                List<List<Map<String, Object>>> moduleChains = new ArrayList<List<Map<String, Object>>>();
                for (String moduleName : moduleNames) {
                    moduleChains.add(builder.buildModuleChain(moduleName));
                }
                return moduleChains;
            }
        }, expected[0], getClass());
    }

    @Test
    public void testBuildModuleChainWithoutDependencies() {
        TestUtil.test(new Callable<List<List<Map<String, Object>>>>() {
            @Override
            public List<List<Map<String, Object>>> call() throws Exception {
                List<List<Map<String, Object>>> moduleChains = new ArrayList<List<Map<String, Object>>>();
                for (String moduleName : moduleNames) {
                    moduleChains.add(builder.buildModuleChainWithoutDependencies(moduleName));
                }
                return moduleChains;
            }
        }, expected[1], getClass());
    }

    @Test
    public void testBuildResourceChain() {
        TestUtil.test(new Callable<List<List<Map<String, Object>>>>() {
            @Override
            public List<List<Map<String, Object>>> call() throws Exception {
                List<List<Map<String, Object>>> resourceChains = new ArrayList<List<Map<String, Object>>>();
                for (String resourceName : resourceNames) {
                    resourceChains.add(builder.buildResourceChain(resourceName));
                }
                return resourceChains;
            }
        }, expected[2], getClass());
    }

    @Test
    public void testBuildResourceTypeChain() {
        TestUtil.test(new Callable<List<List<Map<String, Object>>>>() {
            @Override
            public List<List<Map<String, Object>>> call() throws Exception {
                List<List<Map<String, Object>>> resourceTypeChains = new ArrayList<List<Map<String, Object>>>();
                for (String resourceName : resourceNames) {
                    resourceTypeChains.add(builder.buildResourceTypeChain(resourceName));
                }
                return resourceTypeChains;
            }
        }, expected[3], getClass());
    }

}

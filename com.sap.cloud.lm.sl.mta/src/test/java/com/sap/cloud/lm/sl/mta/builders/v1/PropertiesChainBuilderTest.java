package com.sap.cloud.lm.sl.mta.builders.v1;

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
import com.sap.cloud.lm.sl.common.util.TestUtil.Expectation;
import com.sap.cloud.lm.sl.mta.MtaTestUtil;
import com.sap.cloud.lm.sl.mta.handlers.v1.ConfigurationParser;
import com.sap.cloud.lm.sl.mta.handlers.v1.DescriptorParser;
import com.sap.cloud.lm.sl.mta.model.v1.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v1.Module;
import com.sap.cloud.lm.sl.mta.model.v1.Platform;
import com.sap.cloud.lm.sl.mta.model.v1.Resource;

@RunWith(Parameterized.class)
public class PropertiesChainBuilderTest {

    protected final String deploymentDescriptorLocation;
    protected final String platformLocation;
    protected final Expectation[] expectations;

    protected PropertiesChainBuilder builder;
    protected List<String> moduleNames;
    protected List<String> resourceNames;

    @Parameters
    public static Iterable<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
// @formatter:off
            // (0) All module and resource types are present in the platform:
            {
                "mtad-01.yaml", "platform-01.json",
                new Expectation[] {
                    new Expectation(Expectation.Type.RESOURCE, "module-chain-01.json"),
                    new Expectation(Expectation.Type.RESOURCE, "module-chain-without-dependencies-01.json"),
                    new Expectation(Expectation.Type.RESOURCE, "resource-chain-01.json"),
                    new Expectation(Expectation.Type.RESOURCE, "resource-type-chain-01.json"),
                },
            },
            // (1) No module and resource types in the platform:
            {
                "mtad-01.yaml", "platform-02.json",
                new Expectation[] {
                    new Expectation(Expectation.Type.RESOURCE, "module-chain-02.json"),
                    new Expectation(Expectation.Type.RESOURCE, "module-chain-without-dependencies-02.json"),
                    new Expectation(Expectation.Type.RESOURCE, "resource-chain-02.json"),
                    new Expectation(Expectation.Type.RESOURCE, "resource-type-chain-02.json"),
                },
            },
            // (2) Some module and resource types are present in the platform:
            {
                "mtad-01.yaml", "platform-03.json",
                new Expectation[] {
                    new Expectation(Expectation.Type.RESOURCE, "module-chain-03.json"),
                    new Expectation(Expectation.Type.RESOURCE, "module-chain-without-dependencies-03.json"),
                    new Expectation(Expectation.Type.RESOURCE, "resource-chain-03.json"),
                    new Expectation(Expectation.Type.RESOURCE, "resource-type-chain-03.json"),
                },
            },
            // (3) No platform:
            {
                "mtad-01.yaml", null,
                new Expectation[] {
                    new Expectation(Expectation.Type.RESOURCE, "module-chain-04.json"),
                    new Expectation(Expectation.Type.RESOURCE, "module-chain-without-dependencies-04.json"),
                    new Expectation(Expectation.Type.RESOURCE, "resource-chain-04.json"),
                    new Expectation(Expectation.Type.RESOURCE, "resource-type-chain-04.json"),
                },
            },
// @formatter:on
        });
    }

    public PropertiesChainBuilderTest(String deploymentDescriptorLocation, String platformLocation, Expectation[] expectations) {
        this.deploymentDescriptorLocation = deploymentDescriptorLocation;
        this.platformLocation = platformLocation;
        this.expectations = expectations;
    }

    @Before
    public void setUp() throws Exception {
        DescriptorParser descriptorParser = getDescriptorParser();

        DeploymentDescriptor deploymentDescriptor = MtaTestUtil.loadDeploymentDescriptor(deploymentDescriptorLocation, descriptorParser,
            getClass());
        resourceNames = getResourceNames(deploymentDescriptor.getResources1());
        moduleNames = getModuleNames(deploymentDescriptor.getModules1());

        ConfigurationParser configurationParser = getConfigurationParser();

        Platform platform = null;
        if (platformLocation != null) {
            platform = MtaTestUtil.loadPlatform(platformLocation, configurationParser, getClass());
        }

        builder = createPropertiesChainBuilder(deploymentDescriptor, platform);
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

    protected PropertiesChainBuilder createPropertiesChainBuilder(DeploymentDescriptor deploymentDescriptor, Platform platform) {
        return new PropertiesChainBuilder(deploymentDescriptor, platform);
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
        }, expectations[0], getClass());
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
        }, expectations[1], getClass());
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
        }, expectations[2], getClass());
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
        }, expectations[3], getClass());
    }

}

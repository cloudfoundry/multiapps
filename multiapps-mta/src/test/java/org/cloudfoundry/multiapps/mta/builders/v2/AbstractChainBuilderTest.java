package org.cloudfoundry.multiapps.mta.builders.v2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.cloudfoundry.multiapps.common.test.Tester;
import org.cloudfoundry.multiapps.mta.MtaTestUtil;
import org.cloudfoundry.multiapps.mta.handlers.v2.DescriptorParser;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.Module;
import org.cloudfoundry.multiapps.mta.model.Platform;
import org.cloudfoundry.multiapps.mta.model.Resource;

public class AbstractChainBuilderTest {

    protected DeploymentDescriptor parseDeploymentDescriptor(String deploymentDescriptorLocation) {
        return MtaTestUtil.loadDeploymentDescriptor(deploymentDescriptorLocation, createDescriptorParser(), getClass());
    }

    protected Platform parsePlatform(String platformLocation) {
        return MtaTestUtil.loadPlatform(platformLocation, getClass());
    }

    protected List<String> getResourceNames(DeploymentDescriptor deploymentDescriptor) {
        List<String> resourceNames = new ArrayList<>();
        for (Resource resource : deploymentDescriptor.getResources()) {
            resourceNames.add(resource.getName());
        }
        return resourceNames;
    }

    protected List<String> getModuleNames(DeploymentDescriptor deploymentDescriptor) {
        List<String> moduleNames = new ArrayList<>();
        for (Module module : deploymentDescriptor.getModules()) {
            moduleNames.add(module.getName());
        }
        return moduleNames;
    }

    protected PropertiesChainBuilder createPropertiesChainBuilder(DeploymentDescriptor deploymentDescriptor, Platform platform) {
        return new PropertiesChainBuilder(deploymentDescriptor, platform);
    }

    protected DescriptorParser createDescriptorParser() {
        return new DescriptorParser();
    }


    void executeTestBuildModuleChain(Tester tester, String deploymentDescriptorLocation, String platformLocation, Tester.Expectation expectation) {
        DeploymentDescriptor deploymentDescriptor = parseDeploymentDescriptor(deploymentDescriptorLocation);
        Platform platform = parsePlatform(platformLocation);

        PropertiesChainBuilder builder = createPropertiesChainBuilder(deploymentDescriptor, platform);
        tester.test(() -> {
            List<List<Map<String, Object>>> moduleChains = new ArrayList<>();
            for (String moduleName : getModuleNames(deploymentDescriptor)) {
                moduleChains.add(builder.buildModuleChain(moduleName));
            }
            return moduleChains;
        }, expectation);
    }

    void executeTestBuildModuleChainWithoutDependencies(Tester tester, String deploymentDescriptorLocation, String platformLocation, Tester.Expectation expectation) {
        DeploymentDescriptor deploymentDescriptor = parseDeploymentDescriptor(deploymentDescriptorLocation);
        Platform platform = parsePlatform(platformLocation);

        PropertiesChainBuilder builder = createPropertiesChainBuilder(deploymentDescriptor, platform);
        tester.test(() -> {
            List<List<Map<String, Object>>> moduleChains = new ArrayList<>();
            for (String moduleName : getModuleNames(deploymentDescriptor)) {
                moduleChains.add(builder.buildModuleChainWithoutDependencies(moduleName));
            }
            return moduleChains;
        }, expectation);
    }

    void executeTestBuildResourceChain(Tester tester, String deploymentDescriptorLocation, String platformLocation, Tester.Expectation expectation) {
        DeploymentDescriptor deploymentDescriptor = parseDeploymentDescriptor(deploymentDescriptorLocation);
        Platform platform = parsePlatform(platformLocation);

        PropertiesChainBuilder builder = createPropertiesChainBuilder(deploymentDescriptor, platform);
        tester.test(() -> {
            List<List<Map<String, Object>>> resourceChains = new ArrayList<>();
            for (String resourceName : getResourceNames(deploymentDescriptor)) {
                resourceChains.add(builder.buildResourceChain(resourceName));
            }
            return resourceChains;
        }, expectation);
    }

}

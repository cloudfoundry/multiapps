package com.sap.cloud.lm.sl.mta.builders.v2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;

import com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.model.ParametersContainer;
import com.sap.cloud.lm.sl.mta.model.v2.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v2.Module;
import com.sap.cloud.lm.sl.mta.model.v2.Platform;
import com.sap.cloud.lm.sl.mta.model.v2.PlatformModuleType;
import com.sap.cloud.lm.sl.mta.model.v2.RequiredDependency;
import com.sap.cloud.lm.sl.mta.model.v2.Resource;
import com.sap.cloud.lm.sl.mta.util.PropertiesUtil;

public class ParametersChainBuilder extends PropertiesChainBuilder {

    public ParametersChainBuilder(DeploymentDescriptor descriptor, Platform platform) {
        super(descriptor, platform, new DescriptorHandler());
    }

    public ParametersChainBuilder(DeploymentDescriptor descriptor) {
        super(descriptor, null, new DescriptorHandler());
    }

    public ParametersChainBuilder(DeploymentDescriptor descriptor, Platform platform, DescriptorHandler handler) {
        super(descriptor, platform, handler);
    }

    @Override
    public List<Map<String, Object>> buildModuleChain(String moduleName) {
        Module module = (Module) handler.findModule(descriptor, moduleName);
        if (module == null) {
            return Collections.emptyList();
        }
        List<RequiredDependency> dependencies = module.getRequiredDependencies2();
        PlatformModuleType platformModuleType = (PlatformModuleType) getPlatformModuleType(module);
        DeploymentDescriptor deploymentDescriptor = (com.sap.cloud.lm.sl.mta.model.v2.DeploymentDescriptor) descriptor;
        return getParametersList(dependencies, module, platformModuleType, deploymentDescriptor);
    }

    @Override
    public List<Map<String, Object>> buildModuleChainWithoutDependencies(String moduleName) {
        Module module = (Module) handler.findModule(descriptor, moduleName);
        if (module == null) {
            return Collections.emptyList();
        }
        PlatformModuleType platformModuleType = (PlatformModuleType) getPlatformModuleType(module);
        DeploymentDescriptor deploymentDescriptor = (DeploymentDescriptor) descriptor;
        return PropertiesUtil.getParametersList(module, platformModuleType, deploymentDescriptor);
    }

    @Override
    public List<Map<String, Object>> buildResourceChain(String resourceName) {
        Resource resource = (Resource) handler.findResource(descriptor, resourceName);
        DeploymentDescriptor deploymentDescriptor = (DeploymentDescriptor) descriptor;
        if (resource == null) {
            return Collections.emptyList();
        }
        List<Map<String, Object>> resourceTypeChain = Collections.emptyList();
        if (resource != null && resource.getType() != null) {
            resourceTypeChain = buildResourceTypeChain(resourceName);
        }
        List<Map<String, Object>> resourceChain = PropertiesUtil.getParametersList(resource, deploymentDescriptor);
        resourceChain.addAll(resourceTypeChain);
        return resourceChain;
    }

    @Override
    public List<Map<String, Object>> buildResourceTypeChain(String resourceName) {
        Resource resource = (Resource) handler.findResource(descriptor, resourceName);
        if (resource == null) {
            return Collections.emptyList();
        }
        ParametersContainer platformResourceType = (ParametersContainer) getPlatformResourceType(resource);
        DeploymentDescriptor deploymentDescriptor = (DeploymentDescriptor) descriptor;
        return PropertiesUtil.getParametersList(platformResourceType, deploymentDescriptor);
    }

    protected static List<Map<String, Object>> getParametersList(List<RequiredDependency> dependencies, Module module,
        PlatformModuleType platformModuleType, DeploymentDescriptor descriptor) {
        List<ParametersContainer> containers = new ArrayList<>();
        containers.addAll(dependencies);
        CollectionUtils.addIgnoreNull(containers, module);
        CollectionUtils.addIgnoreNull(containers, platformModuleType);
        CollectionUtils.addIgnoreNull(containers, descriptor);
        return PropertiesUtil.getParametersList(containers);
    }

}

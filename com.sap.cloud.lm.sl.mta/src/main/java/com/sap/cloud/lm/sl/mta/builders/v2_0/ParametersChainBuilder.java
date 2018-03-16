package com.sap.cloud.lm.sl.mta.builders.v2_0;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.handlers.v2_0.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.model.ParametersContainer;
import com.sap.cloud.lm.sl.mta.model.v2_0.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v2_0.Module;
import com.sap.cloud.lm.sl.mta.model.v2_0.ModuleType;
import com.sap.cloud.lm.sl.mta.model.v2_0.Platform;
import com.sap.cloud.lm.sl.mta.model.v2_0.PlatformModuleType;
import com.sap.cloud.lm.sl.mta.model.v2_0.PlatformResourceType;
import com.sap.cloud.lm.sl.mta.model.v2_0.RequiredDependency;
import com.sap.cloud.lm.sl.mta.model.v2_0.Resource;
import com.sap.cloud.lm.sl.mta.model.v2_0.ResourceType;
import com.sap.cloud.lm.sl.mta.model.v2_0.Target;
import com.sap.cloud.lm.sl.mta.util.PropertiesUtil;

public class ParametersChainBuilder extends PropertiesChainBuilder {

    public ParametersChainBuilder(DeploymentDescriptor descriptor, Target target, Platform platform) {
        super(descriptor, target, platform, new DescriptorHandler());
    }

    public ParametersChainBuilder(DeploymentDescriptor descriptor) {
        super(descriptor, null, null, new DescriptorHandler());
    }

    public ParametersChainBuilder(DeploymentDescriptor descriptor, Target target, Platform platform, DescriptorHandler handler) {
        super(descriptor, target, platform, handler);
    }

    @Override
    public List<Map<String, Object>> buildModuleChain(String moduleName) {
        Module module = (Module) handler.findModule(descriptor, moduleName);
        if (module == null) {
            return Collections.emptyList();
        }
        PlatformModuleType platformModuleType = (PlatformModuleType) getPlatformModuleType(module);
        List<RequiredDependency> dependencies = module.getRequiredDependencies2_0();
        ModuleType moduleType = (ModuleType) getModuleType(module);
        DeploymentDescriptor deploymentDescriptor = (com.sap.cloud.lm.sl.mta.model.v2_0.DeploymentDescriptor) descriptor;
        return getParametersList(module, moduleType, platformModuleType, dependencies, deploymentDescriptor);
    }

    @Override
    public List<Map<String, Object>> buildModuleChainWithoutDependencies(String moduleName) {
        Module module = (Module) handler.findModule(descriptor, moduleName);
        if (module == null) {
            return Collections.emptyList();
        }
        ModuleType moduleType = (ModuleType) getModuleType(module);
        PlatformModuleType platformModuleType = (PlatformModuleType) getPlatformModuleType(module);
        DeploymentDescriptor deploymentDescriptor = (DeploymentDescriptor) descriptor;
        return PropertiesUtil.getParametersList(module, platformModuleType, moduleType, deploymentDescriptor);
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
        ResourceType resourceType = (ResourceType) getResourceType(resource);
        PlatformResourceType platformResourceType = (PlatformResourceType) getPlatformResourceType(resource);
        DeploymentDescriptor deploymentDescriptor = (DeploymentDescriptor) descriptor;
        return PropertiesUtil.getParametersList(platformResourceType, resourceType, deploymentDescriptor);
    }

    protected static List<Map<String, Object>> getParametersList(Module module, ModuleType moduleType,
        PlatformModuleType platformModuleType, List<RequiredDependency> dependencies, DeploymentDescriptor descriptor) {
        List<ParametersContainer> containers = new ArrayList<ParametersContainer>();
        containers.addAll(dependencies);
        ListUtil.addNonNull(containers, module);
        ListUtil.addNonNull(containers, platformModuleType);
        ListUtil.addNonNull(containers, moduleType);
        ListUtil.addNonNull(containers, descriptor);
        return PropertiesUtil.getParametersList(containers);
    }

}

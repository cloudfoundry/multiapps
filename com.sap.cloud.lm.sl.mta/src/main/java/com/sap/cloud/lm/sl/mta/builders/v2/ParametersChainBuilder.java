package com.sap.cloud.lm.sl.mta.builders.v2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;

import com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.model.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.Module;
import com.sap.cloud.lm.sl.mta.model.ModuleType;
import com.sap.cloud.lm.sl.mta.model.ParametersContainer;
import com.sap.cloud.lm.sl.mta.model.Platform;
import com.sap.cloud.lm.sl.mta.model.RequiredDependency;
import com.sap.cloud.lm.sl.mta.model.Resource;
import com.sap.cloud.lm.sl.mta.model.ResourceType;
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
        Module module = handler.findModule(descriptor, moduleName);
        if (module == null) {
            return Collections.emptyList();
        }
        List<RequiredDependency> dependencies = module.getRequiredDependencies();
        ModuleType moduleType = getModuleType(module);
        return getParametersList(dependencies, module, moduleType, descriptor);
    }

    @Override
    public List<Map<String, Object>> buildModuleChainWithoutDependencies(String moduleName) {
        Module module = handler.findModule(descriptor, moduleName);
        if (module == null) {
            return Collections.emptyList();
        }
        ModuleType moduleType = getModuleType(module);
        return PropertiesUtil.getParametersList(module, moduleType, descriptor);
    }

    @Override
    public List<Map<String, Object>> buildResourceChain(String resourceName) {
        Resource resource = handler.findResource(descriptor, resourceName);
        if (resource == null) {
            return Collections.emptyList();
        }
        List<Map<String, Object>> resourceTypeChain = Collections.emptyList();
        if (resource != null && resource.getType() != null) {
            resourceTypeChain = buildResourceTypeChain(resourceName);
        }
        List<Map<String, Object>> resourceChain = PropertiesUtil.getParametersList(resource, descriptor);
        resourceChain.addAll(resourceTypeChain);
        return resourceChain;
    }

    @Override
    public List<Map<String, Object>> buildResourceTypeChain(String resourceName) {
        Resource resource = handler.findResource(descriptor, resourceName);
        if (resource == null) {
            return Collections.emptyList();
        }
        ParametersContainer resourceType = getResourceType(resource);
        return PropertiesUtil.getParametersList(resourceType, descriptor);
    }

    protected static List<Map<String, Object>> getParametersList(List<RequiredDependency> dependencies, Module module,
        ModuleType moduleType, DeploymentDescriptor descriptor) {
        List<ParametersContainer> containers = new ArrayList<>();
        containers.addAll(dependencies);
        CollectionUtils.addIgnoreNull(containers, module);
        CollectionUtils.addIgnoreNull(containers, moduleType);
        CollectionUtils.addIgnoreNull(containers, descriptor);
        return PropertiesUtil.getParametersList(containers);
    }

    protected ResourceType getResourceType(Resource resource) {
        if (platform == null) {
            return null;
        }
        return handler.findResourceType(platform, resource.getType());
    }

}

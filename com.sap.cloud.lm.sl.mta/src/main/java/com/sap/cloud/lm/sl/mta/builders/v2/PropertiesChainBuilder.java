package com.sap.cloud.lm.sl.mta.builders.v2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;

import com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.model.PropertiesContainer;
import com.sap.cloud.lm.sl.mta.model.v2.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v2.Module;
import com.sap.cloud.lm.sl.mta.model.v2.Platform;
import com.sap.cloud.lm.sl.mta.model.v2.PlatformModuleType;
import com.sap.cloud.lm.sl.mta.model.v2.RequiredDependency;
import com.sap.cloud.lm.sl.mta.model.v2.Resource;
import com.sap.cloud.lm.sl.mta.model.v2.Target;
import com.sap.cloud.lm.sl.mta.model.v2.TargetModuleType;
import com.sap.cloud.lm.sl.mta.util.PropertiesUtil;

public class PropertiesChainBuilder extends com.sap.cloud.lm.sl.mta.builders.v1.PropertiesChainBuilder {

    public PropertiesChainBuilder(DeploymentDescriptor descriptor, Target target, Platform platform) {
        super(descriptor, target, platform, new DescriptorHandler());
    }

    public PropertiesChainBuilder(DeploymentDescriptor descriptor) {
        super(descriptor, null, null, new DescriptorHandler());
    }

    public PropertiesChainBuilder(DeploymentDescriptor descriptor, Target target, Platform platform, DescriptorHandler handler) {
        super(descriptor, target, platform, handler);
    }

    @Override
    public List<Map<String, Object>> buildModuleChain(String moduleName) {
        Module module = (Module) handler.findModule(descriptor, moduleName);
        if (module == null) {
            return Collections.emptyList();
        }
        List<RequiredDependency> dependencies = module.getRequiredDependencies2();
        TargetModuleType targetModuleType = (TargetModuleType) getTargetModuleType(module);
        PlatformModuleType platformModuleType = (PlatformModuleType) getPlatformModuleType(module);
        return getPropertiesList(dependencies, module, targetModuleType, platformModuleType);
    }

    @Override
    public List<Map<String, Object>> buildModuleChainWithoutDependencies(String moduleName) {
        Module module = (Module) handler.findModule(descriptor, moduleName);
        if (module == null) {
            return Collections.emptyList();
        }
        TargetModuleType targetModuleType = (TargetModuleType) getTargetModuleType(module);
        PlatformModuleType platformModuleType = (PlatformModuleType) getPlatformModuleType(module);
        return PropertiesUtil.getPropertiesList(module, targetModuleType, platformModuleType);
    }

    @Override
    public List<Map<String, Object>> buildResourceChain(String resourceName) {
        Resource resource = (Resource) handler.findResource(descriptor, resourceName);
        if (resource == null) {
            return Collections.emptyList();
        }
        return PropertiesUtil.getPropertiesList(resource);
    }

    @Override
    public List<Map<String, Object>> buildResourceTypeChain(String resourceName) {
        throw new UnsupportedOperationException();
    }

    protected static List<Map<String, Object>> getPropertiesList(List<RequiredDependency> dependencies, Module module,
        TargetModuleType targetModuleType, PlatformModuleType platformModuleType) {
        List<PropertiesContainer> containers = new ArrayList<>();
        containers.addAll(dependencies);
        CollectionUtils.addIgnoreNull(containers, module);
        CollectionUtils.addIgnoreNull(containers, targetModuleType);
        CollectionUtils.addIgnoreNull(containers, platformModuleType);
        return PropertiesUtil.getPropertiesList(containers);
    }

}

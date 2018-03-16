package com.sap.cloud.lm.sl.mta.builders.v2_0;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.handlers.v2_0.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.model.PropertiesContainer;
import com.sap.cloud.lm.sl.mta.model.v2_0.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v2_0.Module;
import com.sap.cloud.lm.sl.mta.model.v2_0.ModuleType;
import com.sap.cloud.lm.sl.mta.model.v2_0.Platform;
import com.sap.cloud.lm.sl.mta.model.v2_0.PlatformModuleType;
import com.sap.cloud.lm.sl.mta.model.v2_0.RequiredDependency;
import com.sap.cloud.lm.sl.mta.model.v2_0.Resource;
import com.sap.cloud.lm.sl.mta.model.v2_0.Target;
import com.sap.cloud.lm.sl.mta.util.PropertiesUtil;

public class PropertiesChainBuilder extends com.sap.cloud.lm.sl.mta.builders.v1_0.PropertiesChainBuilder {

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
        ModuleType moduleType = (ModuleType) getModuleType(module);
        List<RequiredDependency> dependencies = module.getRequiredDependencies2_0();
        PlatformModuleType platformModuleType = (PlatformModuleType) getPlatformModuleType(module);
        return getPropertiesList(module, moduleType, platformModuleType, dependencies);
    }

    @Override
    public List<Map<String, Object>> buildModuleChainWithoutDependencies(String moduleName) {
        Module module = (Module) handler.findModule(descriptor, moduleName);
        if (module == null) {
            return Collections.emptyList();
        }
        ModuleType moduleType = (ModuleType) getModuleType(module);
        PlatformModuleType platformModuleType = (PlatformModuleType) getPlatformModuleType(module);
        return PropertiesUtil.getPropertiesList(module, platformModuleType, moduleType);
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

    protected static List<Map<String, Object>> getPropertiesList(Module module, ModuleType moduleType,
        PlatformModuleType platformModuleType, List<RequiredDependency> dependencies) {
        List<PropertiesContainer> containers = new ArrayList<PropertiesContainer>();
        containers.addAll(dependencies);
        ListUtil.addNonNull(containers, module);
        ListUtil.addNonNull(containers, platformModuleType);
        ListUtil.addNonNull(containers, moduleType);
        return PropertiesUtil.getPropertiesList(containers);
    }

}

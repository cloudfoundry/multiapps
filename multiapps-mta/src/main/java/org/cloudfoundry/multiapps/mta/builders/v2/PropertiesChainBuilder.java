package org.cloudfoundry.multiapps.mta.builders.v2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.cloudfoundry.multiapps.mta.handlers.v2.DescriptorHandler;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.Module;
import org.cloudfoundry.multiapps.mta.model.ModuleType;
import org.cloudfoundry.multiapps.mta.model.Platform;
import org.cloudfoundry.multiapps.mta.model.PropertiesContainer;
import org.cloudfoundry.multiapps.mta.model.RequiredDependency;
import org.cloudfoundry.multiapps.mta.model.Resource;
import org.cloudfoundry.multiapps.mta.util.PropertiesUtil;

public class PropertiesChainBuilder {

    protected final DeploymentDescriptor descriptor;
    protected final Platform platform;
    protected final DescriptorHandler handler;

    public PropertiesChainBuilder(DeploymentDescriptor descriptor, Platform platform, DescriptorHandler handler) {
        this.descriptor = descriptor;
        this.platform = platform;
        this.handler = handler;
    }

    public PropertiesChainBuilder(DeploymentDescriptor descriptor, Platform platform) {
        this(descriptor, platform, new DescriptorHandler());
    }

    public PropertiesChainBuilder(DeploymentDescriptor descriptor) {

        this(descriptor, null, new DescriptorHandler());
    }

    public List<Map<String, Object>> buildModuleChain(String moduleName) {
        Module module = handler.findModule(descriptor, moduleName);
        if (module == null) {
            return Collections.emptyList();
        }
        List<RequiredDependency> dependencies = module.getRequiredDependencies();
        ModuleType moduleType = getModuleType(module);
        return getPropertiesList(dependencies, module, moduleType);
    }

    protected ModuleType getModuleType(Module module) {
        if (platform == null) {
            return null;
        }
        return handler.findModuleType(platform, module.getType());
    }

    public List<Map<String, Object>> buildModuleChainWithoutDependencies(String moduleName) {
        Module module = handler.findModule(descriptor, moduleName);
        if (module == null) {
            return Collections.emptyList();
        }
        ModuleType moduleType = getModuleType(module);
        return PropertiesUtil.getPropertiesList(module, moduleType);
    }

    public List<Map<String, Object>> buildResourceTypeChain(String resourceName) {
        throw new UnsupportedOperationException();
    }

    public List<Map<String, Object>> buildResourceChain(String resourceName) {
        Resource resource = handler.findResource(descriptor, resourceName);
        if (resource == null) {
            return Collections.emptyList();
        }
        return PropertiesUtil.getPropertiesList(resource);
    }

    protected static List<Map<String, Object>> getPropertiesList(List<RequiredDependency> dependencies, Module module,
                                                                 ModuleType moduleType) {
        List<PropertiesContainer> containers = new ArrayList<>(dependencies);
        CollectionUtils.addIgnoreNull(containers, module);
        CollectionUtils.addIgnoreNull(containers, moduleType);
        return PropertiesUtil.getPropertiesList(containers);
    }

}

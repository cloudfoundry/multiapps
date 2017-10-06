package com.sap.cloud.lm.sl.mta.builders.v1_0;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.common.util.Pair;
import com.sap.cloud.lm.sl.mta.handlers.v1_0.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.model.PropertiesContainer;
import com.sap.cloud.lm.sl.mta.model.v1_0.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v1_0.Module;
import com.sap.cloud.lm.sl.mta.model.v1_0.ModuleType;
import com.sap.cloud.lm.sl.mta.model.v1_0.PlatformModuleType;
import com.sap.cloud.lm.sl.mta.model.v1_0.PlatformResourceType;
import com.sap.cloud.lm.sl.mta.model.v1_0.ProvidedDependency;
import com.sap.cloud.lm.sl.mta.model.v1_0.Resource;
import com.sap.cloud.lm.sl.mta.model.v1_0.ResourceType;
import com.sap.cloud.lm.sl.mta.model.v1_0.Target;
import com.sap.cloud.lm.sl.mta.model.v1_0.Platform;
import com.sap.cloud.lm.sl.mta.util.PropertiesUtil;

public class PropertiesChainBuilder {

    protected final DeploymentDescriptor descriptor;
    protected final Target target;
    protected final Platform platform;
    protected final DescriptorHandler handler;

    public PropertiesChainBuilder(DeploymentDescriptor descriptor, Target target, Platform platform) {
        this(descriptor, target, platform, new DescriptorHandler());
    }

    public PropertiesChainBuilder(DeploymentDescriptor descriptor) {
        this(descriptor, null, null, new DescriptorHandler());
    }

    public PropertiesChainBuilder(DeploymentDescriptor descriptor, Target target, Platform platform,
        DescriptorHandler handler) {
        this.descriptor = descriptor;
        this.target = target;
        this.platform = platform;
        this.handler = handler;
    }

    public List<Map<String, Object>> buildModuleChain(String moduleName) {
        Module module = handler.findModule(descriptor, moduleName);
        if (module == null) {
            return Collections.emptyList();
        }
        Pair<List<Resource>, List<ProvidedDependency>> dependencies = findDependencies(module);
        PlatformModuleType platformModuleType = getPlatformModuleType(module);
        ModuleType moduleType = getModuleType(module);
        return getPropertiesList(module, moduleType, platformModuleType, dependencies._1, dependencies._2);
    }

    protected ModuleType getModuleType(Module module) {
        if (platform == null) {
            return null;
        }
        return handler.findModuleType(platform, module.getType());
    }

    protected PlatformModuleType getPlatformModuleType(Module module) {
        if (target == null) {
            return null;
        }
        return handler.findPlatformModuleType(target, module.getType());
    }

    public List<Map<String, Object>> buildModuleChainWithoutDependencies(String moduleName) {
        Module module = handler.findModule(descriptor, moduleName);
        if (module == null) {
            return Collections.emptyList();
        }
        PlatformModuleType platformModuleType = getPlatformModuleType(module);
        ModuleType moduleType = getModuleType(module);
        return PropertiesUtil.getPropertiesList(module, platformModuleType, moduleType);
    }

    public List<Map<String, Object>> buildResourceTypeChain(String resourceName) {
        Resource resource = handler.findResource(descriptor, resourceName);
        if (resource == null) {
            return Collections.emptyList();
        }
        ResourceType resourceType = getResourceType(resource);
        PlatformResourceType platformResourceType = getPlatformResourceType(resource);
        return PropertiesUtil.getPropertiesList(platformResourceType, resourceType);
    }

    protected PlatformResourceType getPlatformResourceType(Resource resource) {
        if (target == null) {
            return null;
        }
        return handler.findTargetResourceType(target, resource.getType());
    }

    protected ResourceType getResourceType(Resource resource) {
        if (platform == null) {
            return null;
        }
        return handler.findResourceType(platform, resource.getType());
    }

    public List<Map<String, Object>> buildResourceChain(String resourceName) {
        Resource resource = handler.findResource(descriptor, resourceName);
        if (resource == null) {
            return Collections.emptyList();
        }
        List<Map<String, Object>> resourceTypeChain = Collections.emptyList();
        if (resource != null && resource.getType() != null) {
            resourceTypeChain = buildResourceTypeChain(resourceName);
        }
        List<Map<String, Object>> resourceChain = PropertiesUtil.getPropertiesList(resource);
        resourceChain.addAll(resourceTypeChain);
        return resourceChain;
    }

    protected Pair<List<Resource>, List<ProvidedDependency>> findDependencies(Module module) {
        List<ProvidedDependency> providedDependencies = new ArrayList<ProvidedDependency>();
        List<Resource> resources = new ArrayList<Resource>();
        for (String dependencyName : module.getRequiredDependencies1_0()) {
            findDependency(dependencyName, providedDependencies, resources);
        }
        return new Pair<List<Resource>, List<ProvidedDependency>>(resources, providedDependencies);
    }

    protected void findDependency(String dependencyName, List<ProvidedDependency> providedDependencies, List<Resource> resources) {
        Pair<Resource, ProvidedDependency> dependency = handler.findDependency(descriptor, dependencyName);
        if (dependency == null) {
            return;
        }
        findResource(dependency, resources);
        findProvidedDependency(dependency, providedDependencies);
    }

    protected void findProvidedDependency(Pair<Resource, ProvidedDependency> dependency, List<ProvidedDependency> providedDependencies) {
        ProvidedDependency providedDependency = dependency._2;
        if (providedDependency != null) {
            providedDependencies.add(providedDependency);
        }
    }

    protected void findResource(Pair<Resource, ProvidedDependency> dependency, List<Resource> resources) {
        Resource resource = dependency._1;
        if (resource != null && resource.getType() == null) {
            resources.add(resource);
        }
    }

    protected static List<Map<String, Object>> getPropertiesList(Module module, ModuleType moduleType,
        PlatformModuleType platformModuleType, List<Resource> resources, List<ProvidedDependency> providedDependencies) {
        List<PropertiesContainer> containers = new ArrayList<PropertiesContainer>();
        for (Resource resource : resources) {
            containers.add(resource);
        }
        for (ProvidedDependency providedDependency : providedDependencies) {
            containers.add(providedDependency);
        }
        ListUtil.addNonNull(containers, module);
        ListUtil.addNonNull(containers, platformModuleType);
        ListUtil.addNonNull(containers, moduleType);
        return PropertiesUtil.getPropertiesList(containers);
    }

}

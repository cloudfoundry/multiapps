package com.sap.cloud.lm.sl.mta.builders.v1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;

import com.sap.cloud.lm.sl.common.util.Pair;
import com.sap.cloud.lm.sl.mta.handlers.v1.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.model.PropertiesContainer;
import com.sap.cloud.lm.sl.mta.model.v1.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v1.Module;
import com.sap.cloud.lm.sl.mta.model.v1.Platform;
import com.sap.cloud.lm.sl.mta.model.v1.PlatformModuleType;
import com.sap.cloud.lm.sl.mta.model.v1.PlatformResourceType;
import com.sap.cloud.lm.sl.mta.model.v1.ProvidedDependency;
import com.sap.cloud.lm.sl.mta.model.v1.Resource;
import com.sap.cloud.lm.sl.mta.model.v1.Target;
import com.sap.cloud.lm.sl.mta.model.v1.TargetModuleType;
import com.sap.cloud.lm.sl.mta.model.v1.TargetResourceType;
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

    public PropertiesChainBuilder(DeploymentDescriptor descriptor, Target target, Platform platform, DescriptorHandler handler) {
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
        TargetModuleType targetModuleType = getTargetModuleType(module);
        PlatformModuleType platformModuleType = getPlatformModuleType(module);
        return getPropertiesList(dependencies._1, dependencies._2, module, targetModuleType, platformModuleType);
    }

    protected TargetModuleType getTargetModuleType(Module module) {
        if (target == null) {
            return null;
        }
        return handler.findTargetModuleType(target, module.getType());
    }

    protected PlatformModuleType getPlatformModuleType(Module module) {
        if (platform == null) {
            return null;
        }
        return handler.findPlatformModuleType(platform, module.getType());
    }

    public List<Map<String, Object>> buildModuleChainWithoutDependencies(String moduleName) {
        Module module = handler.findModule(descriptor, moduleName);
        if (module == null) {
            return Collections.emptyList();
        }
        TargetModuleType targetModuleType = getTargetModuleType(module);
        PlatformModuleType platformModuleType = getPlatformModuleType(module);
        return PropertiesUtil.getPropertiesList(module, targetModuleType, platformModuleType);
    }

    public List<Map<String, Object>> buildResourceTypeChain(String resourceName) {
        Resource resource = handler.findResource(descriptor, resourceName);
        if (resource == null) {
            return Collections.emptyList();
        }
        TargetResourceType targetResourceType = getTargetResourceType(resource);
        PlatformResourceType platformResourceType = getPlatformResourceType(resource);
        return PropertiesUtil.getPropertiesList(targetResourceType, platformResourceType);
    }

    protected TargetResourceType getTargetResourceType(Resource resource) {
        if (target == null) {
            return null;
        }
        return handler.findTargetResourceType(target, resource.getType());
    }

    protected PlatformResourceType getPlatformResourceType(Resource resource) {
        if (platform == null) {
            return null;
        }
        return handler.findPlatformResourceType(platform, resource.getType());
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
        List<ProvidedDependency> providedDependencies = new ArrayList<>();
        List<Resource> resources = new ArrayList<>();
        for (String dependencyName : module.getRequiredDependencies1()) {
            findDependency(dependencyName, providedDependencies, resources);
        }
        return new Pair<>(resources, providedDependencies);
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

    protected static List<Map<String, Object>> getPropertiesList(List<Resource> resources, List<ProvidedDependency> providedDependencies,
        Module module, TargetModuleType targetModuleType, PlatformModuleType platformModuleType) {
        List<PropertiesContainer> containers = new ArrayList<>();
        for (Resource resource : resources) {
            containers.add(resource);
        }
        for (ProvidedDependency providedDependency : providedDependencies) {
            containers.add(providedDependency);
        }
        CollectionUtils.addIgnoreNull(containers, module);
        CollectionUtils.addIgnoreNull(containers, targetModuleType);
        CollectionUtils.addIgnoreNull(containers, platformModuleType);
        return PropertiesUtil.getPropertiesList(containers);
    }

}

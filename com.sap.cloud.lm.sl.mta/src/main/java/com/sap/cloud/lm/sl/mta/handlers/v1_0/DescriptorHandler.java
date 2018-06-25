package com.sap.cloud.lm.sl.mta.handlers.v1_0;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.common.util.Pair;
import com.sap.cloud.lm.sl.mta.builders.ExtensionChainBuilder;
import com.sap.cloud.lm.sl.mta.model.v1_0.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v1_0.ExtensionDescriptor;
import com.sap.cloud.lm.sl.mta.model.v1_0.ExtensionModule;
import com.sap.cloud.lm.sl.mta.model.v1_0.ExtensionProvidedDependency;
import com.sap.cloud.lm.sl.mta.model.v1_0.ExtensionResource;
import com.sap.cloud.lm.sl.mta.model.v1_0.Module;
import com.sap.cloud.lm.sl.mta.model.v1_0.PlatformModuleType;
import com.sap.cloud.lm.sl.mta.model.v1_0.Platform;
import com.sap.cloud.lm.sl.mta.model.v1_0.TargetModuleType;
import com.sap.cloud.lm.sl.mta.model.v1_0.TargetResourceType;
import com.sap.cloud.lm.sl.mta.model.v1_0.ProvidedDependency;
import com.sap.cloud.lm.sl.mta.model.v1_0.Resource;
import com.sap.cloud.lm.sl.mta.model.v1_0.PlatformResourceType;
import com.sap.cloud.lm.sl.mta.model.v1_0.Target;

public class DescriptorHandler {

    public Target findTarget(List<Target> targets, String targetName, Target defaultTarget) {
        for (Target target : targets) {
            if (target.getName()
                .equals(targetName)) {
                return target;
            }
        }
        return defaultTarget;
    }

    public Target findTarget(List<Target> targets, String targetName) {
        return findTarget(targets, targetName, null);
    }

    public Platform findPlatform(List<Platform> platforms, String platformName) {
        if (platformName == null && platforms.size() > 0) {
            return platforms.get(0);
        }
        for (Platform platform : platforms) {
            if (platform.getName()
                .equals(platformName)) {
                return platform;
            }
        }
        return null;
    }

    public PlatformResourceType findPlatformResourceType(Platform platform, String resourceTypeName) {
        for (PlatformResourceType resourceType : platform.getResourceTypes1_0()) {
            if (resourceType.getName()
                .equals(resourceTypeName)) {
                return resourceType;
            }
        }
        return null;
    }

    public TargetResourceType findTargetResourceType(Target target, String resourceTypeName) {
        for (TargetResourceType resourceType : target.getResourceTypes1_0()) {
            if (resourceType.getName()
                .equals(resourceTypeName)) {
                return resourceType;
            }
        }
        return null;
    }

    public PlatformModuleType findPlatformModuleType(Platform platform, String moduleTypeName) {
        for (PlatformModuleType moduleType : platform.getModuleTypes1_0()) {
            if (moduleType.getName()
                .equals(moduleTypeName)) {
                return moduleType;
            }
        }
        return null;
    }

    public TargetModuleType findTargetModuleType(Target target, String moduleTypeName) {
        for (TargetModuleType moduleType : target.getModuleTypes1_0()) {
            if (moduleType.getName()
                .equals(moduleTypeName)) {
                return moduleType;
            }
        }
        return null;
    }

    public Resource findResource(DeploymentDescriptor descriptor, String resourceName) {
        for (Resource resource : descriptor.getResources1_0()) {
            if (resource.getName()
                .equals(resourceName)) {
                return resource;
            }
        }
        return null;
    }

    public ExtensionResource findResource(ExtensionDescriptor descriptor, String resourceName) {
        for (ExtensionResource resource : descriptor.getResources1_0()) {
            if (resource.getName()
                .equals(resourceName)) {
                return resource;
            }
        }
        return null;
    }

    public Module findModule(DeploymentDescriptor descriptor, String moduleName) {
        for (Module module : descriptor.getModules1_0()) {
            if (module.getName()
                .equals(moduleName)) {
                return module;
            }
        }
        return null;
    }

    public ExtensionModule findModule(ExtensionDescriptor descriptor, String moduleName) {
        for (ExtensionModule module : descriptor.getModules1_0()) {
            if (module.getName()
                .equals(moduleName)) {
                return module;
            }
        }
        return null;
    }

    public Pair<Resource, ProvidedDependency> findDependency(DeploymentDescriptor descriptor, String dependencyName) {
        Resource resource = findResource(descriptor, dependencyName);
        if (resource != null) {
            return new Pair<Resource, ProvidedDependency>(resource, null);
        }
        ProvidedDependency providedDependency = findProvidedDependency(descriptor, dependencyName);
        if (providedDependency != null) {
            return new Pair<Resource, ProvidedDependency>(null, providedDependency);
        }
        return null;
    }

    public ProvidedDependency findProvidedDependency(DeploymentDescriptor descriptor, String providedDependencyName) {
        for (Module module : descriptor.getModules1_0()) {
            ProvidedDependency dependency = findProvidedDependency(module, providedDependencyName);
            if (dependency != null) {
                return dependency;
            }
        }
        return null;
    }

    public ExtensionProvidedDependency findProvidedDependency(ExtensionDescriptor descriptor, String providedDependencyName) {
        for (ExtensionModule module : descriptor.getModules1_0()) {
            ExtensionProvidedDependency dependency = findProvidedDependency(module, providedDependencyName);
            if (dependency != null) {
                return dependency;
            }
        }
        return null;
    }

    public ProvidedDependency findProvidedDependency(Module module, String providedDependencyName) {
        for (ProvidedDependency providedDependency : module.getProvidedDependencies1_0()) {
            if (providedDependency.getName()
                .equals(providedDependencyName)) {
                return providedDependency;
            }
        }
        return null;
    }

    public ExtensionProvidedDependency findProvidedDependency(ExtensionModule module, String providedDependencyName) {
        for (ExtensionProvidedDependency providedDependency : module.getProvidedDependencies1_0()) {
            if (providedDependency.getName()
                .equals(providedDependencyName)) {
                return providedDependency;
            }
        }
        return null;
    }

    public List<ExtensionDescriptor> getExtensionDescriptorChain(DeploymentDescriptor deploymentDescriptor,
        List<ExtensionDescriptor> extensionDescriptors, boolean isStrict) throws ContentException {
        return new ExtensionChainBuilder<>(deploymentDescriptor, extensionDescriptors, isStrict).build();
    }

    public List<ExtensionDescriptor> getExtensionDescriptorChain(DeploymentDescriptor deploymentDescriptor,
        List<ExtensionDescriptor> extensionDescriptors) throws ContentException {
        return getExtensionDescriptorChain(deploymentDescriptor, extensionDescriptors, true);
    }

    public List<Module> getSortedModules(DeploymentDescriptor descriptor, String dependencyTypeProperty, String hardDependencyType) {
        List<Pair<Module, Set<String>>> pairs = getModulesAndTheirDependencies(descriptor);
        Collections.sort(pairs, getModuleComparator(dependencyTypeProperty, hardDependencyType));
        List<Module> sortedModules = getModules(pairs);
        return sortedModules;
    }

    protected ModuleComparator getModuleComparator(String dependencyTypeProperty, String hardDependencyType) {
        return new ModuleComparator(dependencyTypeProperty, hardDependencyType);
    }

    protected List<Pair<Module, Set<String>>> getModulesAndTheirDependencies(DeploymentDescriptor descriptor) {
        List<Pair<Module, Set<String>>> result = new ArrayList<>();
        for (Module module : descriptor.getModules1_0()) {
            result.add(new Pair<Module, Set<String>>(module, getModuleDependencies(descriptor, module)));
        }
        return result;
    }

    protected Module findModuleProvidingDependency(DeploymentDescriptor descriptor, String dependencyName) {
        for (Module module : descriptor.getModules1_0()) {
            if (findProvidedDependency(module, dependencyName) != null) {
                return module;
            }
        }
        return null;
    }

    protected Set<String> getModuleDependencies(DeploymentDescriptor descriptor, Module module) {
        return getModuleDependencies(descriptor, module, new HashSet<String>());
    }

    protected Set<String> getModuleDependencies(DeploymentDescriptor descriptor, Module module, Set<String> seenModules) {
        /*
         * If any circular dependencies are detected an error should NOT be thrown, because the module comparator should be able to handle
         * them. Instead an empty set should be returned because the current module is the same as the origin module, and the origin
         * module's dependencies should already be included.
         */
        if (seenModules.contains(module.getName())) {
            return Collections.emptySet();
        } else {
            seenModules.add(module.getName());
        }
        Set<String> moduleDependencies = new HashSet<>();
        for (String dependencyName : getRequiredDependencyNames(module)) {
            Module moduleProvidingDependency = findModuleProvidingDependency(descriptor, dependencyName);
            if (moduleProvidingDependency != null) {
                moduleDependencies.add(moduleProvidingDependency.getName());
                moduleDependencies.addAll(getModuleDependencies(descriptor, moduleProvidingDependency, seenModules));
            }
        }
        return moduleDependencies;
    }

    protected List<String> getRequiredDependencyNames(Module module) {
        return module.getRequiredDependencies1_0();
    }

    protected List<Module> getModules(List<Pair<Module, Set<String>>> pairs) {
        List<Module> modules = new ArrayList<Module>();
        for (Pair<Module, Set<String>> pair : pairs) {
            modules.add(pair._1);
        }
        return modules;
    }

}

package com.sap.cloud.lm.sl.mta.handlers.v1;

import java.util.List;

import com.sap.cloud.lm.sl.common.util.Pair;
import com.sap.cloud.lm.sl.mta.handlers.ModulesSorter;
import com.sap.cloud.lm.sl.mta.model.v1.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v1.ExtensionDescriptor;
import com.sap.cloud.lm.sl.mta.model.v1.ExtensionModule;
import com.sap.cloud.lm.sl.mta.model.v1.ExtensionProvidedDependency;
import com.sap.cloud.lm.sl.mta.model.v1.ExtensionResource;
import com.sap.cloud.lm.sl.mta.model.v1.Module;
import com.sap.cloud.lm.sl.mta.model.v1.Platform;
import com.sap.cloud.lm.sl.mta.model.v1.PlatformModuleType;
import com.sap.cloud.lm.sl.mta.model.v1.PlatformResourceType;
import com.sap.cloud.lm.sl.mta.model.v1.ProvidedDependency;
import com.sap.cloud.lm.sl.mta.model.v1.Resource;

public class DescriptorHandler {

    public PlatformResourceType findPlatformResourceType(Platform platform, String resourceTypeName) {
        for (PlatformResourceType resourceType : platform.getResourceTypes1()) {
            if (resourceType.getName()
                .equals(resourceTypeName)) {
                return resourceType;
            }
        }
        return null;
    }

    public PlatformModuleType findPlatformModuleType(Platform platform, String moduleTypeName) {
        for (PlatformModuleType moduleType : platform.getModuleTypes1()) {
            if (moduleType.getName()
                .equals(moduleTypeName)) {
                return moduleType;
            }
        }
        return null;
    }

    public Resource findResource(DeploymentDescriptor descriptor, String resourceName) {
        for (Resource resource : descriptor.getResources1()) {
            if (resource.getName()
                .equals(resourceName)) {
                return resource;
            }
        }
        return null;
    }

    public ExtensionResource findResource(ExtensionDescriptor descriptor, String resourceName) {
        for (ExtensionResource resource : descriptor.getResources1()) {
            if (resource.getName()
                .equals(resourceName)) {
                return resource;
            }
        }
        return null;
    }

    public Module findModule(DeploymentDescriptor descriptor, String moduleName) {
        for (Module module : descriptor.getModules1()) {
            if (module.getName()
                .equals(moduleName)) {
                return module;
            }
        }
        return null;
    }

    public ExtensionModule findModule(ExtensionDescriptor descriptor, String moduleName) {
        for (ExtensionModule module : descriptor.getModules1()) {
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
            return new Pair<>(resource, null);
        }
        ProvidedDependency providedDependency = findProvidedDependency(descriptor, dependencyName);
        if (providedDependency != null) {
            return new Pair<>(null, providedDependency);
        }
        return null;
    }

    public ProvidedDependency findProvidedDependency(DeploymentDescriptor descriptor, String providedDependencyName) {
        for (Module module : descriptor.getModules1()) {
            ProvidedDependency dependency = findProvidedDependency(module, providedDependencyName);
            if (dependency != null) {
                return dependency;
            }
        }
        return null;
    }

    public ExtensionProvidedDependency findProvidedDependency(ExtensionDescriptor descriptor, String providedDependencyName) {
        for (ExtensionModule module : descriptor.getModules1()) {
            ExtensionProvidedDependency dependency = findProvidedDependency(module, providedDependencyName);
            if (dependency != null) {
                return dependency;
            }
        }
        return null;
    }

    public ProvidedDependency findProvidedDependency(Module module, String providedDependencyName) {
        for (ProvidedDependency providedDependency : module.getProvidedDependencies1()) {
            if (providedDependency.getName()
                .equals(providedDependencyName)) {
                return providedDependency;
            }
        }
        return null;
    }

    public ExtensionProvidedDependency findProvidedDependency(ExtensionModule module, String providedDependencyName) {
        for (ExtensionProvidedDependency providedDependency : module.getProvidedDependencies1()) {
            if (providedDependency.getName()
                .equals(providedDependencyName)) {
                return providedDependency;
            }
        }
        return null;
    }

    public List<? extends Module> getModulesForDeployment(DeploymentDescriptor descriptor, String parallelDeploymentProperty,
        String dependencyTypeProperty, String hardDependencyType) {
        ModulesSorter moduleSorter = getModuleSorter(descriptor, parallelDeploymentProperty, dependencyTypeProperty, hardDependencyType);
        return moduleSorter.sort();
    }

    protected ModulesSorter getModuleSorter(DeploymentDescriptor descriptor, String parallelDeploymentProperty,
        String dependencyTypeProperty, String hardDependencyType) {
        return new com.sap.cloud.lm.sl.mta.handlers.v1.ModulesSorter(descriptor, this, dependencyTypeProperty, hardDependencyType);
    }

}

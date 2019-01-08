package com.sap.cloud.lm.sl.mta.handlers.v2;

import java.util.List;

import com.sap.cloud.lm.sl.common.util.Pair;
import com.sap.cloud.lm.sl.mta.model.v2.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v2.ExtensionDescriptor;
import com.sap.cloud.lm.sl.mta.model.v2.ExtensionModule;
import com.sap.cloud.lm.sl.mta.model.v2.ExtensionProvidedDependency;
import com.sap.cloud.lm.sl.mta.model.v2.ExtensionRequiredDependency;
import com.sap.cloud.lm.sl.mta.model.v2.ExtensionResource;
import com.sap.cloud.lm.sl.mta.model.v2.Module;
import com.sap.cloud.lm.sl.mta.model.v2.ModuleType;
import com.sap.cloud.lm.sl.mta.model.v2.Platform;
import com.sap.cloud.lm.sl.mta.model.v2.ProvidedDependency;
import com.sap.cloud.lm.sl.mta.model.v2.RequiredDependency;
import com.sap.cloud.lm.sl.mta.model.v2.Resource;
import com.sap.cloud.lm.sl.mta.model.v2.ResourceType;

public class DescriptorHandler {

    public RequiredDependency findRequiredDependency(DeploymentDescriptor descriptor, String moduleName, String dependencyName) {
        for (Module module : descriptor.getModules2()) {
            if (module.getName()
                .equals(moduleName)) {
                return findRequiredDependency(module, dependencyName);
            }
        }
        return null;
    }

    public ExtensionRequiredDependency findRequiredDependency(ExtensionDescriptor descriptor, String moduleName, String dependencyName) {
        for (ExtensionModule module : descriptor.getModules()) {
            if (module.getName()
                .equals(moduleName)) {
                return findRequiredDependency(module, dependencyName);
            }
        }
        return null;
    }

    public RequiredDependency findRequiredDependency(Module module, String dependencyName) {
        for (RequiredDependency requiredDependency : module.getRequiredDependencies2()) {
            if (requiredDependency.getName()
                .equals(dependencyName)) {
                return requiredDependency;
            }
        }
        return null;
    }

    public ExtensionRequiredDependency findRequiredDependency(ExtensionModule module, String dependencyName) {
        for (ExtensionRequiredDependency requiredDependency : module.getRequiredDependencies2()) {
            if (requiredDependency.getName()
                .equals(dependencyName)) {
                return requiredDependency;
            }
        }
        return null;
    }

    protected ModulesSorter getModuleSorter(DeploymentDescriptor descriptor, String parallelDeploymentProperty,
        String dependencyTypeProperty, String hardDependencyType) {
        return new ModulesSorter(descriptor, this, dependencyTypeProperty, hardDependencyType);
    }

    public ResourceType findResourceType(Platform platform, String resourceTypeName) {
        for (ResourceType resourceType : platform.getResourceTypes2()) {
            if (resourceType.getName()
                .equals(resourceTypeName)) {
                return resourceType;
            }
        }
        return null;
    }

    public ModuleType findModuleType(Platform platform, String moduleTypeName) {
        for (ModuleType moduleType : platform.getModuleTypes2()) {
            if (moduleType.getName()
                .equals(moduleTypeName)) {
                return moduleType;
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

    public Resource findResource(DeploymentDescriptor descriptor, String resourceName) {
        for (Resource resource : descriptor.getResources2()) {
            if (resource.getName()
                .equals(resourceName)) {
                return resource;
            }
        }
        return null;
    }

    public ExtensionResource findResource(ExtensionDescriptor descriptor, String resourceName) {
        for (ExtensionResource resource : descriptor.getResources()) {
            if (resource.getName()
                .equals(resourceName)) {
                return resource;
            }
        }
        return null;
    }

    public ProvidedDependency findProvidedDependency(DeploymentDescriptor descriptor, String providedDependencyName) {
        for (Module module : descriptor.getModules2()) {
            ProvidedDependency dependency = findProvidedDependency(module, providedDependencyName);
            if (dependency != null) {
                return dependency;
            }
        }
        return null;
    }

    public ExtensionProvidedDependency findProvidedDependency(ExtensionDescriptor descriptor, String providedDependencyName) {
        for (ExtensionModule module : descriptor.getModules2()) {
            ExtensionProvidedDependency dependency = findProvidedDependency(module, providedDependencyName);
            if (dependency != null) {
                return dependency;
            }
        }
        return null;
    }

    public ProvidedDependency findProvidedDependency(Module module, String providedDependencyName) {
        for (ProvidedDependency providedDependency : module.getProvidedDependencies2()) {
            if (providedDependency.getName()
                .equals(providedDependencyName)) {
                return providedDependency;
            }
        }
        return null;
    }

    public ExtensionProvidedDependency findProvidedDependency(ExtensionModule module, String providedDependencyName) {
        for (ExtensionProvidedDependency providedDependency : module.getProvidedDependencies2()) {
            if (providedDependency.getName()
                .equals(providedDependencyName)) {
                return providedDependency;
            }
        }
        return null;
    }

    public ExtensionModule findModule(ExtensionDescriptor descriptor, String moduleName) {
        for (ExtensionModule module : descriptor.getModules2()) {
            if (module.getName()
                .equals(moduleName)) {
                return module;
            }
        }
        return null;
    }

    public Module findModule(DeploymentDescriptor descriptor, String moduleName) {
        for (Module module : descriptor.getModules2()) {
            if (module.getName()
                .equals(moduleName)) {
                return module;
            }
        }
        return null;
    }

    public List<? extends Module> getModulesForDeployment(DeploymentDescriptor descriptor, String parallelDeploymentProperty,
        String dependencyTypeProperty, String hardDependencyType) {
        ModulesSorter moduleSorter = getModuleSorter(descriptor, parallelDeploymentProperty, dependencyTypeProperty, hardDependencyType);
        return moduleSorter.sort();
    }

    
}

package org.cloudfoundry.multiapps.mta.handlers.v2;

import java.util.List;
import java.util.Objects;

import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.ExtensionDescriptor;
import org.cloudfoundry.multiapps.mta.model.ExtensionModule;
import org.cloudfoundry.multiapps.mta.model.ExtensionProvidedDependency;
import org.cloudfoundry.multiapps.mta.model.ExtensionRequiredDependency;
import org.cloudfoundry.multiapps.mta.model.ExtensionResource;
import org.cloudfoundry.multiapps.mta.model.Module;
import org.cloudfoundry.multiapps.mta.model.ModuleType;
import org.cloudfoundry.multiapps.mta.model.Platform;
import org.cloudfoundry.multiapps.mta.model.ProvidedDependency;
import org.cloudfoundry.multiapps.mta.model.RequiredDependency;
import org.cloudfoundry.multiapps.mta.model.Resource;
import org.cloudfoundry.multiapps.mta.model.ResourceType;

public class DescriptorHandler {

    public RequiredDependency findRequiredDependency(DeploymentDescriptor descriptor, String moduleName, String dependencyName) {
        return descriptor.getModules()
                         .stream()
                         .filter(module -> module.getName()
                                                 .equals(moduleName))
                         .findFirst()
                         .map(module -> findRequiredDependency(module, dependencyName))
                         .orElse(null);
    }

    public ExtensionRequiredDependency findRequiredDependency(ExtensionDescriptor descriptor, String moduleName, String dependencyName) {
        return descriptor.getModules()
                         .stream()
                         .filter(module -> module.getName()
                                                 .equals(moduleName))
                         .findFirst()
                         .map(module -> findRequiredDependency(module, dependencyName))
                         .orElse(null);
    }

    public RequiredDependency findRequiredDependency(Module module, String dependencyName) {
        return module.getRequiredDependencies()
                     .stream()
                     .filter(requiredDependency -> requiredDependency.getName()
                                                                     .equals(dependencyName))
                     .findFirst()
                     .orElse(null);
    }

    public ExtensionRequiredDependency findRequiredDependency(ExtensionModule module, String dependencyName) {
        return module.getRequiredDependencies()
                     .stream()
                     .filter(requiredDependency -> requiredDependency.getName()
                                                                     .equals(dependencyName))
                     .findFirst()
                     .orElse(null);
    }

    protected ModulesSorter getModuleSorter(DeploymentDescriptor descriptor, String parallelDeploymentProperty,
                                            String dependencyTypeProperty, String hardDependencyType) {
        return new ModulesSorter(descriptor, this, dependencyTypeProperty, hardDependencyType);
    }

    public ResourceType findResourceType(Platform platform, String resourceTypeName) {
        return platform.getResourceTypes()
                       .stream()
                       .filter(resourceType -> resourceType.getName()
                                                           .equals(resourceTypeName))
                       .findFirst()
                       .orElse(null);
    }

    public ModuleType findModuleType(Platform platform, String moduleTypeName) {
        return platform.getModuleTypes()
                       .stream()
                       .filter(moduleType -> moduleType.getName()
                                                       .equals(moduleTypeName))
                       .findFirst()
                       .orElse(null);
    }

    public Resource findResource(DeploymentDescriptor descriptor, String resourceName) {
        return descriptor.getResources()
                         .stream()
                         .filter(resource -> resource.getName()
                                                     .equals(resourceName))
                         .findFirst()
                         .orElse(null);
    }

    public ExtensionResource findResource(ExtensionDescriptor descriptor, String resourceName) {
        return descriptor.getResources()
                         .stream()
                         .filter(resource -> resource.getName()
                                                     .equals(resourceName))
                         .findFirst()
                         .orElse(null);
    }

    public ProvidedDependency findProvidedDependency(DeploymentDescriptor descriptor, String providedDependencyName) {
        return descriptor.getModules()
                         .stream()
                         .map(module -> findProvidedDependency(module, providedDependencyName))
                         .filter(Objects::nonNull)
                         .findFirst()
                         .orElse(null);
    }

    public ExtensionProvidedDependency findProvidedDependency(ExtensionDescriptor descriptor, String providedDependencyName) {
        return descriptor.getModules()
                         .stream()
                         .map(module -> findProvidedDependency(module, providedDependencyName))
                         .filter(Objects::nonNull)
                         .findFirst()
                         .orElse(null);
    }

    public ProvidedDependency findProvidedDependency(Module module, String providedDependencyName) {
        return module.getProvidedDependencies()
                     .stream()
                     .filter(providedDependency -> providedDependency.getName()
                                                                     .equals(providedDependencyName))
                     .findFirst()
                     .orElse(null);
    }

    public ExtensionProvidedDependency findProvidedDependency(ExtensionModule module, String providedDependencyName) {
        return module.getProvidedDependencies()
                     .stream()
                     .filter(providedDependency -> providedDependency.getName()
                                                                     .equals(providedDependencyName))
                     .findFirst()
                     .orElse(null);
    }

    public ExtensionModule findModule(ExtensionDescriptor descriptor, String moduleName) {
        return descriptor.getModules()
                         .stream()
                         .filter(module -> module.getName()
                                                 .equals(moduleName))
                         .findFirst()
                         .orElse(null);
    }

    public Module findModule(DeploymentDescriptor descriptor, String moduleName) {
        return descriptor.getModules()
                         .stream()
                         .filter(module -> module.getName()
                                                 .equals(moduleName))
                         .findFirst()
                         .orElse(null);
    }

    public List<Module> getModulesForDeployment(DeploymentDescriptor descriptor, String parallelDeploymentProperty,
                                                String dependencyTypeProperty, String hardDependencyType) {
        ModulesSorter moduleSorter = getModuleSorter(descriptor, parallelDeploymentProperty, dependencyTypeProperty, hardDependencyType);
        return moduleSorter.sort();
    }
}

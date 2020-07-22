package org.cloudfoundry.multiapps.mta.handlers.v3;

import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.ExtensionDescriptor;
import org.cloudfoundry.multiapps.mta.model.ExtensionHook;
import org.cloudfoundry.multiapps.mta.model.ExtensionModule;
import org.cloudfoundry.multiapps.mta.model.ExtensionRequiredDependency;
import org.cloudfoundry.multiapps.mta.model.ExtensionResource;
import org.cloudfoundry.multiapps.mta.model.RequiredDependency;
import org.cloudfoundry.multiapps.mta.model.Resource;

public class DescriptorHandler extends org.cloudfoundry.multiapps.mta.handlers.v2.DescriptorHandler {

    @Override
    public RequiredDependency findRequiredDependency(DeploymentDescriptor descriptor, String consumerName, String dependencyName) {
        RequiredDependency moduleRequiredDependency = super.findRequiredDependency(descriptor, consumerName, dependencyName);
        if (moduleRequiredDependency != null) {
            return moduleRequiredDependency;
        }
        return descriptor.getResources()
                         .stream()
                         .filter(resource -> resource.getName()
                                                     .equals(consumerName))
                         .findFirst()
                         .map(resource -> findRequiredDependency(resource, dependencyName))
                         .orElse(null);
    }

    @Override
    public ExtensionRequiredDependency findRequiredDependency(ExtensionDescriptor descriptor, String consumerName, String dependencyName) {
        ExtensionRequiredDependency moduleRequiredDependency = super.findRequiredDependency(descriptor, consumerName, dependencyName);
        if (moduleRequiredDependency != null) {
            return moduleRequiredDependency;
        }
        return descriptor.getResources()
                         .stream()
                         .filter(resource -> resource.getName()
                                                     .equals(consumerName))
                         .findFirst()
                         .map(resource -> findRequiredDependency(resource, dependencyName))
                         .orElse(null);
    }

    public RequiredDependency findRequiredDependency(Resource resource, String dependencyName) {
        return resource.getRequiredDependencies()
                       .stream()
                       .filter(requiredDependency -> requiredDependency.getName()
                                                                       .equals(dependencyName))
                       .findFirst()
                       .orElse(null);
    }

    public ExtensionRequiredDependency findRequiredDependency(ExtensionResource resource, String dependencyName) {
        return resource.getRequiredDependencies()
                       .stream()
                       .filter(requiredDependency -> requiredDependency.getName()
                                                                       .equals(dependencyName))
                       .findFirst()
                       .orElse(null);
    }

    public ExtensionRequiredDependency findRequiredDependency(ExtensionHook hook, String dependencyName) {
        return hook.getRequiredDependencies()
                   .stream()
                   .filter(requiredDependency -> requiredDependency.getName()
                                                                   .equals(dependencyName))
                   .findFirst()
                   .orElse(null);
    }

    @Override
    protected org.cloudfoundry.multiapps.mta.handlers.v2.ModulesSorter getModuleSorter(DeploymentDescriptor descriptor,
                                                                                String parallelDeploymentProperty,
                                                                                String dependencyTypeProperty, String hardDependencyType) {
        return new org.cloudfoundry.multiapps.mta.handlers.v3.ModulesSorter(descriptor,
                                                                     this,
                                                                     dependencyTypeProperty,
                                                                     hardDependencyType,
                                                                     parallelDeploymentProperty);
    }

    public ExtensionHook findHook(ExtensionDescriptor extensionDescriptor, String moduleName, String hookName) {
        ExtensionModule module = findModule(extensionDescriptor, moduleName);
        if (module == null) {
            return null;
        }
        return module.getHooks()
                     .stream()
                     .filter(hook -> hook.getName()
                                         .equals(hookName))
                     .findFirst()
                     .orElse(null);
    }

}

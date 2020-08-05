package org.cloudfoundry.multiapps.mta.resolvers.v2;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.cloudfoundry.multiapps.common.ContentException;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.Module;
import org.cloudfoundry.multiapps.mta.model.RequiredDependency;
import org.cloudfoundry.multiapps.mta.resolvers.Resolver;
import org.cloudfoundry.multiapps.mta.resolvers.ResolverBuilder;
import org.cloudfoundry.multiapps.mta.util.NameUtil;

public class ModuleReferenceResolver implements Resolver<Module> {

    protected final DeploymentDescriptor descriptor;
    protected final Module module;
    protected final String prefix;
    protected final ResolverBuilder propertiesResolverBuilder;
    protected final ResolverBuilder requiredDependenciesPropertiesResolverBuilder;

    public ModuleReferenceResolver(DeploymentDescriptor descriptor, Module module, String prefix, ResolverBuilder propertiesResolverBuilder,
                                   ResolverBuilder requiredDependenciesPropertiesResolverBuilder) {
        this.descriptor = descriptor;
        this.module = module;
        this.requiredDependenciesPropertiesResolverBuilder = requiredDependenciesPropertiesResolverBuilder;
        this.prefix = NameUtil.getPrefixedName(prefix, module.getName());
        this.propertiesResolverBuilder = propertiesResolverBuilder;
    }

    @Override
    public Module resolve() throws ContentException {
        module.setProperties(getResolvedProperties());
        module.setParameters(getResolvedParameters());
        module.setRequiredDependencies(getResolvedDependencies());
        return module;
    }

    private Map<String, Object> getResolvedProperties() {
        return createModulePropertiesReferenceResolver(module.getProperties()).resolve();
    }

    private Map<String, Object> getResolvedParameters() {
        return createModulePropertiesReferenceResolver(module.getParameters()).resolve();
    }

    protected ModulePropertiesReferenceResolver createModulePropertiesReferenceResolver(Map<String, Object> properties) {
        return new ModulePropertiesReferenceResolver(descriptor, module, properties, prefix, propertiesResolverBuilder);
    }

    protected List<RequiredDependency> getResolvedDependencies() {
        return module.getRequiredDependencies()
                     .stream()
                     .map(this::resolveRequiredDependency)
                     .collect(Collectors.toList());
    }

    protected RequiredDependency resolveRequiredDependency(RequiredDependency dependency) {
        return createRequiredDependencyResolver(dependency).resolve();
    }

    protected RequiredDependencyReferenceResolver createRequiredDependencyResolver(RequiredDependency requiredDependency) {
        return new RequiredDependencyReferenceResolver(descriptor,
                                                       module,
                                                       requiredDependency,
                                                       prefix,
                                                       requiredDependenciesPropertiesResolverBuilder);
    }

}

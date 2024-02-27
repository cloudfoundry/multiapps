package com.sap.cloud.lm.sl.mta.resolvers.v2;

import static com.sap.cloud.lm.sl.mta.util.ValidatorUtil.getPrefixedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.mta.model.v2.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v2.Module;
import com.sap.cloud.lm.sl.mta.model.v2.RequiredDependency;
import com.sap.cloud.lm.sl.mta.resolvers.Resolver;
import com.sap.cloud.lm.sl.mta.resolvers.ResolverBuilder;

public class ModuleReferenceResolver implements Resolver<Module> {

    protected final DeploymentDescriptor descriptor;
    protected final Module module;
    protected final String prefix;
    protected final ResolverBuilder propertiesResolverBuilder;
    protected final ResolverBuilder requiredDepencenciesPropertiesResolverBuilder;

    public ModuleReferenceResolver(DeploymentDescriptor descriptor, Module module, String prefix, ResolverBuilder propertiesResolverBuilder,
                                   ResolverBuilder requiredDepencenciesPropertiesResolverBuilder) {
        this.descriptor = descriptor;
        this.module = module;
        this.requiredDepencenciesPropertiesResolverBuilder = requiredDepencenciesPropertiesResolverBuilder;
        this.prefix = getPrefixedName(prefix, module.getName());
        this.propertiesResolverBuilder = propertiesResolverBuilder;
    }

    @Override
    public Module resolve() throws ContentException {
        module.setProperties(getResolvedProperties());
        module.setParameters(getResolvedParameters());
        module.setRequiredDependencies2(getResolvedDependencies());
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
        List<RequiredDependency> result = new ArrayList<>();
        for (RequiredDependency requiredDependency : module.getRequiredDependencies2()) {
            result.add(resolveRequiredDependency(requiredDependency));
        }
        return result;
    }

    protected RequiredDependency resolveRequiredDependency(RequiredDependency dependency) {
        return createRequiredDependencyResolver(dependency).resolve();
    }

    protected RequiredDependencyReferenceResolver createRequiredDependencyResolver(RequiredDependency requiredDependency) {
        return new RequiredDependencyReferenceResolver(descriptor,
                                                       module,
                                                       requiredDependency,
                                                       prefix,
                                                       requiredDepencenciesPropertiesResolverBuilder);
    }

}

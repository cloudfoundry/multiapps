package com.sap.cloud.lm.sl.mta.resolvers.v3;

import java.util.List;
import java.util.stream.Collectors;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.mta.model.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.Hook;
import com.sap.cloud.lm.sl.mta.model.Module;
import com.sap.cloud.lm.sl.mta.resolvers.ResolverBuilder;

public class ModuleReferenceResolver extends com.sap.cloud.lm.sl.mta.resolvers.v2.ModuleReferenceResolver {

    public ModuleReferenceResolver(DeploymentDescriptor descriptor, Module module, String prefix, ResolverBuilder propertiesResolverBuilder,
                                   ResolverBuilder requiredDependenciesPropertiesResolverBuilder) {
        super(descriptor, module, prefix, propertiesResolverBuilder, requiredDependenciesPropertiesResolverBuilder);
    }

    @Override
    public Module resolve() throws ContentException {
        super.resolve();
        module.setHooks(getResolvedHooks());
        return module;
    }

    private List<Hook> getResolvedHooks() {
        return module.getHooks()
                     .stream()
                     .map(hook -> getHookReferenceResolver(hook).resolve())
                     .collect(Collectors.toList());
    }

    private HookReferenceResolver getHookReferenceResolver(Hook hook) {
        return new HookReferenceResolver(hook,
                                         descriptor,
                                         module,
                                         prefix,
                                         propertiesResolverBuilder,
                                         requiredDependenciesPropertiesResolverBuilder);
    }

}

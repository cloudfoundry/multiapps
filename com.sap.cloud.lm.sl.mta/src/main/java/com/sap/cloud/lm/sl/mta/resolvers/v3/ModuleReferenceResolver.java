package com.sap.cloud.lm.sl.mta.resolvers.v3;

import java.util.ArrayList;
import java.util.List;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.mta.model.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.Hook;
import com.sap.cloud.lm.sl.mta.model.Module;
import com.sap.cloud.lm.sl.mta.resolvers.ResolverBuilder;

public class ModuleReferenceResolver extends com.sap.cloud.lm.sl.mta.resolvers.v2.ModuleReferenceResolver {

    public ModuleReferenceResolver(DeploymentDescriptor descriptor, Module module, String prefix, ResolverBuilder propertiesResolverBuilder,
        ResolverBuilder requiredDepencenciesPropertiesResolverBuilder) {
        super(descriptor, module, prefix, propertiesResolverBuilder, requiredDepencenciesPropertiesResolverBuilder);
    }

    @Override
    public Module resolve() throws ContentException {
        super.resolve();
        module.setHooks(getResolvedHooks());
        return module;
    }

    private List<Hook> getResolvedHooks() {
        List<Hook> resolvedHooks = new ArrayList<>();
        module.getHooks()
            .forEach(hook -> resolvedHooks.add(getHookReferenceResolver(hook).resolve()));
        return resolvedHooks;
    }

    private HookReferenceResolver getHookReferenceResolver(Hook hook) {
        return new HookReferenceResolver(hook, descriptor, module, prefix, propertiesResolverBuilder,
            requiredDepencenciesPropertiesResolverBuilder);
    }

}

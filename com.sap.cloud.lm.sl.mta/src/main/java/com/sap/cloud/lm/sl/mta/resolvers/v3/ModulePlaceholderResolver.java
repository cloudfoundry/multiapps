package com.sap.cloud.lm.sl.mta.resolvers.v3;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.mta.builders.v2.ParametersChainBuilder;
import com.sap.cloud.lm.sl.mta.model.Hook;
import com.sap.cloud.lm.sl.mta.model.Module;
import com.sap.cloud.lm.sl.mta.resolvers.ResolverBuilder;

public class ModulePlaceholderResolver extends com.sap.cloud.lm.sl.mta.resolvers.v2.ModulePlaceholderResolver {

    public ModulePlaceholderResolver(Module module, String prefix, ParametersChainBuilder parametersChainBuilder,
        ResolverBuilder propertiesResolverBuilder, ResolverBuilder parametersResolverBuilder, Map<String, String> singularToPluralMapping) {
        super(module, prefix, parametersChainBuilder, propertiesResolverBuilder, parametersResolverBuilder, singularToPluralMapping);
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
            .forEach(hook -> resolvedHooks.add(getHookPlaceholderResolver(hook).resolve()));
        return resolvedHooks;
    }

    private HookPlaceholderResolver getHookPlaceholderResolver(Hook hook) {
        return new HookPlaceholderResolver(module, hook, prefix, parametersChainBuilder, propertiesResolverBuilder,
            parametersResolverBuilder, singularToPluralMapping, getMergedParameters());
    }

}

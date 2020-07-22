package org.cloudfoundry.multiapps.mta.resolvers.v3;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.cloudfoundry.multiapps.common.ContentException;
import org.cloudfoundry.multiapps.mta.builders.v2.ParametersChainBuilder;
import org.cloudfoundry.multiapps.mta.model.Hook;
import org.cloudfoundry.multiapps.mta.model.Module;
import org.cloudfoundry.multiapps.mta.resolvers.ResolverBuilder;

public class ModulePlaceholderResolver extends org.cloudfoundry.multiapps.mta.resolvers.v2.ModulePlaceholderResolver {

    public ModulePlaceholderResolver(Module module, String prefix, ParametersChainBuilder parametersChainBuilder,
                                     ResolverBuilder propertiesResolverBuilder, ResolverBuilder parametersResolverBuilder,
                                     Map<String, String> singularToPluralMapping) {
        super(module, prefix, parametersChainBuilder, propertiesResolverBuilder, parametersResolverBuilder, singularToPluralMapping);
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
                     .map(hook -> getHookPlaceholderResolver(hook).resolve())
                     .collect(Collectors.toList());
    }

    private HookPlaceholderResolver getHookPlaceholderResolver(Hook hook) {
        return new HookPlaceholderResolver(module,
                                           hook,
                                           prefix,
                                           parametersChainBuilder,
                                           propertiesResolverBuilder,
                                           parametersResolverBuilder,
                                           singularToPluralMapping,
                                           getMergedParameters());
    }

}

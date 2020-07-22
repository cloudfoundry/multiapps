package org.cloudfoundry.multiapps.mta.resolvers.v3;

import java.util.List;
import java.util.stream.Collectors;

import org.cloudfoundry.multiapps.common.ContentException;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.Hook;
import org.cloudfoundry.multiapps.mta.model.Module;
import org.cloudfoundry.multiapps.mta.resolvers.ResolverBuilder;

public class ModuleReferenceResolver extends org.cloudfoundry.multiapps.mta.resolvers.v2.ModuleReferenceResolver {

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

package org.cloudfoundry.multiapps.mta.resolvers.v3;

import java.util.Map;
import java.util.Set;

import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.Module;
import org.cloudfoundry.multiapps.mta.resolvers.ResolverBuilder;

public class DescriptorPlaceholderResolver extends org.cloudfoundry.multiapps.mta.resolvers.v2.DescriptorPlaceholderResolver {

    public DescriptorPlaceholderResolver(DeploymentDescriptor descriptor, ResolverBuilder propertiesResolverBuilder,
                                         ResolverBuilder parametersResolverBuilder, Map<String, String> singularToPluralMapping,
                                         Set<String> dynamicResolvableParameters) {
        super(descriptor, propertiesResolverBuilder, parametersResolverBuilder, singularToPluralMapping, dynamicResolvableParameters);
    }

    @Override
    protected ModulePlaceholderResolver getModuleResolver(Module module) {
        return new ModulePlaceholderResolver(module,
                                             prefix,
                                             parametersChainBuilder,
                                             propertiesResolverBuilder,
                                             parametersResolverBuilder,
                                             singularToPluralMapping,
                                             dynamicResolvableParameters);
    }

}

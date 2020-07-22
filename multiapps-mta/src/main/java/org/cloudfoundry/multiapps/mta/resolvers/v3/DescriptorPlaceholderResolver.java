package org.cloudfoundry.multiapps.mta.resolvers.v3;

import java.util.Map;

import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.Module;
import org.cloudfoundry.multiapps.mta.resolvers.ResolverBuilder;

public class DescriptorPlaceholderResolver extends org.cloudfoundry.multiapps.mta.resolvers.v2.DescriptorPlaceholderResolver {

    public DescriptorPlaceholderResolver(DeploymentDescriptor descriptor, ResolverBuilder propertiesResolverBuilder,
                                         ResolverBuilder parametersResolverBuilder, Map<String, String> singularToPluralMapping) {
        super(descriptor, propertiesResolverBuilder, parametersResolverBuilder, singularToPluralMapping);
    }

    @Override
    protected ModulePlaceholderResolver getModuleResolver(Module module) {
        return new ModulePlaceholderResolver(module,
                                             prefix,
                                             parametersChainBuilder,
                                             propertiesResolverBuilder,
                                             parametersResolverBuilder,
                                             singularToPluralMapping);
    }

}

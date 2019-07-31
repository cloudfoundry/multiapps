package com.sap.cloud.lm.sl.mta.resolvers.v3;

import java.util.Map;

import com.sap.cloud.lm.sl.mta.model.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.Module;
import com.sap.cloud.lm.sl.mta.resolvers.ResolverBuilder;

public class DescriptorPlaceholderResolver extends com.sap.cloud.lm.sl.mta.resolvers.v2.DescriptorPlaceholderResolver {

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

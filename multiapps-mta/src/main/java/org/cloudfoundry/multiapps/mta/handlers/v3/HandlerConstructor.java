package org.cloudfoundry.multiapps.mta.handlers.v3;

import java.util.Map;

import org.cloudfoundry.multiapps.common.util.MiscUtil;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.resolvers.PlaceholderResolver;
import org.cloudfoundry.multiapps.mta.resolvers.Resolver;
import org.cloudfoundry.multiapps.mta.resolvers.ResolverBuilder;
import org.cloudfoundry.multiapps.mta.resolvers.v3.DescriptorPlaceholderResolver;
import org.cloudfoundry.multiapps.mta.resolvers.v3.DescriptorReferenceResolver;

public class HandlerConstructor extends org.cloudfoundry.multiapps.mta.handlers.v2.HandlerConstructor {

    @Override
    public DescriptorParser getDescriptorParser() {
        return new DescriptorParser();
    }

    @Override
    public DescriptorMerger getDescriptorMerger() {
        return new DescriptorMerger(getDescriptorHandler());
    }

    @Override
    public DescriptorValidator getDescriptorValidator() {
        return new DescriptorValidator();
    }

    @Override
    public DescriptorHandler getDescriptorHandler() {
        if (this.handler != null) {
            return MiscUtil.cast(this.handler);
        }
        return new DescriptorHandler();
    }

    @Override
    public PlaceholderResolver<DeploymentDescriptor>
           getDescriptorPlaceholderResolver(DeploymentDescriptor mergedDescriptor, ResolverBuilder propertiesResolverBuilder,
                                            ResolverBuilder parametersResolverBuilder, Map<String, String> singularToPluralMapping) {
        return new DescriptorPlaceholderResolver(mergedDescriptor,
                                                 propertiesResolverBuilder,
                                                 parametersResolverBuilder,
                                                 singularToPluralMapping);
    }

    @Override
    public Resolver<DeploymentDescriptor> getDescriptorReferenceResolver(DeploymentDescriptor mergedDescriptor,
                                                                         ResolverBuilder modulesPropertiesResolverBuilder,
                                                                         ResolverBuilder resourcePropertiesResolverBuilder,
                                                                         ResolverBuilder requiredDependenciesPropertiesResolverBuilder) {
        return new DescriptorReferenceResolver(mergedDescriptor,
                                               modulesPropertiesResolverBuilder,
                                               resourcePropertiesResolverBuilder,
                                               requiredDependenciesPropertiesResolverBuilder);
    }

}

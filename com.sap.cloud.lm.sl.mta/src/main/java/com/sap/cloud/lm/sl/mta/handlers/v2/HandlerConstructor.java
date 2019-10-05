package com.sap.cloud.lm.sl.mta.handlers.v2;

import java.util.Map;

import com.sap.cloud.lm.sl.mta.model.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.resolvers.PlaceholderResolver;
import com.sap.cloud.lm.sl.mta.resolvers.Resolver;
import com.sap.cloud.lm.sl.mta.resolvers.ResolverBuilder;
import com.sap.cloud.lm.sl.mta.resolvers.v2.DescriptorPlaceholderResolver;
import com.sap.cloud.lm.sl.mta.resolvers.v2.DescriptorReferenceResolver;

public class HandlerConstructor implements com.sap.cloud.lm.sl.mta.handlers.HandlerConstructor {

    protected DescriptorHandler handler;

    public HandlerConstructor() {
        handler = getDescriptorHandler();
    }

    @Override
    public DescriptorParser getDescriptorParser() {
        return new DescriptorParser();
    }

    @Override
    public DescriptorHandler getDescriptorHandler() {
        if (this.handler != null) {
            return this.handler;
        }
        return new DescriptorHandler();
    }

    @Override
    public DescriptorMerger getDescriptorMerger() {
        return new DescriptorMerger(getDescriptorHandler());
    }

    @Override
    public DescriptorValidator getDescriptorValidator() {
        return new DescriptorValidator(getDescriptorHandler());
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
                requiredDependenciesPropertiesResolverBuilder);
    }
}

package com.sap.cloud.lm.sl.mta.handlers.v3;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.cast;

import java.util.Map;

import com.sap.cloud.lm.sl.mta.model.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.resolvers.PlaceholderResolver;
import com.sap.cloud.lm.sl.mta.resolvers.Resolver;
import com.sap.cloud.lm.sl.mta.resolvers.ResolverBuilder;
import com.sap.cloud.lm.sl.mta.resolvers.v3.DescriptorPlaceholderResolver;
import com.sap.cloud.lm.sl.mta.resolvers.v3.DescriptorReferenceResolver;

public class HandlerConstructor extends com.sap.cloud.lm.sl.mta.handlers.v2.HandlerConstructor {

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
            return cast(this.handler);
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
                                                                         ResolverBuilder requiredDepencenciesPropertiesResolverBuilder) {
        return new DescriptorReferenceResolver(mergedDescriptor,
                                               modulesPropertiesResolverBuilder,
                                               resourcePropertiesResolverBuilder,
                                               requiredDepencenciesPropertiesResolverBuilder);
    }

}

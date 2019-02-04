package com.sap.cloud.lm.sl.mta.handlers.v2;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.cast;

import com.sap.cloud.lm.sl.mta.model.SystemParameters;
import com.sap.cloud.lm.sl.mta.model.v2.DeploymentDescriptor;
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
    public PlaceholderResolver<? extends DeploymentDescriptor> getDescriptorPlaceholderResolver(final DeploymentDescriptor mergedDescriptor,
        SystemParameters systemParameters, ResolverBuilder propertiesResolverBuilder, ResolverBuilder parametersResolverBuilder) {
        com.sap.cloud.lm.sl.mta.model.v2.DeploymentDescriptor mergedDescriptorV2 = cast(mergedDescriptor);
        return new DescriptorPlaceholderResolver(mergedDescriptorV2, systemParameters, propertiesResolverBuilder,
            parametersResolverBuilder);
    }

    @Override
    public Resolver<? extends DeploymentDescriptor> getDescriptorReferenceResolver(DeploymentDescriptor mergedDescriptor,
        ResolverBuilder modulesPropertiesResolverBuilder, ResolverBuilder resourcePropertiesResolverBuilder,
        ResolverBuilder requiredDepencenciesPropertiesResolverBuilder) {
        return new DescriptorReferenceResolver((com.sap.cloud.lm.sl.mta.model.v2.DeploymentDescriptor) mergedDescriptor,
            modulesPropertiesResolverBuilder, requiredDepencenciesPropertiesResolverBuilder);
    }
}

package com.sap.cloud.lm.sl.mta.handlers.v1_0;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.mta.model.SystemParameters;
import com.sap.cloud.lm.sl.mta.model.v1_0.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v1_0.Platform;
import com.sap.cloud.lm.sl.mta.model.v1_0.Target;
import com.sap.cloud.lm.sl.mta.resolvers.PlaceholderResolver;
import com.sap.cloud.lm.sl.mta.resolvers.Resolver;
import com.sap.cloud.lm.sl.mta.resolvers.ResolverBuilder;

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
        if (handler != null) {
            return handler;
        }
        return new DescriptorHandler();
    }

    @Override
    public ConfigurationParser getConfigurationParser() {
        return new ConfigurationParser();
    }

    @Override
    public DescriptorMerger getDescriptorMerger() {
        return new DescriptorMerger(handler);
    }

    @Override
    public DescriptorValidator getDescriptorValidator() {
        return new DescriptorValidator(handler);
    }

    @Override
    public PlaceholderResolver<? extends DeploymentDescriptor> getDescriptorPlaceholderResolver(final DeploymentDescriptor mergedDescriptor,
        Platform platform, Target target, SystemParameters systemParameters, ResolverBuilder propertiesResolverBuilder,
        ResolverBuilder parametersResolverBuilder) {
        return new PlaceholderResolver<DeploymentDescriptor>("", "", systemParameters) {
            public DeploymentDescriptor descriptor = mergedDescriptor;

            @Override
            public DeploymentDescriptor resolve() throws ContentException {
                return descriptor;
            }
        };
    }

    @Override
    public Resolver<? extends DeploymentDescriptor> getDescriptorReferenceResolver(
        final DeploymentDescriptor mergedDescriptor, ResolverBuilder modulesPropertiesResolverBuilder,
        ResolverBuilder resourcePropertiesResolverBuilder, ResolverBuilder requiredDepencenciesPropertiesResolverBuilder) {
        return new Resolver<DeploymentDescriptor>() {
            @Override
            public DeploymentDescriptor resolve() {
                return mergedDescriptor;
            }
        };
    }

}

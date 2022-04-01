package com.sap.cloud.lm.sl.mta.handlers.v3;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.cast;

import com.sap.cloud.lm.sl.mta.model.SystemParameters;
import com.sap.cloud.lm.sl.mta.model.v2.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v2.Platform;
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
    public ConfigurationParser getConfigurationParser() {
        return new ConfigurationParser();
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
    public PlaceholderResolver<? extends DeploymentDescriptor>
           getDescriptorPlaceholderResolver(final DeploymentDescriptor mergedDescriptor, Platform platform,
                                            SystemParameters systemParameters, ResolverBuilder propertiesResolverBuilder,
                                            ResolverBuilder parametersResolverBuilder) {
        com.sap.cloud.lm.sl.mta.model.v3.DeploymentDescriptor mergedDescriptorV3 = cast(mergedDescriptor);
        com.sap.cloud.lm.sl.mta.model.v2.Platform platformV2 = cast(platform);
        return new DescriptorPlaceholderResolver(mergedDescriptorV3,
                                                 platformV2,
                                                 systemParameters,
                                                 propertiesResolverBuilder,
                                                 parametersResolverBuilder);
    }

    @Override
    public Resolver<? extends com.sap.cloud.lm.sl.mta.model.v3.DeploymentDescriptor>
           getDescriptorReferenceResolver(DeploymentDescriptor mergedDescriptor, ResolverBuilder modulesPropertiesResolverBuilder,
                                          ResolverBuilder resourcePropertiesResolverBuilder,
                                          ResolverBuilder requiredDepencenciesPropertiesResolverBuilder) {
        com.sap.cloud.lm.sl.mta.model.v3.DeploymentDescriptor descriptor = cast(mergedDescriptor);
        return cast(new DescriptorReferenceResolver(descriptor,
                                                    modulesPropertiesResolverBuilder,
                                                    resourcePropertiesResolverBuilder,
                                                    requiredDepencenciesPropertiesResolverBuilder));
    }

}

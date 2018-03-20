package com.sap.cloud.lm.sl.mta.handlers.v3_1;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.cast;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.mta.model.SystemParameters;
import com.sap.cloud.lm.sl.mta.model.v1_0.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v1_0.Platform;
import com.sap.cloud.lm.sl.mta.model.v1_0.Target;
import com.sap.cloud.lm.sl.mta.resolvers.PlaceholderResolver;
import com.sap.cloud.lm.sl.mta.resolvers.Resolver;
import com.sap.cloud.lm.sl.mta.resolvers.ResolverBuilder;
import com.sap.cloud.lm.sl.mta.resolvers.v3_1.DescriptorPlaceholderResolver;
import com.sap.cloud.lm.sl.mta.resolvers.v3_1.DescriptorReferenceResolver;

public class HandlerConstructor extends com.sap.cloud.lm.sl.mta.handlers.v3_0.HandlerConstructor {

    @Override
    public DescriptorParser getDescriptorParser() {
        return new DescriptorParser();
    }

    @Override
    public ConfigurationParser getConfigurationParser() {
        return new ConfigurationParser();
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
    public PlaceholderResolver<? extends DeploymentDescriptor> getDescriptorPlaceholderResolver(final DeploymentDescriptor mergedDescriptor,
        Platform platform, Target target, SystemParameters systemParameters, ResolverBuilder propertiesResolverBuilder,
        ResolverBuilder parametersResolverBuilder) {
        com.sap.cloud.lm.sl.mta.model.v3_1.DeploymentDescriptor mergedDescriptorV3 = cast(mergedDescriptor);
        com.sap.cloud.lm.sl.mta.model.v2_0.Platform platformV2 = cast(platform);
        com.sap.cloud.lm.sl.mta.model.v2_0.Target targetV2 = cast(target);
        return new DescriptorPlaceholderResolver(mergedDescriptorV3, platformV2, targetV2, systemParameters, propertiesResolverBuilder,
            parametersResolverBuilder);
    }

    @Override
    public Resolver<? extends com.sap.cloud.lm.sl.mta.model.v3_1.DeploymentDescriptor, ContentException> getDescriptorReferenceResolver(
        DeploymentDescriptor mergedDescriptor, ResolverBuilder modulesPropertiesResolverBuilder,
        ResolverBuilder resourcePropertiesResolverBuilder, ResolverBuilder requiredDepencenciesPropertiesResolverBuilder) {
        com.sap.cloud.lm.sl.mta.model.v3_1.DeploymentDescriptor descriptor = cast(mergedDescriptor);
        return cast(new DescriptorReferenceResolver(descriptor, modulesPropertiesResolverBuilder, resourcePropertiesResolverBuilder,
            requiredDepencenciesPropertiesResolverBuilder));
    }
}

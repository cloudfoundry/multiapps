package com.sap.cloud.lm.sl.mta.handlers;

import com.sap.cloud.lm.sl.mta.handlers.v1.ConfigurationParser;
import com.sap.cloud.lm.sl.mta.handlers.v1.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.handlers.v1.DescriptorMerger;
import com.sap.cloud.lm.sl.mta.handlers.v1.DescriptorParser;
import com.sap.cloud.lm.sl.mta.handlers.v1.DescriptorValidator;
import com.sap.cloud.lm.sl.mta.model.SystemParameters;
import com.sap.cloud.lm.sl.mta.model.v1.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v1.Platform;
import com.sap.cloud.lm.sl.mta.resolvers.PlaceholderResolver;
import com.sap.cloud.lm.sl.mta.resolvers.Resolver;
import com.sap.cloud.lm.sl.mta.resolvers.ResolverBuilder;

public interface HandlerConstructor {

    DescriptorParser getDescriptorParser();

    DescriptorHandler getDescriptorHandler();

    ConfigurationParser getConfigurationParser();

    DescriptorMerger getDescriptorMerger();

    DescriptorValidator getDescriptorValidator();

    PlaceholderResolver<? extends DeploymentDescriptor> getDescriptorPlaceholderResolver(DeploymentDescriptor mergedDescriptor,
        Platform platform, SystemParameters systemParameters, ResolverBuilder propertiesResolverBuilder,
        ResolverBuilder parametersResolverBuilder);

    Resolver<? extends DeploymentDescriptor> getDescriptorReferenceResolver(DeploymentDescriptor mergedDescriptor,
        ResolverBuilder modulesPropertiesResolverBuilder, ResolverBuilder resourcePropertiesResolverBuilder,
        ResolverBuilder requiredDepencenciesPropertiesResolverBuilder);

}

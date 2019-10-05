package com.sap.cloud.lm.sl.mta.handlers;

import java.util.Map;

import com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorMerger;
import com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorParser;
import com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorValidator;
import com.sap.cloud.lm.sl.mta.model.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.resolvers.PlaceholderResolver;
import com.sap.cloud.lm.sl.mta.resolvers.Resolver;
import com.sap.cloud.lm.sl.mta.resolvers.ResolverBuilder;

public interface HandlerConstructor {

    DescriptorParser getDescriptorParser();

    DescriptorHandler getDescriptorHandler();

    DescriptorMerger getDescriptorMerger();

    DescriptorValidator getDescriptorValidator();

    PlaceholderResolver<DeploymentDescriptor> getDescriptorPlaceholderResolver(DeploymentDescriptor mergedDescriptor,
                                                                               ResolverBuilder propertiesResolverBuilder,
                                                                               ResolverBuilder parametersResolverBuilder,
                                                                               Map<String, String> singularToPluralMapping);

    Resolver<DeploymentDescriptor> getDescriptorReferenceResolver(DeploymentDescriptor mergedDescriptor,
                                                                  ResolverBuilder modulesPropertiesResolverBuilder,
                                                                  ResolverBuilder resourcePropertiesResolverBuilder,
                                                                  ResolverBuilder requiredDependenciesPropertiesResolverBuilder);

}

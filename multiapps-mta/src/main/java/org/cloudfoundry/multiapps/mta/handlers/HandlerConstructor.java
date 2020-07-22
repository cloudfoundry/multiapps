package org.cloudfoundry.multiapps.mta.handlers;

import java.util.Map;

import org.cloudfoundry.multiapps.mta.handlers.v2.DescriptorHandler;
import org.cloudfoundry.multiapps.mta.handlers.v2.DescriptorMerger;
import org.cloudfoundry.multiapps.mta.handlers.v2.DescriptorParser;
import org.cloudfoundry.multiapps.mta.handlers.v2.DescriptorValidator;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.resolvers.PlaceholderResolver;
import org.cloudfoundry.multiapps.mta.resolvers.Resolver;
import org.cloudfoundry.multiapps.mta.resolvers.ResolverBuilder;

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

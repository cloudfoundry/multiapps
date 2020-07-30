package org.cloudfoundry.multiapps.mta.handlers;

import static java.text.MessageFormat.format;

import java.util.Map;

import org.cloudfoundry.multiapps.mta.Messages;
import org.cloudfoundry.multiapps.mta.handlers.v2.DescriptorHandler;
import org.cloudfoundry.multiapps.mta.handlers.v2.DescriptorMerger;
import org.cloudfoundry.multiapps.mta.handlers.v2.DescriptorParser;
import org.cloudfoundry.multiapps.mta.handlers.v2.DescriptorValidator;
import org.cloudfoundry.multiapps.mta.handlers.v2.HandlerFactoryV2;
import org.cloudfoundry.multiapps.mta.handlers.v3.HandlerFactoryV3;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.resolvers.PlaceholderResolver;
import org.cloudfoundry.multiapps.mta.resolvers.Resolver;
import org.cloudfoundry.multiapps.mta.resolvers.ResolverBuilder;

public interface HandlerFactory {

    static final Integer HIGHEST_VERSION_INPUT = Integer.MAX_VALUE;

    static HandlerFactory forSchemaVersion(int majorSchemaVersion) {
        switch (majorSchemaVersion) {
            case 1:
            case 2:
                return new HandlerFactoryV2();
            case Integer.MAX_VALUE: // HandlerFactory.MAX_VERSION_SUPPORTED_INPUT
            case 3:
                return new HandlerFactoryV3();
            default:
                throw new UnsupportedOperationException(format(Messages.UNSUPPORTED_VERSION, majorSchemaVersion));
        }
    }

    int getMajorSchemaVersion();

    DescriptorParser getDescriptorParser();

    DescriptorHandler getDescriptorHandler();

    DescriptorMerger getDescriptorMerger();

    DescriptorValidator getDescriptorValidator();

    PlaceholderResolver<DeploymentDescriptor>
                       getDescriptorPlaceholderResolver(DeploymentDescriptor mergedDescriptor, ResolverBuilder propertiesResolver,
                                                        ResolverBuilder parametersResolver, Map<String, String> singularToPluralMapping);

    Resolver<DeploymentDescriptor> getDescriptorReferenceResolver(DeploymentDescriptor mergedDescriptor,
                                                                  ResolverBuilder modulesPropertiesResolverBuilder,
                                                                  ResolverBuilder resourcePropertiesResolverBuilder,
                                                                  ResolverBuilder requiredDependenciesPropertiesResolverBuilder);

}

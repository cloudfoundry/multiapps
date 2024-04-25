package org.cloudfoundry.multiapps.mta.handlers.v3;

import java.util.Map;
import java.util.Set;

import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.resolvers.PlaceholderResolver;
import org.cloudfoundry.multiapps.mta.resolvers.Resolver;
import org.cloudfoundry.multiapps.mta.resolvers.ResolverBuilder;
import org.cloudfoundry.multiapps.mta.resolvers.v3.DescriptorPlaceholderResolver;
import org.cloudfoundry.multiapps.mta.resolvers.v3.DescriptorReferenceResolver;

public class HandlerFactoryV3 implements org.cloudfoundry.multiapps.mta.handlers.HandlerFactory {

    @Override
    public int getMajorSchemaVersion() {
        return 3;
    }

    @Override
    public DescriptorParser getDescriptorParser() {
        return new DescriptorParser();
    }

    @Override
    public DescriptorHandler getDescriptorHandler() {
        return new DescriptorHandler();
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
    public PlaceholderResolver<DeploymentDescriptor>
           getDescriptorPlaceholderResolver(DeploymentDescriptor mergedDescriptor, ResolverBuilder propertiesResolverBuilder,
                                            ResolverBuilder parametersResolverBuilder, Map<String, String> singularToPluralMapping,
                                            Set<String> dynamicResolvableParameters, Map<String, String> idleToLiveParameterPairs) {
        return new DescriptorPlaceholderResolver(mergedDescriptor,
                                                 propertiesResolverBuilder,
                                                 parametersResolverBuilder,
                                                 singularToPluralMapping,
                                                 dynamicResolvableParameters,
                idleToLiveParameterPairs);
    }

    @Override
    public Resolver<DeploymentDescriptor> getDescriptorReferenceResolver(DeploymentDescriptor mergedDescriptor,
                                                                         ResolverBuilder modulesPropertiesResolverBuilder,
                                                                         ResolverBuilder resourcePropertiesResolverBuilder,
                                                                         ResolverBuilder requiredDependenciesPropertiesResolverBuilder,
                                                                         Set<String> dynamicResolvableParameters) {
        return new DescriptorReferenceResolver(mergedDescriptor,
                                               modulesPropertiesResolverBuilder,
                                               resourcePropertiesResolverBuilder,
                                               requiredDependenciesPropertiesResolverBuilder,
                                               dynamicResolvableParameters);
    }

    @Override
    public ResourceBatchCalculator getResourceBatchCalculator(DeploymentDescriptor descriptor) {
        return new ResourceBatchCalculator(descriptor);
    }

    @Override
    public SelectiveDeployChecker
           getSelectiveDeployChecker(DeploymentDescriptor deploymentDescriptor,
                                     org.cloudfoundry.multiapps.mta.handlers.v2.DescriptorHandler descriptorHandler) {
        return new SelectiveDeployChecker(deploymentDescriptor, descriptorHandler);
    }
}

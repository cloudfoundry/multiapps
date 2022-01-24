package org.cloudfoundry.multiapps.mta.handlers.v2;

import java.util.Map;

import org.cloudfoundry.multiapps.mta.handlers.HandlerFactory;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.resolvers.PlaceholderResolver;
import org.cloudfoundry.multiapps.mta.resolvers.Resolver;
import org.cloudfoundry.multiapps.mta.resolvers.ResolverBuilder;
import org.cloudfoundry.multiapps.mta.resolvers.v2.DescriptorPlaceholderResolver;
import org.cloudfoundry.multiapps.mta.resolvers.v2.DescriptorReferenceResolver;

public class HandlerFactoryV2 implements HandlerFactory {

    @Override
    public int getMajorSchemaVersion() {
        return 2;
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
        return new DescriptorValidator(getDescriptorHandler());
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
                                                                         ResolverBuilder requiredDependenciesPropertiesResolverBuilder) {
        return new DescriptorReferenceResolver(mergedDescriptor,
                                               modulesPropertiesResolverBuilder,
                                               requiredDependenciesPropertiesResolverBuilder);
    }

    @Override
    public ResourceBatchCalculator getResourceBatchCalculator(DeploymentDescriptor descriptor) {
        return new ResourceBatchCalculator();
    }

    @Override
    public SelectiveDeployChecker getSelectiveDeployChecker(DeploymentDescriptor deploymentDescriptor,
                                                            DescriptorHandler descriptorHandler) {
        return new SelectiveDeployChecker();
    }
}

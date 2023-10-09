package org.cloudfoundry.multiapps.mta.resolvers.v3;

import static org.cloudfoundry.multiapps.mta.resolvers.ReferencePattern.FULLY_QUALIFIED;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.cloudfoundry.multiapps.common.ContentException;
import org.cloudfoundry.multiapps.mta.handlers.v3.DescriptorHandler;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.NamedElement;
import org.cloudfoundry.multiapps.mta.model.ProvidedDependency;
import org.cloudfoundry.multiapps.mta.resolvers.ProvidedValuesResolver;
import org.cloudfoundry.multiapps.mta.resolvers.ProvidesValuesResolver;
import org.cloudfoundry.multiapps.mta.resolvers.ReferenceResolver;
import org.cloudfoundry.multiapps.mta.resolvers.ResolverBuilder;

public class ProvidedDependencyReferenceResolver extends ReferenceResolver<ProvidedDependency> {

    protected final ProvidedDependency dependency;
    protected final ResolverBuilder propertiesResolverBuilder;
    private DescriptorHandler handler;
    private DeploymentDescriptor descriptor;
    private String consumerName;

    public ProvidedDependencyReferenceResolver(DeploymentDescriptor descriptor, NamedElement consumer, ProvidedDependency dependency,
                                               String prefix, ResolverBuilder propertiesResolverBuilder,
                                               Set<String> dynamicResolvableParameters) {
        this(descriptor, consumer, dependency, prefix, new DescriptorHandler(), propertiesResolverBuilder, dynamicResolvableParameters);
    }

    public ProvidedDependencyReferenceResolver(DeploymentDescriptor descriptor, NamedElement consumer, ProvidedDependency dependency,
                                               String prefix, DescriptorHandler handler, ResolverBuilder propertiesResolverBuilder,
                                               Set<String> dynamicResolvableParameters) {
        super(dependency.getName(), prefix, handler, descriptor, consumer.getName(), FULLY_QUALIFIED, dynamicResolvableParameters);
        this.dependency = dependency;
        this.propertiesResolverBuilder = propertiesResolverBuilder;
        this.handler = handler;
        this.consumerName = consumer.getName();
        this.descriptor = descriptor;
    }

    @Override
    public ProvidedDependency resolve() throws ContentException {
        Map<String, Object> resolvedProperties = resolve(dependency.getProperties(), Collections.emptyMap(), true);
        Map<String, Object> resolvedParameters = resolve(dependency.getParameters(), Collections.emptyMap(), true);
        dependency.setProperties(resolvedProperties);
        dependency.setParameters(resolvedParameters);
        return dependency;
    }

    @Override
    protected Map<String, Object> resolve(Map<String, Object> properties, final Map<String, Object> propertyValues, Boolean isStrict) {
        ProvidedValuesResolver valuesResolver = new ProvidesValuesResolver(consumerName, handler, descriptor);
        return propertiesResolverBuilder.build(properties, valuesResolver, patternToMatch, prefix, isStrict, dynamicResolvableParameters)
                                        .resolve();
    }

}

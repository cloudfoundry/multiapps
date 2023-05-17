package org.cloudfoundry.multiapps.mta.resolvers.v2;

import static org.cloudfoundry.multiapps.mta.resolvers.ReferencePattern.SHORT;

import java.util.Map;
import java.util.Set;

import org.cloudfoundry.multiapps.common.ContentException;
import org.cloudfoundry.multiapps.mta.handlers.v2.DescriptorHandler;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.NamedElement;
import org.cloudfoundry.multiapps.mta.model.RequiredDependency;
import org.cloudfoundry.multiapps.mta.resolvers.ProvidedValuesResolver;
import org.cloudfoundry.multiapps.mta.resolvers.ReferenceResolver;
import org.cloudfoundry.multiapps.mta.resolvers.ResolverBuilder;

public class RequiredDependencyReferenceResolver extends ReferenceResolver<RequiredDependency> {

    protected final RequiredDependency dependency;
    protected final ResolverBuilder propertiesResolverBuilder;

    public RequiredDependencyReferenceResolver(DeploymentDescriptor descriptor, NamedElement container, RequiredDependency dependency,
                                               String prefix, ResolverBuilder propertiesResolverBuilder,
                                               Set<String> dynamicResolvableParameters) {
        this(descriptor, container, dependency, prefix, new DescriptorHandler(), propertiesResolverBuilder, dynamicResolvableParameters);
    }

    public RequiredDependencyReferenceResolver(DeploymentDescriptor descriptor, NamedElement container, RequiredDependency dependency,
                                               String prefix, DescriptorHandler handler, ResolverBuilder propertiesResolverBuilder,
                                               Set<String> dynamicResolvableParameters) {
        super(dependency.getName(), prefix, handler, descriptor, container.getName(), SHORT, dynamicResolvableParameters);
        this.dependency = dependency;
        this.propertiesResolverBuilder = propertiesResolverBuilder;
    }

    @Override
    public RequiredDependency resolve() throws ContentException {
        boolean isStrict = dependency.getList() == null; // According to the MTA specification, if a
                                                         // reference that is part of a requires
                                                         // dependency with a list property, cannot
                                                         // be resolved, then its value should be
                                                         // set to 'null'.
        Map<String, Object> replacementValues = resolveProvidedValues(dependency.getName());
        Map<String, Object> resolvedProperties = resolve(dependency.getProperties(), replacementValues, isStrict);
        Map<String, Object> resolvedParameters = resolve(dependency.getParameters(), replacementValues, isStrict);
        dependency.setProperties(resolvedProperties);
        dependency.setParameters(resolvedParameters);
        return dependency;
    }

    @Override
    protected Map<String, Object> resolve(Map<String, Object> properties, final Map<String, Object> propertyValues, Boolean isStrict) {
        ProvidedValuesResolver valuesResolver = irrelevant -> propertyValues;
        return propertiesResolverBuilder.build(properties, valuesResolver, patternToMatch, prefix, isStrict, dynamicResolvableParameters)
                                        .resolve();
    }

}

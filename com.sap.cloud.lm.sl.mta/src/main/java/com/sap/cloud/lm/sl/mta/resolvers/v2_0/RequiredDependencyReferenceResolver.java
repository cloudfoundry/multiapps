package com.sap.cloud.lm.sl.mta.resolvers.v2_0;

import static com.sap.cloud.lm.sl.mta.resolvers.ReferencePattern.SHORT;

import java.util.Map;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.mta.handlers.v2_0.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.model.NamedElement;
import com.sap.cloud.lm.sl.mta.model.v2_0.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v2_0.RequiredDependency;
import com.sap.cloud.lm.sl.mta.resolvers.ProvidedValuesResolver;
import com.sap.cloud.lm.sl.mta.resolvers.ReferenceResolver;
import com.sap.cloud.lm.sl.mta.resolvers.ResolverBuilder;

public class RequiredDependencyReferenceResolver extends ReferenceResolver<RequiredDependency> {

    protected final RequiredDependency dependency;
    protected final ResolverBuilder propertiesResolverBuilder;

    public RequiredDependencyReferenceResolver(DeploymentDescriptor descriptor, NamedElement container, RequiredDependency dependency,
        String prefix, ResolverBuilder propertiesResolverBuilder) {
        this(descriptor, container, dependency, prefix, new DescriptorHandler(), propertiesResolverBuilder);
    }

    public RequiredDependencyReferenceResolver(DeploymentDescriptor descriptor, NamedElement container, RequiredDependency dependency,
        String prefix, DescriptorHandler handler, ResolverBuilder propertiesResolverBuilder) {
        super(dependency.getName(), prefix, handler, descriptor, container.getName(), SHORT);
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
    protected Map<String, Object> resolve(Map<String, Object> properties, final Map<String, Object> propertyValues, Boolean isStrict)
        throws ContentException {
        ProvidedValuesResolver valuesResolver = new ProvidedValuesResolver() {
            @Override
            public Map<String, Object> resolveProvidedValues(String irrelevant) {
                return propertyValues;
            }
        };
        return propertiesResolverBuilder.build(properties, valuesResolver, patternToMatch, prefix, isStrict)
            .resolve();
    }

}

package org.cloudfoundry.multiapps.mta.resolvers.v3;

import static org.cloudfoundry.multiapps.mta.resolvers.ReferencePattern.FULLY_QUALIFIED;

import java.util.Map;

import org.cloudfoundry.multiapps.mta.handlers.v3.DescriptorHandler;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.Resource;
import org.cloudfoundry.multiapps.mta.resolvers.ProvidedValuesResolver;
import org.cloudfoundry.multiapps.mta.resolvers.ReferenceResolver;
import org.cloudfoundry.multiapps.mta.resolvers.ResolverBuilder;

public class ResourcePropertiesReferenceResolver extends ReferenceResolver<Map<String, Object>> implements ProvidedValuesResolver {

    protected Map<String, Object> properties;
    protected ResolverBuilder propertiesResolverBuilder;

    public ResourcePropertiesReferenceResolver(DeploymentDescriptor descriptor, Resource resource, Map<String, Object> properties,
                                               String prefix, ResolverBuilder propertiesResolverBuilder) {
        super("", prefix, new DescriptorHandler(), descriptor, resource.getName(), FULLY_QUALIFIED);
        this.properties = properties;
        this.propertiesResolverBuilder = propertiesResolverBuilder;
    }

    @Override
    public Map<String, Object> resolve() {
        return propertiesResolverBuilder.build(properties, this, patternToMatch, prefix, true)
                                        .resolve();
    }
}

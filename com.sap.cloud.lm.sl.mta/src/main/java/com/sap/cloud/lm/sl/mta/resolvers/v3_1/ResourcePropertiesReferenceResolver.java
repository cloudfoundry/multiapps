package com.sap.cloud.lm.sl.mta.resolvers.v3_1;

import static com.sap.cloud.lm.sl.mta.resolvers.ReferencePattern.FULLY_QUALIFIED;

import java.util.Map;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.mta.handlers.v3_1.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.model.v3_1.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v3_1.Resource;
import com.sap.cloud.lm.sl.mta.resolvers.ProvidedValuesResolver;
import com.sap.cloud.lm.sl.mta.resolvers.ReferenceResolver;
import com.sap.cloud.lm.sl.mta.resolvers.ResolverBuilder;

public class ResourcePropertiesReferenceResolver extends ReferenceResolver<Map<String, Object>>
    implements ProvidedValuesResolver<ContentException> {

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
        return propertiesResolverBuilder.build(properties, this, patternToMatch, prefix, true).resolve();
    }
}

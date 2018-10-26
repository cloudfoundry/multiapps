package com.sap.cloud.lm.sl.mta.resolvers.v2;

import static com.sap.cloud.lm.sl.mta.resolvers.ReferencePattern.FULLY_QUALIFIED;

import java.util.Map;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.model.v2.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v2.Module;
import com.sap.cloud.lm.sl.mta.resolvers.ProvidedValuesResolver;
import com.sap.cloud.lm.sl.mta.resolvers.ReferenceResolver;
import com.sap.cloud.lm.sl.mta.resolvers.ResolverBuilder;

public class ModulePropertiesReferenceResolver extends ReferenceResolver<Map<String, Object>> implements ProvidedValuesResolver {

    protected final Map<String, Object> properties;
    protected final ResolverBuilder propertiesResolverBuilder;

    public ModulePropertiesReferenceResolver(DeploymentDescriptor descriptor, Module module, Map<String, Object> properties, String prefix,
        ResolverBuilder propertiesResolverBuilder) {
        super("", prefix, new DescriptorHandler(), descriptor, module.getName(), FULLY_QUALIFIED);
        this.properties = properties;
        this.propertiesResolverBuilder = propertiesResolverBuilder;
    }

    @Override
    public Map<String, Object> resolve() throws ContentException {
        return resolve(properties);
    }

    protected Map<String, Object> resolve(Map<String, Object> parameters) {
        return propertiesResolverBuilder.build(parameters, this, patternToMatch, prefix, true)
            .resolve();
    }

}

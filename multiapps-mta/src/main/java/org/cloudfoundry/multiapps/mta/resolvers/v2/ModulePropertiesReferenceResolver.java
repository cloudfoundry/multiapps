package org.cloudfoundry.multiapps.mta.resolvers.v2;

import static org.cloudfoundry.multiapps.mta.resolvers.ReferencePattern.FULLY_QUALIFIED;

import java.util.Map;

import org.cloudfoundry.multiapps.common.ContentException;
import org.cloudfoundry.multiapps.mta.handlers.v2.DescriptorHandler;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.Module;
import org.cloudfoundry.multiapps.mta.resolvers.ProvidedValuesResolver;
import org.cloudfoundry.multiapps.mta.resolvers.ReferenceResolver;
import org.cloudfoundry.multiapps.mta.resolvers.ResolverBuilder;

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

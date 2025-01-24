package org.cloudfoundry.multiapps.mta.resolvers;

import static java.text.MessageFormat.format;

import java.util.Map;
import java.util.Set;

import org.cloudfoundry.multiapps.common.ContentException;
import org.cloudfoundry.multiapps.mta.Messages;
import org.cloudfoundry.multiapps.mta.handlers.v2.DescriptorHandler;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.ProvidedDependency;
import org.cloudfoundry.multiapps.mta.model.RequiredDependency;
import org.cloudfoundry.multiapps.mta.model.Resource;

public abstract class ReferenceResolver<T> extends PatternResolver<T> {

    private DescriptorHandler handler;
    private String consumerName;
    private DeploymentDescriptor descriptor;

    public ReferenceResolver(String objectName, String prefix, DescriptorHandler handler, DeploymentDescriptor descriptor,
                             String consumerName, ReferencePattern patternToMatch, Set<String> dynamicResolvableParameters) {
        super(objectName, prefix, patternToMatch, dynamicResolvableParameters);
        this.handler = handler;
        this.consumerName = consumerName;
        this.descriptor = descriptor;
    }

    public Map<String, Object> resolveProvidedValues(String dependencyName) throws ContentException {
        Resource consumerResource = handler.findResource(descriptor, consumerName);
        if (!doesRequiredDependencyExists(dependencyName) && (consumerResource == null || !consumerResource.isOptional())) {
            throw new ContentException(format(Messages.ILLEGAL_REFERENCES_DETECTED, consumerName, dependencyName));
        }

        ProvidedDependency providedDependency = handler.findProvidedDependency(descriptor, dependencyName);
        if (providedDependency != null) {
            return providedDependency.getProperties();
        }
        Resource resource = handler.findResource(descriptor, dependencyName);
        if (resource == null && consumerResource.isOptional()) {
            return Map.of();
        }
        return resource.getProperties();
    }

    protected boolean doesRequiredDependencyExists(String dependencyName) {
        RequiredDependency requiredDependency = handler.findRequiredDependency(descriptor, consumerName, dependencyName);
        return requiredDependency != null;
    }
}

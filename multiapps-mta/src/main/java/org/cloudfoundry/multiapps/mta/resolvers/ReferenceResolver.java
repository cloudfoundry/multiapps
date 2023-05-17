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
        assertRequiredDependencyExists(dependencyName);
        ProvidedDependency providedDependency = handler.findProvidedDependency(descriptor, dependencyName);
        if (providedDependency != null) {
            return providedDependency.getProperties();
        }
        Resource resource = handler.findResource(descriptor, dependencyName);
        return resource.getProperties();
    }

    protected void assertRequiredDependencyExists(String dependencyName) {
        RequiredDependency requiredDependency = handler.findRequiredDependency(descriptor, consumerName, dependencyName);
        if (requiredDependency == null) {
            throw new ContentException(format(Messages.ILLEGAL_REFERENCES_DETECTED, consumerName, dependencyName));
        }
    }
}

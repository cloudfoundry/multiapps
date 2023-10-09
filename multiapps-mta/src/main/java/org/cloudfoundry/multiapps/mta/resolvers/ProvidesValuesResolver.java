package org.cloudfoundry.multiapps.mta.resolvers;

import static java.text.MessageFormat.format;

import java.util.Map;

import org.cloudfoundry.multiapps.common.ContentException;
import org.cloudfoundry.multiapps.mta.Messages;
import org.cloudfoundry.multiapps.mta.handlers.v3.DescriptorHandler;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.RequiredDependency;
import org.cloudfoundry.multiapps.mta.model.Resource;

public class ProvidesValuesResolver implements ProvidedValuesResolver {

    private DescriptorHandler handler;
    private String consumerName;
    private DeploymentDescriptor descriptor;

    public ProvidesValuesResolver(String consumerName, DescriptorHandler handler, DeploymentDescriptor descriptor) {

        this.handler = handler;
        this.consumerName = consumerName;
        this.descriptor = descriptor;
    }

    @Override
    public Map<String, Object> resolveProvidedValues(String dependencyName) throws ContentException {
        assertRequiredDependencyExists(dependencyName);
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

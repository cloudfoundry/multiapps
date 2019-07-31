package com.sap.cloud.lm.sl.mta.resolvers;

import static java.text.MessageFormat.format;

import java.util.Map;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.message.Messages;
import com.sap.cloud.lm.sl.mta.model.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.ProvidedDependency;
import com.sap.cloud.lm.sl.mta.model.RequiredDependency;
import com.sap.cloud.lm.sl.mta.model.Resource;

public abstract class ReferenceResolver<T> extends PatternResolver<T> {

    private DescriptorHandler handler;
    private String consumerName;
    private DeploymentDescriptor descriptor;

    public ReferenceResolver(String objectName, String prefix, DescriptorHandler handler, DeploymentDescriptor descriptor,
                             String consumerName, ReferencePattern patternToMatch) {
        super(objectName, prefix, patternToMatch);
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

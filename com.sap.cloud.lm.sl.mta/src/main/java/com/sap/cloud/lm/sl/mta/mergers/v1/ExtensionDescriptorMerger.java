package com.sap.cloud.lm.sl.mta.mergers.v1;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.ListUtils;

import com.sap.cloud.lm.sl.common.util.Pair;
import com.sap.cloud.lm.sl.mta.handlers.v1.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.model.ElementContext;
import com.sap.cloud.lm.sl.mta.model.PropertiesContainer;
import com.sap.cloud.lm.sl.mta.model.Visitor;
import com.sap.cloud.lm.sl.mta.model.v1.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v1.ExtensionDescriptor;
import com.sap.cloud.lm.sl.mta.model.v1.ExtensionModule;
import com.sap.cloud.lm.sl.mta.model.v1.ExtensionProvidedDependency;
import com.sap.cloud.lm.sl.mta.model.v1.ExtensionResource;
import com.sap.cloud.lm.sl.mta.model.v1.Module;
import com.sap.cloud.lm.sl.mta.model.v1.ProvidedDependency;
import com.sap.cloud.lm.sl.mta.model.v1.Resource;
import com.sap.cloud.lm.sl.mta.util.PropertiesUtil;

public class ExtensionDescriptorMerger extends Visitor {

    protected final ExtensionDescriptor extensionDescriptor;
    protected final DescriptorHandler handler;

    public ExtensionDescriptorMerger(ExtensionDescriptor extensionDescriptor, DescriptorHandler handler) {
        this.extensionDescriptor = extensionDescriptor;
        this.handler = handler;
    }

    public Pair<DeploymentDescriptor, List<String>> merge(Pair<DeploymentDescriptor, List<String>> descriptor) {
        List<String> targets = ListUtils.union(descriptor._2, extensionDescriptor.getDeployTargets());
        descriptor._1.accept(this);
        return new Pair<>(descriptor._1, targets);
    }

    @Override
    public void visit(ElementContext context, DeploymentDescriptor descriptor) {
        descriptor.setProperties(PropertiesUtil.mergeExtensionProperties(descriptor.getProperties(), extensionDescriptor.getProperties()));
    }

    @Override
    public void visit(ElementContext context, Module module) {
        ExtensionModule extension = handler.findModule(extensionDescriptor, module.getName());
        if (extension != null) {
            merge(module, extension);
        }
    }

    @Override
    public void visit(ElementContext context, ProvidedDependency providedDependency) {
        ExtensionProvidedDependency extension = handler.findProvidedDependency(extensionDescriptor, providedDependency.getName());
        if (extension != null) {
            merge(providedDependency, extension);
        }
    }

    @Override
    public void visit(ElementContext context, Resource resource) {
        ExtensionResource extension = handler.findResource(extensionDescriptor, resource.getName());
        if (extension != null) {
            merge(resource, extension);
        }
    }

    protected void merge(Module module, ExtensionModule extension) {
        module.setProperties(mergeProperties(module, extension));
    }

    protected Map<String, Object> mergeProperties(PropertiesContainer container, PropertiesContainer extensionContainer) {
        return PropertiesUtil.mergeExtensionProperties(container.getProperties(), extensionContainer.getProperties());
    }

    protected void merge(ProvidedDependency providedDependency, ExtensionProvidedDependency extension) {
        providedDependency.setProperties(mergeProperties(providedDependency, extension));
    }

    protected void merge(Resource resource, ExtensionResource extension) {
        resource.setProperties(mergeProperties(resource, extension));
    }
}

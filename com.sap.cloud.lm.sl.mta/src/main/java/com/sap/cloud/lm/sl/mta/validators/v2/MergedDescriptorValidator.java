package com.sap.cloud.lm.sl.mta.validators.v2;

import java.util.Set;
import java.util.TreeSet;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.model.ElementContext;
import com.sap.cloud.lm.sl.mta.model.Visitor;
import com.sap.cloud.lm.sl.mta.model.v2.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v2.Module;
import com.sap.cloud.lm.sl.mta.model.v2.ProvidedDependency;
import com.sap.cloud.lm.sl.mta.model.v2.RequiredDependency;
import com.sap.cloud.lm.sl.mta.model.v2.Resource;
import com.sap.cloud.lm.sl.mta.validators.DescriptorValidationRules;

public class MergedDescriptorValidator extends Visitor {

    protected final DeploymentDescriptor mergedDescriptor;
    protected final DescriptorHandler handler;
    protected final DescriptorValidationRules validationRules;
    protected final Set<String> emptyParameters = new TreeSet<>();

    public MergedDescriptorValidator(DeploymentDescriptor mergedDescriptor, DescriptorValidationRules validationRules,
        DescriptorHandler handler) {
        this.mergedDescriptor = mergedDescriptor;
        this.validationRules = validationRules;
        this.handler = handler;

    }
    
    public void validate() throws ContentException {
        mergedDescriptor.accept(this);
        validationRules.postValidate();
    }

    @Override
    public void visit(ElementContext context, DeploymentDescriptor descriptor) throws ContentException {
        validationRules.validateParameters((DeploymentDescriptor) descriptor, context);
    }

    @Override
    public void visit(ElementContext context, Module module) throws ContentException {
        validationRules.validateProperties(module, context);
        validationRules.validateParameters((Module) module, context);
    }

    @Override
    public void visit(ElementContext context, Resource resource) throws ContentException {
        validationRules.validateProperties(resource, context);
        validationRules.validateParameters(resource, context);
    }

    @Override
    public void visit(ElementContext context, RequiredDependency requiredDependency) throws ContentException {
        validationRules.validateProperties(requiredDependency, context);
        validationRules.validateParameters(requiredDependency, context);
    }

    @Override
    public void visit(ElementContext context, ProvidedDependency providedDependency) {
        validationRules.validateProperties(providedDependency, context);
    }
    
}

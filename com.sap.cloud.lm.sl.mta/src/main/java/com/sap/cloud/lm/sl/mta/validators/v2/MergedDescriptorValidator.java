package com.sap.cloud.lm.sl.mta.validators.v2;

import java.util.Set;
import java.util.TreeSet;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.model.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.ElementContext;
import com.sap.cloud.lm.sl.mta.model.Module;
import com.sap.cloud.lm.sl.mta.model.ProvidedDependency;
import com.sap.cloud.lm.sl.mta.model.RequiredDependency;
import com.sap.cloud.lm.sl.mta.model.Resource;
import com.sap.cloud.lm.sl.mta.model.Visitor;
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
        validationRules.validateParameters(context, descriptor);
    }

    @Override
    public void visit(ElementContext context, Module module) throws ContentException {
        validationRules.validateProperties(context, module);
        validationRules.validateParameters(context, module);
    }

    @Override
    public void visit(ElementContext context, Resource resource) throws ContentException {
        validationRules.validateProperties(context, resource);
        validationRules.validateParameters(context, resource);
    }

    @Override
    public void visit(ElementContext context, RequiredDependency requiredDependency) throws ContentException {
        validationRules.validateProperties(context, requiredDependency);
        validationRules.validateParameters(context, requiredDependency);
    }

    @Override
    public void visit(ElementContext context, ProvidedDependency providedDependency) {
        validationRules.validateProperties(context, providedDependency);
        validationRules.validateParameters(context, providedDependency);
    }

}

package com.sap.cloud.lm.sl.mta.validators.v2;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.common.util.Pair;
import com.sap.cloud.lm.sl.mta.handlers.v1.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.model.ElementContext;
import com.sap.cloud.lm.sl.mta.model.v2.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v2.Module;
import com.sap.cloud.lm.sl.mta.model.v2.RequiredDependency;
import com.sap.cloud.lm.sl.mta.model.v2.Resource;
import com.sap.cloud.lm.sl.mta.validators.DescriptorValidationRules;

public class MergedDescriptorValidator extends com.sap.cloud.lm.sl.mta.validators.v1.MergedDescriptorValidator {

    protected final Set<String> emptyParameters = new TreeSet<String>();

    public MergedDescriptorValidator(Pair<com.sap.cloud.lm.sl.mta.model.v1.DeploymentDescriptor, List<String>> mergedDescriptor,
        DescriptorValidationRules validationRules, DescriptorHandler handler) {
        super(mergedDescriptor, validationRules, handler);
    }

    @Override
    public void visit(ElementContext context, com.sap.cloud.lm.sl.mta.model.v1.DeploymentDescriptor descriptor) throws ContentException {
        validationRules.validateParameters((DeploymentDescriptor) descriptor, context);
    }

    @Override
    public void visit(ElementContext context, com.sap.cloud.lm.sl.mta.model.v1.Module module) throws ContentException {
        super.visit(context, module);
        validationRules.validateParameters((Module) module, context);
    }

    @Override
    public void visit(ElementContext context, com.sap.cloud.lm.sl.mta.model.v1.Resource resource) throws ContentException {
        super.visit(context, resource);
        validationRules.validateParameters((Resource) resource, context);
    }

    @Override
    public void visit(ElementContext context, RequiredDependency requiredDependency) throws ContentException {
        validationRules.validateProperties(requiredDependency, context);
        validationRules.validateParameters(requiredDependency, context);
    }

}

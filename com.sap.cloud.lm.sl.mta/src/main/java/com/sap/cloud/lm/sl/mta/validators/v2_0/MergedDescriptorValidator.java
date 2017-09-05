package com.sap.cloud.lm.sl.mta.validators.v2_0;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.common.util.Pair;
import com.sap.cloud.lm.sl.mta.handlers.v1_0.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.model.ElementContext;
import com.sap.cloud.lm.sl.mta.model.v2_0.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v2_0.Module;
import com.sap.cloud.lm.sl.mta.model.v2_0.RequiredDependency;
import com.sap.cloud.lm.sl.mta.model.v2_0.Resource;
import com.sap.cloud.lm.sl.mta.validators.DescriptorValidationRules;

public class MergedDescriptorValidator extends com.sap.cloud.lm.sl.mta.validators.v1_0.MergedDescriptorValidator {

    protected final Set<String> emptyParameters = new TreeSet<String>();

    public MergedDescriptorValidator(Pair<com.sap.cloud.lm.sl.mta.model.v1_0.DeploymentDescriptor, List<String>> mergedDescriptor,
        DescriptorValidationRules validationRules, DescriptorHandler handler) {
        super(mergedDescriptor, validationRules, handler);
    }

    @Override
    public void visit(ElementContext context, com.sap.cloud.lm.sl.mta.model.v1_0.DeploymentDescriptor descriptor) throws ContentException {
        validationRules.validateParameters((DeploymentDescriptor) descriptor, context);
    }

    @Override
    public void visit(ElementContext context, com.sap.cloud.lm.sl.mta.model.v1_0.Module module) throws ContentException {
        super.visit(context, module);
        validationRules.validateParameters((Module) module, context);
    }

    @Override
    public void visit(ElementContext context, com.sap.cloud.lm.sl.mta.model.v1_0.Resource resource) throws ContentException {
        super.visit(context, resource);
        validationRules.validateParameters((Resource) resource, context);
    }

    @Override
    public void visit(ElementContext context, RequiredDependency requiredDependency) throws ContentException {
        validationRules.validateProperties(requiredDependency, context);
        validationRules.validateParameters(requiredDependency, context);
    }

}

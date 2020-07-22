package org.cloudfoundry.multiapps.mta.validators.v2;

import org.cloudfoundry.multiapps.common.ContentException;
import org.cloudfoundry.multiapps.mta.handlers.v2.DescriptorHandler;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.ElementContext;
import org.cloudfoundry.multiapps.mta.model.Module;
import org.cloudfoundry.multiapps.mta.model.ProvidedDependency;
import org.cloudfoundry.multiapps.mta.model.RequiredDependency;
import org.cloudfoundry.multiapps.mta.model.Resource;
import org.cloudfoundry.multiapps.mta.model.Visitor;
import org.cloudfoundry.multiapps.mta.validators.DescriptorValidationRules;

public class MergedDescriptorValidator extends Visitor {

    protected final DeploymentDescriptor mergedDescriptor;
    protected final DescriptorHandler handler;
    protected final DescriptorValidationRules validationRules;

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

package com.sap.cloud.lm.sl.mta.validators.v1;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.common.util.Pair;
import com.sap.cloud.lm.sl.mta.handlers.v1.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.message.Messages;
import com.sap.cloud.lm.sl.mta.model.ElementContext;
import com.sap.cloud.lm.sl.mta.model.Visitor;
import com.sap.cloud.lm.sl.mta.model.v1.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v1.Module;
import com.sap.cloud.lm.sl.mta.model.v1.ProvidedDependency;
import com.sap.cloud.lm.sl.mta.model.v1.Resource;
import com.sap.cloud.lm.sl.mta.validators.DescriptorValidationRules;

public class MergedDescriptorValidator extends Visitor {

    protected final Set<String> emptyProperties = new TreeSet<>();

    protected final Pair<DeploymentDescriptor, List<String>> mergedDescriptor;
    protected final DescriptorHandler handler;
    protected final DescriptorValidationRules validationRules;

    public MergedDescriptorValidator(Pair<DeploymentDescriptor, List<String>> mergedDescriptor, DescriptorValidationRules validationRules,
        DescriptorHandler handler) {
        this.mergedDescriptor = mergedDescriptor;
        this.handler = handler;
        this.validationRules = validationRules;
    }

    public void validate(String platform) throws ContentException {
        mergedDescriptor._1.accept(this);
        validateTarget(platform);
        validationRules.postValidate();
    }

    @Override
    public void visit(ElementContext context, DeploymentDescriptor descriptor) throws ContentException {
        validationRules.validateProperties(descriptor, context);
    }

    @Override
    public void visit(ElementContext context, Module module) throws ContentException {
        validationRules.validateProperties(module, context);
    }

    @Override
    public void visit(ElementContext context, ProvidedDependency providedDependency) {
        validationRules.validateProperties(providedDependency, context);
    }

    @Override
    public void visit(ElementContext context, Resource resource) throws ContentException {
        validationRules.validateProperties(resource, context);

    }

    protected void validateTarget(String target) {
        if (!targetIsListed(target)) {
            throw new ContentException(Messages.DEPLOY_TARGET_NOT_LISTED, target);
        }
    }

    protected boolean targetIsListed(String targetName) {
        List<String> targets = mergedDescriptor._2;
        return targets.contains(targetName) || targets.size() == 0;
    }
}

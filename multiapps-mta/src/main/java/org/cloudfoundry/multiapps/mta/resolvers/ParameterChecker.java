package org.cloudfoundry.multiapps.mta.resolvers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.cloudfoundry.multiapps.mta.helpers.SimplePropertyVisitor;
import org.cloudfoundry.multiapps.mta.helpers.VisitableObject;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.ElementContext;
import org.cloudfoundry.multiapps.mta.model.Hook;
import org.cloudfoundry.multiapps.mta.model.Module;
import org.cloudfoundry.multiapps.mta.model.ParametersContainer;
import org.cloudfoundry.multiapps.mta.model.ProvidedDependency;
import org.cloudfoundry.multiapps.mta.model.RequiredDependency;
import org.cloudfoundry.multiapps.mta.model.Resource;
import org.cloudfoundry.multiapps.mta.model.Visitor;

public abstract class ParameterChecker extends Visitor implements SimplePropertyVisitor {
    private List<String> matchedParameters;

    private Set<String> currentParameters;

    protected abstract Set<String> getModuleParametersToMatch();

    protected abstract Set<String> getModuleHookParametersToMatch();

    protected abstract Set<String> getResourceParametersToMatch();

    protected abstract Set<String> getGlobalParametersToMatch();

    protected abstract Set<String> getDependencyParametersToMatch();

    public List<String> getCustomParameters(DeploymentDescriptor descriptor) {
        this.matchedParameters = new ArrayList<>();
        descriptor.accept(this);
        return matchedParameters;
    }

    @Override
    public void visit(ElementContext context, DeploymentDescriptor descriptor) {
        currentParameters = getGlobalParametersToMatch();
        findMatchesInParameters(descriptor);
    }

    @Override
    public void visit(ElementContext context, Module module) {
        currentParameters = getModuleParametersToMatch();
        findMatchesInParameters(module);
    }

    @Override
    public void visit(ElementContext context, ProvidedDependency providedDependency) {
        currentParameters = getDependencyParametersToMatch();
        findMatchesInParameters(providedDependency);
    }

    @Override
    public void visit(ElementContext context, RequiredDependency requiredDependency) {
        currentParameters = getDependencyParametersToMatch();
        findMatchesInParameters(requiredDependency);
    }

    @Override
    public void visit(ElementContext context, Resource resource) {
        currentParameters = getResourceParametersToMatch();
        findMatchesInParameters(resource);
    }

    @Override
    public void visit(ElementContext context, Hook hook) {
        currentParameters = getModuleHookParametersToMatch();
        findMatchesInParameters(hook);
    }

    private void findMatchesInParameters(ParametersContainer parametersContainer) {
        findMatchesInKeySet(parametersContainer.getParameters()
                                               .keySet());
    }

    private void findMatchesInKeySet(Set<String> keys) {
        new VisitableObject(keys).accept(this);
    }

    @Override
    public Object visit(String key, String value) {
        parameterKeyMatches(value);
        return value;
    }

    private void parameterKeyMatches(String valueToMatch) {
        if (currentParameters != null && !currentParameters.contains(valueToMatch)) {
            matchedParameters.add(valueToMatch);
        }
    }

}

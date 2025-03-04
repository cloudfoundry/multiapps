package org.cloudfoundry.multiapps.mta.resolvers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
    private List<CustomParameterContainer> unsupportedParameters;

    private Set<String> predefinedParametersForContainer;

    protected abstract Set<String> getModuleParametersToMatch();

    protected abstract Set<String> getModuleHookParametersToMatch();

    protected abstract Set<String> getResourceParametersToMatch();

    protected abstract Set<String> getGlobalParametersToMatch();

    protected abstract Set<String> getDependencyParametersToMatch();

    public List<CustomParameterContainer> getCustomParameters(DeploymentDescriptor descriptor) {
        this.unsupportedParameters = new ArrayList<>();
        descriptor.accept(this);
        return this.unsupportedParameters;
    }

    @Override
    public void visit(ElementContext context, DeploymentDescriptor descriptor) {
        matchAndStore(null, getGlobalParametersToMatch(), descriptor, null);
    }

    @Override
    public void visit(ElementContext context, Module module) {
        matchAndStore(module.getName(), getModuleParametersToMatch(), module, context.getPrefixedName());
    }

    @Override
    public void visit(ElementContext context, ProvidedDependency providedDependency) {
        matchAndStore(providedDependency.getName(), getDependencyParametersToMatch(), providedDependency, context.getPrefixedName());
    }

    @Override
    public void visit(ElementContext context, RequiredDependency requiredDependency) {
        matchAndStore(requiredDependency.getName(), getDependencyParametersToMatch(), requiredDependency, context.getPrefixedName());
    }

    @Override
    public void visit(ElementContext context, Resource resource) {
        matchAndStore(resource.getName(), getResourceParametersToMatch(), resource, context.getPrefixedName());
    }

    @Override
    public void visit(ElementContext context, Hook hook) {
        matchAndStore(hook.getName(), getModuleHookParametersToMatch(), hook, context.getPrefixedName());
    }

    private void matchAndStore(String name, Set<String> parametersToMatch, ParametersContainer parametersContainer, String prefixedName) {
        this.predefinedParametersForContainer = parametersToMatch;
        List<String> unmatched = findMatchesInParameters(parametersContainer);
        if (!unmatched.isEmpty()) {
            unsupportedParameters.add(new CustomParameterContainer(name, unmatched, prefixedName));
        }
    }

    private List<String> findMatchesInParameters(ParametersContainer parametersContainer) {
        List<String> matchedKeys = (List<String>) findMatchesInKeySet(parametersContainer.getParameters()
                                                                                         .keySet());
        return matchedKeys.stream()
                          .filter(Objects::nonNull)
                          .collect(Collectors.toList());
    }

    private Object findMatchesInKeySet(Set<String> keys) {
        return new VisitableObject(keys).accept(this);
    }

    @Override
    public Object visit(String key, String value) {
        return parameterKeyMatches(value);
    }

    private String parameterKeyMatches(String valueToMatch) {
        if (predefinedParametersForContainer != null && !predefinedParametersForContainer.contains(valueToMatch)) {
            return valueToMatch;
        }
        return null;
    }

}

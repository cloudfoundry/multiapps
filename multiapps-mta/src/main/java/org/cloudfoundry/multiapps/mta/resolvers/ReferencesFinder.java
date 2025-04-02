package org.cloudfoundry.multiapps.mta.resolvers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.cloudfoundry.multiapps.common.util.MiscUtil;
import org.cloudfoundry.multiapps.mta.helpers.SimplePropertyVisitor;
import org.cloudfoundry.multiapps.mta.helpers.VisitableObject;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.ElementContext;
import org.cloudfoundry.multiapps.mta.model.Hook;
import org.cloudfoundry.multiapps.mta.model.Module;
import org.cloudfoundry.multiapps.mta.model.ParametersContainer;
import org.cloudfoundry.multiapps.mta.model.PropertiesContainer;
import org.cloudfoundry.multiapps.mta.model.ProvidedDependency;
import org.cloudfoundry.multiapps.mta.model.RequiredDependency;
import org.cloudfoundry.multiapps.mta.model.Resource;
import org.cloudfoundry.multiapps.mta.model.Visitor;

public class ReferencesFinder extends Visitor implements SimplePropertyVisitor {

    private Set<String> foundReferences;

    public void fillWithReferences(DeploymentDescriptor descriptor, Set<String> referenceContainer) {
        this.foundReferences = referenceContainer;
        descriptor.accept(this);
    }

    @Override
    public void visit(ElementContext context, DeploymentDescriptor descriptor) {
        findReferencesInParameters(descriptor);
    }

    @Override
    public void visit(ElementContext context, Module module) {
        findReferencesInProperties(module);
        findReferencesInParameters(module);
    }

    @Override
    public void visit(ElementContext context, ProvidedDependency providedDependency) {
        findReferencesInProperties(providedDependency);
        findReferencesInParameters(providedDependency);
    }

    @Override
    public void visit(ElementContext context, RequiredDependency requiredDependency) {
        findReferencesInProperties(requiredDependency);
        findReferencesInParameters(requiredDependency);
    }

    @Override
    public void visit(ElementContext context, Resource resource) {
        findReferencesInProperties(resource);
        findReferencesInParameters(resource);
    }

    @Override
    public void visit(ElementContext context, Hook hook) {
        findReferencesInParameters(hook);
    }

    private void findReferencesInProperties(PropertiesContainer propertiesContainer) {
        findReferencesInMap(propertiesContainer.getProperties());
    }

    private void findReferencesInParameters(ParametersContainer parametersContainer) {
        findReferencesInMap(parametersContainer.getParameters());
    }

    private void findReferencesInMap(Map<String, Object> map) {
        Map<String, Object> modifiableMap = new HashMap<>(map);
        new VisitableObject(modifiableMap).accept(this);
    }

    @Override
    public Object visit(String key, String value) {
        referencePatternMatches(value);
        return value;
    }

    private void referencePatternMatches(String valueToMatch) {
        for (Pattern compiledPattern : ReferencePattern.COMPILED_PATTERNS) {
            Matcher matcher = compiledPattern.matcher(MiscUtil.cast(valueToMatch));
            while (matcher.find()) {
                for (int i = 1; i <= matcher.groupCount(); i++) {
                    String reference = matcher.group(i);
                    foundReferences.add(reference);
                }
            }
        }
    }

}

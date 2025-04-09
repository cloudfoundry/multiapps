package org.cloudfoundry.multiapps.mta.resolvers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cloudfoundry.multiapps.mta.helpers.SimplePropertyVisitor;
import org.cloudfoundry.multiapps.mta.helpers.VisitableObject;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.ElementContext;
import org.cloudfoundry.multiapps.mta.model.Hook;
import org.cloudfoundry.multiapps.mta.model.Module;
import org.cloudfoundry.multiapps.mta.model.ProvidedDependency;
import org.cloudfoundry.multiapps.mta.model.RequiredDependency;
import org.cloudfoundry.multiapps.mta.model.Resource;
import org.cloudfoundry.multiapps.mta.model.Visitor;

public class ReferencesFinder extends Visitor implements SimplePropertyVisitor {

    private Set<String> foundReferences;

    public Set<String> getAllReferences(DeploymentDescriptor descriptor) {
        this.foundReferences = new HashSet<>();
        descriptor.accept(this);
        return this.foundReferences;
    }

    @Override
    public void visit(ElementContext context, DeploymentDescriptor descriptor) {
        findReferencesInMap(descriptor.getParameters());
    }

    @Override
    public void visit(ElementContext context, Module module) {
        findReferencesInMap(module.getParameters());
        findReferencesInMap(module.getProperties());
    }

    @Override
    public void visit(ElementContext context, ProvidedDependency providedDependency) {
        findReferencesInMap(providedDependency.getParameters());
        findReferencesInMap(providedDependency.getProperties());
    }

    @Override
    public void visit(ElementContext context, RequiredDependency requiredDependency) {
        findReferencesInMap(requiredDependency.getParameters());
        findReferencesInMap(requiredDependency.getProperties());
    }

    @Override
    public void visit(ElementContext context, Resource resource) {
        findReferencesInMap(resource.getParameters());
        findReferencesInMap(resource.getProperties());
    }

    @Override
    public void visit(ElementContext context, Hook hook) {
        findReferencesInMap(hook.getParameters());
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
        for (ReferencePattern pattern : ReferencePattern.values()) {
            List<Reference> references = pattern.match(valueToMatch);
            for (Reference reference : references) {
                foundReferences.add(reference.getKey());
            }
        }
    }

}

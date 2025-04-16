package org.cloudfoundry.multiapps.mta.resolvers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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

    private List<ReferenceContainer> foundReferences;

    public List<ReferenceContainer> getAllReferences(DeploymentDescriptor descriptor) {
        this.foundReferences = new ArrayList<>();
        descriptor.accept(this);
        return this.foundReferences;
    }

    @Override
    public void visit(ElementContext context, DeploymentDescriptor descriptor) {
        collectReferences(null, descriptor.getParameters());
    }

    @Override
    public void visit(ElementContext context, Module module) {
        collectReferences(module.getName(), module.getParameters(), module.getProperties());
    }

    @Override
    public void visit(ElementContext context, ProvidedDependency providedDependency) {
        collectReferences(context.getPrefixedName(), providedDependency.getParameters(), providedDependency.getProperties());
    }

    @Override
    public void visit(ElementContext context, RequiredDependency requiredDependency) {
        collectReferences(context.getPrefixedName(), requiredDependency.getParameters(), requiredDependency.getProperties());
    }

    @Override
    public void visit(ElementContext context, Resource resource) {
        collectReferences(resource.getName(), resource.getParameters(), resource.getProperties());
    }

    @Override
    public void visit(ElementContext context, Hook hook) {
        collectReferences(context.getPrefixedName(), hook.getParameters());
    }

    private void collectReferences(String name, Map<String, Object>... maps) {
        List<Reference> merged = Arrays.stream(maps)
                                       .flatMap(map -> findReferencesInParameters(map).stream())
                                       .collect(Collectors.toList());

        if (!merged.isEmpty()) {
            foundReferences.add(new ReferenceContainer(name, merged));
        }
    }

    private List<Reference> findReferencesInParameters(Map<String, Object> map) {
        if (map == null) {
            return Collections.emptyList();
        }

        List<Reference> matchedKeys = (List<Reference>) findReferencesInList(new ArrayList(map.values()));
        return matchedKeys.stream()
                          .filter(Objects::nonNull)
                          .collect(Collectors.toList());
    }

    private Object findReferencesInList(List<String> keys) {
        Object result = new VisitableObject(keys).accept(this);
        return extractReferences(result);
    }

    private List<Reference> extractReferences(Object input) {
        if (input == null) {
            return Collections.emptyList();
        }

        if (input instanceof Reference) {
            return List.of((Reference) input);
        }

        if (input instanceof List<?>) {
            return ((List<?>) input).stream()
                                    .flatMap(item -> extractReferences(item).stream())
                                    .collect(Collectors.toList());
        }

        if (input instanceof Map<?, ?>) {
            return ((Map<?, ?>) input).values()
                                      .stream()
                                      .flatMap(value -> extractReferences(value).stream())
                                      .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    @Override
    public Object visit(String key, String value) {
        return referencePatternMatches(value);
    }

    private List<Reference> referencePatternMatches(String valueToMatch) {
        //Not using ReferencePattern.values() in order to have the more specific FULLY_QUALIFIED pattern before SHORT
        for (ReferencePattern pattern : List.of(
            ReferencePattern.PLACEHOLDER,
            ReferencePattern.FULLY_QUALIFIED,
            ReferencePattern.SHORT)) {

            List<Reference> references = pattern.match(valueToMatch);
            if (!references.isEmpty()) {
                return references;
            }
        }
        return null;
    }

}

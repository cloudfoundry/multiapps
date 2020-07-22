package org.cloudfoundry.multiapps.mta.resolvers;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

public class ReferencesUnescaper extends Visitor implements SimplePropertyVisitor {

    private static final List<String> REFERENCE_PATTERNS = Arrays.asList("\\\\(\\$\\{.+?\\})", "\\\\(\\~\\{.+?\\})");
    private static final String ESCAPED_STRING_REPLACEMENT = "$1";

    public void unescapeReferences(DeploymentDescriptor descriptor) {
        descriptor.accept(this);
    }

    @Override
    public void visit(ElementContext context, DeploymentDescriptor descriptor) {
        unescapeReferencesInParameters(descriptor);
    }

    @Override
    public void visit(ElementContext context, Module module) {
        unescapeReferencesInProperties(module);
        unescapeReferencesInParameters(module);
    }

    @Override
    public void visit(ElementContext context, ProvidedDependency providedDependency) {
        unescapeReferencesInProperties(providedDependency);
        unescapeReferencesInParameters(providedDependency);
    }

    @Override
    public void visit(ElementContext context, RequiredDependency requiredDependency) {
        unescapeReferencesInProperties(requiredDependency);
        unescapeReferencesInParameters(requiredDependency);
    }

    @Override
    public void visit(ElementContext context, Resource resource) {
        unescapeReferencesInProperties(resource);
        unescapeReferencesInParameters(resource);
    }

    @Override
    public void visit(ElementContext context, Hook hook) {
        unescapeReferencesInParameters(hook);
    }

    private void unescapeReferencesInProperties(PropertiesContainer propertiesContainer) {
        Map<String, Object> unescapedProperties = unescapeReferencesInMap(propertiesContainer.getProperties());
        propertiesContainer.setProperties(unescapedProperties);
    }

    private void unescapeReferencesInParameters(ParametersContainer parametersContainer) {
        Map<String, Object> unescapedProperties = unescapeReferencesInMap(parametersContainer.getParameters());
        parametersContainer.setParameters(unescapedProperties);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> unescapeReferencesInMap(Map<String, Object> map) {
        Map<String, Object> modifiableMap = new LinkedHashMap<>(map);
        return (Map<String, Object>) new VisitableObject(modifiableMap).accept(this);
    }

    @Override
    public Object visit(String key, String value) {
        for (String referencePattern : REFERENCE_PATTERNS) {
            value = unescape(value, referencePattern);
        }
        return value;
    }

    private static String unescape(String value, String escapedStringRegex) {
        return value.replaceAll(escapedStringRegex, ESCAPED_STRING_REPLACEMENT);
    }

}

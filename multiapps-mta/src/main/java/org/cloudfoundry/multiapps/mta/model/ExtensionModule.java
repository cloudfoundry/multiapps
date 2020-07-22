package org.cloudfoundry.multiapps.mta.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.cloudfoundry.multiapps.common.util.yaml.YamlElement;
import org.cloudfoundry.multiapps.mta.parsers.v2.ExtensionModuleParser;
import org.cloudfoundry.multiapps.mta.parsers.v3.ModuleParser;

public class ExtensionModule extends VersionedEntity implements VisitableElement, NamedElement, PropertiesContainer, ParametersContainer {

    @YamlElement(ExtensionModuleParser.NAME)
    private String name;
    @YamlElement(ExtensionModuleParser.PROPERTIES)
    private Map<String, Object> properties = Collections.emptyMap();
    @YamlElement(ExtensionModuleParser.PARAMETERS)
    private Map<String, Object> parameters = Collections.emptyMap();
    @YamlElement(ExtensionModuleParser.REQUIRES)
    private List<ExtensionRequiredDependency> requiredDependencies = Collections.emptyList();
    @YamlElement(ExtensionModuleParser.PROVIDES)
    private List<ExtensionProvidedDependency> providedDependencies = Collections.emptyList();
    @YamlElement(ModuleParser.HOOKS)
    private List<ExtensionHook> hooks = Collections.emptyList();

    // Required by Jackson.
    protected ExtensionModule() {
        super(0);
    }

    protected ExtensionModule(int majorSchemaVersion) {
        super(majorSchemaVersion);
    }

    public static ExtensionModule createV2() {
        return new ExtensionModule(2);
    }

    public static ExtensionModule createV3() {
        return new ExtensionModule(3);
    }

    public String getName() {
        return name;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public List<ExtensionRequiredDependency> getRequiredDependencies() {
        return requiredDependencies;
    }

    public List<ExtensionProvidedDependency> getProvidedDependencies() {
        return providedDependencies;
    }

    public List<ExtensionHook> getHooks() {
        return hooks;
    }

    public ExtensionModule setName(String name) {
        this.name = ObjectUtils.defaultIfNull(name, this.name);
        return this;
    }

    public ExtensionModule setProperties(Map<String, Object> properties) {
        this.properties = ObjectUtils.defaultIfNull(properties, this.properties);
        return this;
    }

    public ExtensionModule setParameters(Map<String, Object> parameters) {
        this.parameters = ObjectUtils.defaultIfNull(parameters, this.parameters);
        return this;
    }

    public ExtensionModule setRequiredDependencies(List<ExtensionRequiredDependency> requiredDependencies) {
        this.requiredDependencies = ObjectUtils.defaultIfNull(requiredDependencies, this.requiredDependencies);
        return this;
    }

    public ExtensionModule setProvidedDependencies(List<ExtensionProvidedDependency> providedDependencies) {
        this.providedDependencies = ObjectUtils.defaultIfNull(providedDependencies, this.providedDependencies);
        return this;
    }

    public ExtensionModule setHooks(List<ExtensionHook> hooks) {
        this.hooks = ObjectUtils.defaultIfNull(hooks, this.hooks);
        return this;
    }

    @Override
    public void accept(ElementContext context, Visitor visitor) {
        visitor.visit(context, this);
        for (ExtensionProvidedDependency providedDependency : getProvidedDependencies()) {
            providedDependency.accept(new ElementContext(providedDependency, context), visitor);
        }

        for (ExtensionRequiredDependency requiredDependency : getRequiredDependencies()) {
            requiredDependency.accept(new ElementContext(requiredDependency, context), visitor);
        }
    }

}

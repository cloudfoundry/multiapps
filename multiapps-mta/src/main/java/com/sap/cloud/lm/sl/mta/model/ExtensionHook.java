package com.sap.cloud.lm.sl.mta.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;

import com.sap.cloud.lm.sl.common.util.yaml.YamlElement;
import com.sap.cloud.lm.sl.mta.parsers.v3.ExtensionHookParser;

public class ExtensionHook extends VersionedEntity implements VisitableElement, NamedElement {

    // Required by Jackson.
    protected ExtensionHook() {
        super(0);
    }

    protected ExtensionHook(int majorSchemaVersion) {
        super(majorSchemaVersion);
    }

    @YamlElement(ExtensionHookParser.NAME)
    private String name;
    @YamlElement(ExtensionHookParser.PARAMETERS)
    private Map<String, Object> parameters = Collections.emptyMap();
    @YamlElement(ExtensionHookParser.REQUIRES)
    private List<ExtensionRequiredDependency> requiredDependencies = Collections.emptyList();

    public static ExtensionHook createV3() {
        return new ExtensionHook(3);
    }

    @Override
    public String getName() {
        return name;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public List<ExtensionRequiredDependency> getRequiredDependencies() {
        return requiredDependencies;
    }

    public ExtensionHook setName(String name) {
        this.name = ObjectUtils.defaultIfNull(name, this.name);
        return this;
    }

    public ExtensionHook setParameters(Map<String, Object> parameters) {
        this.parameters = ObjectUtils.defaultIfNull(parameters, this.parameters);
        return this;
    }

    public ExtensionHook setRequiredDependencies(List<ExtensionRequiredDependency> requiredDependencies) {
        this.requiredDependencies = ObjectUtils.defaultIfNull(requiredDependencies, this.requiredDependencies);
        return this;
    }

    @Override
    public void accept(ElementContext context, Visitor visitor) {
        visitor.visit(context, this);
        for (ExtensionRequiredDependency requiredDependency : requiredDependencies) {
            requiredDependency.accept(new ElementContext(requiredDependency, context), visitor);
        }
    }

}

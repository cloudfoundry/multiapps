package com.sap.cloud.lm.sl.mta.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;

import com.sap.cloud.lm.sl.common.util.yaml.YamlElement;
import com.sap.cloud.lm.sl.mta.parsers.v3.HookParser;

public class Hook extends VersionedEntity implements VisitableElement, NamedElement, ParametersContainer {

    // Required by Jackson.
    protected Hook() {
        super(0);
    }

    protected Hook(int majorSchemaVersion) {
        super(majorSchemaVersion);
    }

    public static Hook copyOf(Hook original) {
        Hook copy = new Hook(original.majorSchemaVersion);
        copy.name = original.name;
        copy.type = original.type;
        copy.phases = original.phases;
        copy.parameters = new TreeMap<>(original.parameters);
        copy.requiredDependencies = copyRequiredDependencies(original.requiredDependencies);
        return copy;
    }

    private static List<RequiredDependency> copyRequiredDependencies(List<RequiredDependency> originals) {
        return originals.stream()
                        .map(RequiredDependency::copyOf)
                        .collect(Collectors.toList());
    }

    @YamlElement(HookParser.NAME)
    private String name;
    @YamlElement(HookParser.TYPE)
    private String type;
    @YamlElement(HookParser.PHASES)
    private List<String> phases = Collections.emptyList();
    @YamlElement(HookParser.PARAMETERS)
    private Map<String, Object> parameters = Collections.emptyMap();
    @YamlElement(HookParser.REQUIRES)
    private List<RequiredDependency> requiredDependencies = Collections.emptyList();

    public static Hook createV3() {
        return new Hook(3);
    }

    @Override
    public String getName() {
        return name;
    }

    public List<String> getPhases() {
        return phases;
    }

    public String getType() {
        return type;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public List<RequiredDependency> getRequiredDependencies() {
        return requiredDependencies;
    }

    public Hook setName(String name) {
        this.name = ObjectUtils.defaultIfNull(name, this.name);
        return this;
    }

    public Hook setPhases(List<String> phases) {
        this.phases = ObjectUtils.defaultIfNull(phases, this.phases);
        return this;
    }

    public Hook setType(String type) {
        this.type = ObjectUtils.defaultIfNull(type, this.type);
        return this;
    }

    public Hook setParameters(Map<String, Object> parameters) {
        this.parameters = ObjectUtils.defaultIfNull(parameters, this.parameters);
        return this;
    }

    public Hook setRequiredDependencies(List<RequiredDependency> requiredDependencies) {
        this.requiredDependencies = ObjectUtils.defaultIfNull(requiredDependencies, this.requiredDependencies);
        return this;
    }

    @Override
    public void accept(ElementContext context, Visitor visitor) {
        visitor.visit(context, this);
        for (RequiredDependency requiredDependency : requiredDependencies) {
            requiredDependency.accept(new ElementContext(requiredDependency, context), visitor);
        }
    }

}

package com.sap.cloud.lm.sl.mta.parsers.v3;

import static com.sap.cloud.lm.sl.mta.handlers.v3.Schemas.HOOK;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.Hook;
import com.sap.cloud.lm.sl.mta.model.RequiredDependency;
import com.sap.cloud.lm.sl.mta.parsers.ListParser;
import com.sap.cloud.lm.sl.mta.parsers.ModelParser;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class HookParser extends ModelParser<Hook> {

    protected static final String HOOK_OBJECT_NAME = "MTA hook";

    public static final String NAME = "name";
    public static final String TYPE = "type";
    public static final String PHASES = "phases";
    public static final String PARAMETERS = "parameters";
    public static final String REQUIRES = "requires";

    protected final Set<String> usedRequiredDependencyNames = new HashSet<>();

    public HookParser(Map<String, Object> source) {
        this(HOOK, source);
    }

    public HookParser(MapElement schema, Map<String, Object> source) {
        super(HOOK_OBJECT_NAME, schema, source);
    }

    @Override
    public Hook parse() throws ParsingException {
        return Hook.createV3()
                   .setName(getName())
                   .setParameters(getParameters())
                   .setPhases(getPhases())
                   .setRequiredDependencies(getRequiredDependencies())
                   .setType(getType());
    }

    private String getType() {
        return getStringElement(TYPE);
    }

    protected List<RequiredDependency> getRequiredDependencies() {
        return getListElement(REQUIRES, new ListParser<RequiredDependency>() {
            @Override
            protected RequiredDependency parseItem(Map<String, Object> map) {
                return getRequiredDependencyParser(map).setUsedValues(usedRequiredDependencyNames)
                                                       .parse();
            }
        });
    }

    protected RequiredDependencyParser getRequiredDependencyParser(Map<String, Object> source) {
        return new RequiredDependencyParser(source); // only v3
    }

    private List<String> getPhases() {
        return getListElement(PHASES);
    }

    private Map<String, Object> getParameters() {
        return getMapElement(PARAMETERS);
    }

    private String getName() {
        return getStringElement(NAME);
    }

}

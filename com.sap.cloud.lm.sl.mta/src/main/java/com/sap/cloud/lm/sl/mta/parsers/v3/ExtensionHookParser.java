package com.sap.cloud.lm.sl.mta.parsers.v3;

import static com.sap.cloud.lm.sl.mta.handlers.v3.Schemas.EXT_HOOK;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.ExtensionHook;
import com.sap.cloud.lm.sl.mta.model.ExtensionRequiredDependency;
import com.sap.cloud.lm.sl.mta.parsers.ListParser;
import com.sap.cloud.lm.sl.mta.parsers.ModelParser;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class ExtensionHookParser extends ModelParser<ExtensionHook> {

    protected static final String HOOK_OBJECT_NAME = "MTA extension hook";

    public static final String NAME = "name";
    public static final String PARAMETERS = "parameters";
    public static final String REQUIRES = "requires";

    protected final Set<String> usedRequiredDependencyNames = new HashSet<>();

    public ExtensionHookParser(Map<String, Object> source) {
        this(EXT_HOOK, source);
    }

    public ExtensionHookParser(MapElement schema, Map<String, Object> source) {
        super(HOOK_OBJECT_NAME, schema, source);
    }

    @Override
    public ExtensionHook parse() throws ParsingException {
        return ExtensionHook.createV3()
            .setName(getName())
            .setParameters(getParameters())
            .setRequiredDependencies(getExtensionRequiredDependencies());
    }

    protected List<ExtensionRequiredDependency> getExtensionRequiredDependencies() {
        return getListElement(REQUIRES, new ListParser<ExtensionRequiredDependency>() {
            @Override
            protected ExtensionRequiredDependency parseItem(Map<String, Object> map) {
                return getRequiredDependencyParser(map).setUsedValues(usedRequiredDependencyNames)
                    .parse();
            }
        });
    }

    protected ExtensionRequiredDependencyParser getRequiredDependencyParser(Map<String, Object> source) {
        return new ExtensionRequiredDependencyParser(source); // only v3
    }

    private Map<String, Object> getParameters() {
        return getMapElement(PARAMETERS);
    }

    private String getName() {
        return getStringElement(NAME);
    }

}

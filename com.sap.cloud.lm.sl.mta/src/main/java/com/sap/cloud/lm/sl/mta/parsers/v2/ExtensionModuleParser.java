package com.sap.cloud.lm.sl.mta.parsers.v2;

import static com.sap.cloud.lm.sl.mta.handlers.v2.Schemas.EXT_MODULE;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.model.v2.ExtensionModule;
import com.sap.cloud.lm.sl.mta.model.v2.ExtensionModule.Builder;
import com.sap.cloud.lm.sl.mta.model.v2.ExtensionProvidedDependency;
import com.sap.cloud.lm.sl.mta.model.v2.ExtensionRequiredDependency;
import com.sap.cloud.lm.sl.mta.parsers.ListParser;
import com.sap.cloud.lm.sl.mta.parsers.ModelParser;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class ExtensionModuleParser extends ModelParser<ExtensionModule> {

    protected static final String PROCESSED_OBJECT_NAME = "MTA extension module";

    public static final String NAME = "name";
    public static final String PROPERTIES = "properties";
    public static final String PROVIDES = "provides";
    public static final String PARAMETERS = "parameters";
    public static final String REQUIRES = "requires";

    protected Set<String> usedProvidedDependencyNames = Collections.emptySet();
    protected Set<String> usedRequiredDependencyNames = new HashSet<>();

    public ExtensionModuleParser(Map<String, Object> source) {
        this(EXT_MODULE, source);
    }

    protected ExtensionModuleParser(MapElement schema, Map<String, Object> source) {
        super(PROCESSED_OBJECT_NAME, schema, source);
    }

    @Override
    public ExtensionModule parse() throws ParsingException {
        Builder builder = new Builder();
        builder.setName(getName());
        builder.setProperties(getProperties());
        builder.setParameters(getParameters());
        builder.setRequiredDependencies2(getExtensionRequiredDependencies2());
        builder.setProvidedDependencies2(getExtensionProvidedDependencies2());
        return builder.build();
    }

    protected String getName() throws ParsingException {
        return getStringElement(NAME);
    }

    protected Map<String, Object> getProperties() {
        return getMapElement(PROPERTIES);
    }

    protected Map<String, Object> getParameters() {
        return getMapElement(PARAMETERS);
    }

    protected List<ExtensionRequiredDependency> getExtensionRequiredDependencies2() {
        return getListElement(REQUIRES, new ListParser<ExtensionRequiredDependency>() {
            @Override
            protected ExtensionRequiredDependency parseItem(Map<String, Object> map) {
                return getRequiredDependencyParser(map).setUsedValues(usedRequiredDependencyNames)
                    .parse();
            }
        });
    }

    protected List<ExtensionProvidedDependency> getExtensionProvidedDependencies2() {
        return ListUtil.cast(getExtensionProvidedDependencies());
    }

    protected List<ExtensionProvidedDependency> getExtensionProvidedDependencies() {
        return getListElement(PROVIDES, new ListParser<ExtensionProvidedDependency>() {
            @Override
            protected ExtensionProvidedDependency parseItem(Map<String, Object> map) {
                return getProvidedDependencyParser(map).setUsedValues(usedProvidedDependencyNames)
                    .parse();
            }
        });
    }

    protected ExtensionRequiredDependencyParser getRequiredDependencyParser(Map<String, Object> source) {
        return new com.sap.cloud.lm.sl.mta.parsers.v2.ExtensionRequiredDependencyParser(source);
    }

    protected ExtensionProvidedDependencyParser getProvidedDependencyParser(Map<String, Object> source) {
        return new com.sap.cloud.lm.sl.mta.parsers.v2.ExtensionProvidedDependencyParser(source);
    }

}

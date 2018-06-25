package com.sap.cloud.lm.sl.mta.parsers.v1_0;

import static com.sap.cloud.lm.sl.mta.handlers.v1_0.Schemas.EXT_MODULE;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.v1_0.ExtensionModule;
import com.sap.cloud.lm.sl.mta.model.v1_0.ExtensionModule.Builder;
import com.sap.cloud.lm.sl.mta.model.v1_0.ExtensionProvidedDependency;
import com.sap.cloud.lm.sl.mta.parsers.ListParser;
import com.sap.cloud.lm.sl.mta.parsers.ModelParser;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class ExtensionModuleParser extends ModelParser<ExtensionModule> {

    protected static final String PROCESSED_OBJECT_NAME = "MTA extension module";

    public static final String NAME = "name";
    public static final String PROPERTIES = "properties";
    public static final String PROVIDES = "provides";

    protected Set<String> usedProvidedDependencyNames = Collections.emptySet();

    public ExtensionModuleParser(Map<String, Object> source) {
        this(EXT_MODULE, source);
    }

    protected ExtensionModuleParser(MapElement schema, Map<String, Object> source) {
        super(PROCESSED_OBJECT_NAME, schema, source);
    }

    public ExtensionModuleParser setUsedProvidedDependencyNames(Set<String> usedProvidedDependencyNames) {
        this.usedProvidedDependencyNames = usedProvidedDependencyNames;
        return this;
    }

    @Override
    public ExtensionModule parse() throws ParsingException {
        Builder builder = new Builder();
        builder.setName(getName());
        builder.setProperties(getProperties());
        builder.setProvidedDependencies1_0(getExtensionProvidedDependencies1_0());
        return builder.build();
    }

    protected String getName() throws ParsingException {
        return getStringElement(NAME);
    }

    protected Map<String, Object> getProperties() {
        return getMapElement(PROPERTIES);
    }

    protected List<ExtensionProvidedDependency> getExtensionProvidedDependencies1_0() throws ParsingException {
        return getListElement(PROVIDES, new ListParser<ExtensionProvidedDependency>() {
            @Override
            protected ExtensionProvidedDependency parseItem(Map<String, Object> map) throws ParsingException {
                return getProvidedDependencyParser(map).setUsedValues(usedProvidedDependencyNames)
                    .parse();
            }
        });
    }

    protected ExtensionProvidedDependencyParser getProvidedDependencyParser(Map<String, Object> source) {
        return new ExtensionProvidedDependencyParser(source);
    }

}

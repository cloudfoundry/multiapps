package com.sap.cloud.lm.sl.mta.parsers.v1;

import static com.sap.cloud.lm.sl.mta.handlers.v1.Schemas.MODULE;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.v1.Module;
import com.sap.cloud.lm.sl.mta.model.v1.ProvidedDependency;
import com.sap.cloud.lm.sl.mta.model.v1.Module.Builder;
import com.sap.cloud.lm.sl.mta.parsers.ListParser;
import com.sap.cloud.lm.sl.mta.parsers.ModelParser;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class ModuleParser extends ModelParser<Module> {

    protected static final String PROCESSED_OBJECT_NAME = "MTA module";

    public static final String NAME = "name";
    public static final String TYPE = "type";
    public static final String DESCRIPTION = "description";
    public static final String PROPERTIES = "properties";
    public static final String REQUIRES = "requires";
    public static final String PROVIDES = "provides";

    protected Set<String> usedProvidedDependencyNames = Collections.emptySet();

    public ModuleParser(Map<String, Object> source) {
        this(MODULE, source);
    }

    protected ModuleParser(MapElement schema, Map<String, Object> source) {
        super(PROCESSED_OBJECT_NAME, schema, source);
    }

    public ModuleParser setUsedProvidedDependencyNames(Set<String> usedProvidedDependencyNames) {
        this.usedProvidedDependencyNames = usedProvidedDependencyNames;
        return this;
    }

    @Override
    public Module parse() throws ParsingException {
        Builder builder = new Builder();
        builder.setName(getName());
        builder.setType(getType());
        builder.setDescription(getDescription());
        builder.setProperties(getProperties());
        builder.setRequiredDependencies1(getRequiredDependencies1());
        builder.setProvidedDependencies1(getProvidedDependencies1());
        return builder.build();
    }

    protected String getName() {
        return getStringElement(NAME);
    }

    protected String getType() {
        return getStringElement(TYPE);
    }

    protected String getDescription() {
        return getStringElement(DESCRIPTION);
    }

    protected List<String> getRequiredDependencies1() {
        return getListElement(REQUIRES);
    }

    protected Map<String, Object> getProperties() {
        return getMapElement(PROPERTIES);
    }

    protected List<ProvidedDependency> getProvidedDependencies1() {
        return getListElement(PROVIDES, new ListParser<ProvidedDependency>() {
            @Override
            protected ProvidedDependency parseItem(Map<String, Object> map) {
                return getProvidedDependencyParser(map).setUsedValues(usedProvidedDependencyNames)
                    .parse();
            }
        });
    }

    protected ProvidedDependencyParser getProvidedDependencyParser(Map<String, Object> source) {
        return new ProvidedDependencyParser(source);
    }

}

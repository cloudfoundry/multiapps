package com.sap.cloud.lm.sl.mta.parsers.v2;

import static com.sap.cloud.lm.sl.mta.handlers.v2.Schemas.MODULE;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.model.v2.Module;
import com.sap.cloud.lm.sl.mta.model.v2.Module.Builder;
import com.sap.cloud.lm.sl.mta.model.v2.ProvidedDependency;
import com.sap.cloud.lm.sl.mta.model.v2.RequiredDependency;
import com.sap.cloud.lm.sl.mta.parsers.ListParser;
import com.sap.cloud.lm.sl.mta.parsers.ModelParser;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class ModuleParser extends ModelParser<Module> {

    public static final String PATH = "path";
    public static final String PARAMETERS = "parameters";
    public static final String NAME = "name";
    public static final String TYPE = "type";
    public static final String DESCRIPTION = "description";
    public static final String PROPERTIES = "properties";
    public static final String REQUIRES = "requires";
    public static final String PROVIDES = "provides";
    protected static final String PROCESSED_OBJECT_NAME = "MTA module";
    protected final Set<String> usedRequiredDependencyNames = new HashSet<>();
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
        builder.setPath(getPath());
        builder.setProperties(getProperties());
        builder.setParameters(getParameters());
        builder.setRequiredDependencies2(getRequiredDependencies2());
        builder.setProvidedDependencies2(getProvidedDependencies2());
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

    protected Map<String, Object> getProperties() {
        return getMapElement(PROPERTIES);
    }

    protected String getPath() {
        return getStringElement(PATH);
    }

    protected Map<String, Object> getParameters() {
        return getMapElement(PARAMETERS);
    }

    protected List<ProvidedDependency> getProvidedDependencies2() {
        List<ProvidedDependency> providedDependencies = ListUtil.cast(getProvidedDependencies());
        return getAllProvidedDependencies(providedDependencies);
    }

    protected List<ProvidedDependency> getProvidedDependencies() {
        return getListElement(PROVIDES, new ListParser<ProvidedDependency>() {
            @Override
            protected ProvidedDependency parseItem(Map<String, Object> map) {
                return getProvidedDependencyParser(map).setUsedValues(usedProvidedDependencyNames)
                                                       .parse();
            }
        });
    }

    protected ProvidedDependencyParser getProvidedDependencyParser(Map<String, Object> source) {
        return new ProvidedDependencyParser(source); // v2
    }

    protected List<ProvidedDependency> getAllProvidedDependencies(List<ProvidedDependency> providedDependencies) {
        List<ProvidedDependency> result = providedDependencies;
        if (!currentModuleIsProvided(result)) {
            result = ListUtil.cast(result);
            result.add(getCurrentModuleAsProvidedDependency());
        }
        return result;
    }

    protected boolean currentModuleIsProvided(List<ProvidedDependency> providedDependencies) {
        String currentModuleName = getName();
        for (ProvidedDependency providedDependency : providedDependencies) {
            if (providedDependency.getName()
                                  .equals(currentModuleName)) {
                return true;
            }
        }
        return false;
    }

    protected ProvidedDependency getCurrentModuleAsProvidedDependency() {
        Map<String, Object> currentModule = new TreeMap<>();
        currentModule.put(NAME, getName());
        return getProvidedDependencyParser(currentModule).setUsedValues(usedProvidedDependencyNames)
                                                         .parse();
    }

    protected List<RequiredDependency> getRequiredDependencies2() {
        return getListElement(REQUIRES, new ListParser<RequiredDependency>() {
            @Override
            protected RequiredDependency parseItem(Map<String, Object> map) {
                return getRequiredDependencyParser(map).setUsedValues(usedRequiredDependencyNames)
                                                       .parse();
            }
        });
    }

    protected RequiredDependencyParser getRequiredDependencyParser(Map<String, Object> source) {
        return new RequiredDependencyParser(source);
    }

}

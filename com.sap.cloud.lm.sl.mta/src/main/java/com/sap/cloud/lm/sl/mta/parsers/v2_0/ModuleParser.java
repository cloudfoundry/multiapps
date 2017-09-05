package com.sap.cloud.lm.sl.mta.parsers.v2_0;

import static com.sap.cloud.lm.sl.mta.handlers.v2_0.Schemas.MODULE;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.model.v2_0.Module;
import com.sap.cloud.lm.sl.mta.model.v2_0.Module.ModuleBuilder;
import com.sap.cloud.lm.sl.mta.model.v2_0.ProvidedDependency;
import com.sap.cloud.lm.sl.mta.model.v2_0.RequiredDependency;
import com.sap.cloud.lm.sl.mta.parsers.ListParser;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class ModuleParser extends com.sap.cloud.lm.sl.mta.parsers.v1_0.ModuleParser {

    public static final String PATH = "path";
    public static final String PARAMETERS = "parameters";

    protected final Set<String> usedRequiredDependencyNames = new HashSet<String>();

    public ModuleParser(Map<String, Object> source) {
        super(MODULE, source);
    }

    protected ModuleParser(MapElement schema, Map<String, Object> source) {
        super(schema, source);
    }

    @Override
    public Module parse() throws ParsingException {
        ModuleBuilder builder = new ModuleBuilder();
        builder.setName(getName());
        builder.setType(getType());
        builder.setDescription(getDescription());
        builder.setPath(getPath());
        builder.setProperties(getProperties());
        builder.setParameters(getParameters());
        builder.setRequiredDependencies2_0(getRequiredDependencies2_0());
        builder.setProvidedDependencies2_0(getProvidedDependencies2_0());
        return builder.build();
    }

    protected String getPath() throws ParsingException {
        return getStringElement(PATH);
    }

    protected Map<String, Object> getParameters() {
        return getMapElement(PARAMETERS);
    }

    protected List<ProvidedDependency> getProvidedDependencies2_0() throws ParsingException {
        List<ProvidedDependency> providedDependencies = ListUtil.cast(getProvidedDependencies1_0());
        return getAllProvidedDependencies(providedDependencies);
    }

    @Override
    protected ProvidedDependencyParser getProvidedDependencyParser(Map<String, Object> source) {
        return new ProvidedDependencyParser(source); // v2
    }

    protected List<ProvidedDependency> getAllProvidedDependencies(List<ProvidedDependency> providedDependencies) throws ParsingException {
        List<ProvidedDependency> result = providedDependencies;
        if (!currentModuleIsProvided(result)) {
            result = ListUtil.cast(result);
            result.add(getCurrentModuleAsProvidedDependency());
        }
        return result;
    }

    protected boolean currentModuleIsProvided(List<ProvidedDependency> providedDependencies) throws ParsingException {
        String currentModuleName = getName();
        for (ProvidedDependency providedDependency : providedDependencies) {
            if (providedDependency.getName().equals(currentModuleName)) {
                return true;
            }
        }
        return false;
    }

    protected ProvidedDependency getCurrentModuleAsProvidedDependency() throws ParsingException {
        Map<String, Object> currentModule = new TreeMap<String, Object>();
        currentModule.put(NAME, getName());
        return (ProvidedDependency) getProvidedDependencyParser(currentModule).setUsedValues(usedProvidedDependencyNames).parse();
    }

    protected List<RequiredDependency> getRequiredDependencies2_0() throws ParsingException {
        return getListElement(REQUIRES, new ListParser<RequiredDependency>() {
            @Override
            protected RequiredDependency parseItem(Map<String, Object> map) throws ParsingException {
                return getRequiredDependencyParser(map).setUsedValues(usedRequiredDependencyNames).parse();
            }
        });
    }

    protected RequiredDependencyParser getRequiredDependencyParser(Map<String, Object> source) {
        return new RequiredDependencyParser(source); // v2
    }

}

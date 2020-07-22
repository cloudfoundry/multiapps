package org.cloudfoundry.multiapps.mta.parsers.v2;

import static org.cloudfoundry.multiapps.mta.handlers.v2.Schemas.MODULE;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.cloudfoundry.multiapps.common.ParsingException;
import org.cloudfoundry.multiapps.mta.model.Module;
import org.cloudfoundry.multiapps.mta.model.ProvidedDependency;
import org.cloudfoundry.multiapps.mta.model.RequiredDependency;
import org.cloudfoundry.multiapps.mta.parsers.ListParser;
import org.cloudfoundry.multiapps.mta.parsers.ModelParser;
import org.cloudfoundry.multiapps.mta.schema.MapElement;

public class ModuleParser extends ModelParser<Module> {

    protected static final String PROCESSED_OBJECT_NAME = "MTA module";

    public static final String PATH = "path";
    public static final String PARAMETERS = "parameters";
    public static final String NAME = "name";
    public static final String TYPE = "type";
    public static final String DESCRIPTION = "description";
    public static final String PROPERTIES = "properties";
    public static final String REQUIRES = "requires";
    public static final String PROVIDES = "provides";

    protected Set<String> usedProvidedDependencyNames = new HashSet<>();
    protected final Set<String> usedRequiredDependencyNames = new HashSet<>();

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
        return createEntity().setName(getDescription())
                             .setName(getName())
                             .setType(getType())
                             .setPath(getPath())
                             .setProperties(getProperties())
                             .setParameters(getParameters())
                             .setProvidedDependencies(getProvidedDependencies())
                             .setRequiredDependencies(getRequiredDependencies());
    }

    protected Module createEntity() {
        return Module.createV2();
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

    protected List<ProvidedDependency> getProvidedDependencies() {
        List<ProvidedDependency> providedDependencies = getListElement(PROVIDES, new ListParser<ProvidedDependency>() {
            @Override
            protected ProvidedDependency parseItem(Map<String, Object> map) {
                return getProvidedDependencyParser(map).setUsedValues(usedProvidedDependencyNames)
                                                       .parse();
            }
        });
        return getAllProvidedDependencies(providedDependencies);
    }

    protected ProvidedDependencyParser getProvidedDependencyParser(Map<String, Object> source) {
        return new ProvidedDependencyParser(source); // v2
    }

    protected List<ProvidedDependency> getAllProvidedDependencies(List<ProvidedDependency> providedDependencies) {
        List<ProvidedDependency> allProvidedDependencies = new ArrayList<>(providedDependencies);
        if (!currentModuleIsProvided(allProvidedDependencies)) {
            allProvidedDependencies.add(getCurrentModuleAsProvidedDependency());
        }
        return allProvidedDependencies;
    }

    protected boolean currentModuleIsProvided(List<ProvidedDependency> providedDependencies) {
        String currentModuleName = getName();
        return providedDependencies.stream()
                                   .anyMatch(providedDependency -> providedDependency.getName()
                                                                                     .equals(currentModuleName));
    }

    protected ProvidedDependency getCurrentModuleAsProvidedDependency() {
        Map<String, Object> currentModule = new TreeMap<>();
        currentModule.put(NAME, getName());
        return getProvidedDependencyParser(currentModule).setUsedValues(usedProvidedDependencyNames)
                                                         .parse();
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
        return new RequiredDependencyParser(source);
    }

}

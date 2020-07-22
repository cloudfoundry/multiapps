package org.cloudfoundry.multiapps.mta.parsers.v3;

import static org.cloudfoundry.multiapps.mta.handlers.v3.Schemas.MODULE;

import java.util.List;
import java.util.Map;

import org.cloudfoundry.multiapps.common.ParsingException;
import org.cloudfoundry.multiapps.mta.model.Hook;
import org.cloudfoundry.multiapps.mta.model.Metadata;
import org.cloudfoundry.multiapps.mta.model.Module;
import org.cloudfoundry.multiapps.mta.parsers.ListParser;
import org.cloudfoundry.multiapps.mta.schema.MapElement;

public class ModuleParser extends org.cloudfoundry.multiapps.mta.parsers.v2.ModuleParser {

    public static final String PROPERTIES_METADATA = "properties-metadata";
    public static final String PARAMETERS_METADATA = "parameters-metadata";
    public static final String DEPLOYED_AFTER = "deployed-after";
    public static final String HOOKS = "hooks";

    public ModuleParser(Map<String, Object> source) {
        super(MODULE, source);
    }

    protected ModuleParser(MapElement schema, Map<String, Object> source) {
        super(schema, source);
    }

    @Override
    public Module parse() throws ParsingException {
        return super.parse().setDeployedAfter(getDeployedAfter())
                            .setPropertiesMetadata(getPropertiesMetadata())
                            .setParametersMetadata(getParametersMetadata())
                            .setHooks(getHooks());
    }

    @Override
    public Module createEntity() {
        return Module.createV3();
    }

    protected Metadata getPropertiesMetadata() {
        return getMetadata(PROPERTIES_METADATA, getProperties());
    }

    protected Metadata getParametersMetadata() {
        return getMetadata(PARAMETERS_METADATA, getParameters());
    }

    protected List<Hook> getHooks() {
        return getListElement(HOOKS, new ListParser<Hook>() {

            @Override
            protected Hook parseItem(Map<String, Object> map) {
                return new HookParser(map).parse();
            }
        });
    }

    @Override
    protected ProvidedDependencyParser getProvidedDependencyParser(Map<String, Object> source) {
        return new ProvidedDependencyParser(source); // v3
    }

    @Override
    protected RequiredDependencyParser getRequiredDependencyParser(Map<String, Object> source) {
        return new RequiredDependencyParser(source); // v3
    }

    protected List<String> getDeployedAfter() {
        return getListElement(DEPLOYED_AFTER);
    }
}

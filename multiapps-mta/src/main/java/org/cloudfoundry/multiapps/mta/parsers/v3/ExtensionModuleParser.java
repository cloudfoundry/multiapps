package org.cloudfoundry.multiapps.mta.parsers.v3;

import static org.cloudfoundry.multiapps.mta.handlers.v2.Schemas.EXT_MODULE;

import java.util.List;
import java.util.Map;

import org.cloudfoundry.multiapps.common.ParsingException;
import org.cloudfoundry.multiapps.mta.model.ExtensionHook;
import org.cloudfoundry.multiapps.mta.model.ExtensionModule;
import org.cloudfoundry.multiapps.mta.parsers.ListParser;
import org.cloudfoundry.multiapps.mta.schema.MapElement;

public class ExtensionModuleParser extends org.cloudfoundry.multiapps.mta.parsers.v2.ExtensionModuleParser {

    public static final String HOOKS = "hooks";

    public ExtensionModuleParser(Map<String, Object> source) {
        super(EXT_MODULE, source);
    }

    protected ExtensionModuleParser(MapElement schema, Map<String, Object> source) {
        super(schema, source);
    }

    @Override
    public ExtensionModule parse() throws ParsingException {
        ExtensionModule extension = super.parse();
        extension.setHooks(getExtensionHooks());
        return extension;
    }

    private List<ExtensionHook> getExtensionHooks() {
        return getListElement(HOOKS, new ListParser<ExtensionHook>() {
            @Override
            protected ExtensionHook parseItem(Map<String, Object> map) {
                return getHookParse(map).setUsedValues(usedRequiredDependencyNames)
                                        .parse();
            }
        });
    }

    @Override
    protected ExtensionModule createEntity() {
        return ExtensionModule.createV3();
    }

    @Override
    protected ExtensionRequiredDependencyParser getRequiredDependencyParser(Map<String, Object> source) {
        return new org.cloudfoundry.multiapps.mta.parsers.v3.ExtensionRequiredDependencyParser(source);
    }

    @Override
    protected ExtensionProvidedDependencyParser getProvidedDependencyParser(Map<String, Object> source) {
        return new org.cloudfoundry.multiapps.mta.parsers.v3.ExtensionProvidedDependencyParser(source);
    }

    protected ExtensionHookParser getHookParse(Map<String, Object> source) {
        return new ExtensionHookParser(source);
    }

}

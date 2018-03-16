package com.sap.cloud.lm.sl.mta.parsers.v2_0;

import static com.sap.cloud.lm.sl.mta.handlers.v2_0.Schemas.EXT_MODULE;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.model.v2_0.ExtensionModule;
import com.sap.cloud.lm.sl.mta.model.v2_0.ExtensionModule.ExtensionModuleBuilder;
import com.sap.cloud.lm.sl.mta.model.v2_0.ExtensionProvidedDependency;
import com.sap.cloud.lm.sl.mta.model.v2_0.ExtensionRequiredDependency;
import com.sap.cloud.lm.sl.mta.parsers.ListParser;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class ExtensionModuleParser extends com.sap.cloud.lm.sl.mta.parsers.v1_0.ExtensionModuleParser {

    public static final String PARAMETERS = "parameters";
    public static final String REQUIRES = "requires";

    protected Set<String> usedRequiredDependencyNames = new HashSet<String>();

    public ExtensionModuleParser(Map<String, Object> source) {
        super(EXT_MODULE, source);
    }

    protected ExtensionModuleParser(MapElement schema, Map<String, Object> source) {
        super(schema, source);
    }

    @Override
    public ExtensionModule parse() throws ParsingException {
        ExtensionModuleBuilder builder = new ExtensionModuleBuilder();
        builder.setName(getName());
        builder.setProperties(getProperties());
        builder.setParameters(getParameters());
        builder.setRequiredDependencies2_0(getExtensionRequiredDependencies2_0());
        builder.setProvidedDependencies2_0(getExtensionProvidedDependencies2_0());
        return builder.build();
    }

    protected Map<String, Object> getParameters() {
        return getMapElement(PARAMETERS);
    }

    protected List<ExtensionRequiredDependency> getExtensionRequiredDependencies2_0() throws ParsingException {
        return getListElement(REQUIRES, new ListParser<ExtensionRequiredDependency>() {
            @Override
            protected ExtensionRequiredDependency parseItem(Map<String, Object> map) throws ParsingException {
                return getRequiredDependencyParser(map).setUsedValues(usedRequiredDependencyNames)
                    .parse();
            }
        });
    }

    protected List<ExtensionProvidedDependency> getExtensionProvidedDependencies2_0() throws ParsingException {
        return ListUtil.cast(getExtensionProvidedDependencies1_0());
    }

    protected ExtensionRequiredDependencyParser getRequiredDependencyParser(Map<String, Object> source) {
        return new com.sap.cloud.lm.sl.mta.parsers.v2_0.ExtensionRequiredDependencyParser(source);
    }

    @Override
    protected ExtensionProvidedDependencyParser getProvidedDependencyParser(Map<String, Object> source) {
        return new com.sap.cloud.lm.sl.mta.parsers.v2_0.ExtensionProvidedDependencyParser(source);
    }

}

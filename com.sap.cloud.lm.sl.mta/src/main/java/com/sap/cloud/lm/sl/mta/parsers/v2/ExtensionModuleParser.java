package com.sap.cloud.lm.sl.mta.parsers.v2;

import static com.sap.cloud.lm.sl.mta.handlers.v2.Schemas.EXT_MODULE;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.model.v2.ExtensionModule;
import com.sap.cloud.lm.sl.mta.model.v2.ExtensionProvidedDependency;
import com.sap.cloud.lm.sl.mta.model.v2.ExtensionRequiredDependency;
import com.sap.cloud.lm.sl.mta.model.v2.ExtensionModule.Builder;
import com.sap.cloud.lm.sl.mta.parsers.ListParser;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class ExtensionModuleParser extends com.sap.cloud.lm.sl.mta.parsers.v1.ExtensionModuleParser {

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
        Builder builder = new Builder();
        builder.setName(getName());
        builder.setProperties(getProperties());
        builder.setParameters(getParameters());
        builder.setRequiredDependencies2(getExtensionRequiredDependencies2());
        builder.setProvidedDependencies2(getExtensionProvidedDependencies2());
        return builder.build();
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
        return ListUtil.cast(getExtensionProvidedDependencies1());
    }

    protected ExtensionRequiredDependencyParser getRequiredDependencyParser(Map<String, Object> source) {
        return new com.sap.cloud.lm.sl.mta.parsers.v2.ExtensionRequiredDependencyParser(source);
    }

    @Override
    protected ExtensionProvidedDependencyParser getProvidedDependencyParser(Map<String, Object> source) {
        return new com.sap.cloud.lm.sl.mta.parsers.v2.ExtensionProvidedDependencyParser(source);
    }

}

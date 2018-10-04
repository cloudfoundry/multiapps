package com.sap.cloud.lm.sl.mta.parsers.v3;

import static com.sap.cloud.lm.sl.mta.handlers.v3.Schemas.EXT_MODULE;

import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.model.v3.ExtensionModule;
import com.sap.cloud.lm.sl.mta.model.v3.ExtensionProvidedDependency;
import com.sap.cloud.lm.sl.mta.model.v3.ExtensionRequiredDependency;
import com.sap.cloud.lm.sl.mta.model.v3.ExtensionModule.Builder;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class ExtensionModuleParser extends com.sap.cloud.lm.sl.mta.parsers.v2.ExtensionModuleParser {

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
        builder.setRequiredDependencies3(getExtensionRequiredDependencies3());
        builder.setProvidedDependencies3(getExtensionProvidedDependencies3());
        return builder.build();
    }

    protected List<ExtensionRequiredDependency> getExtensionRequiredDependencies3() throws ParsingException {
        return ListUtil.cast(getExtensionRequiredDependencies2());
    }

    protected List<ExtensionProvidedDependency> getExtensionProvidedDependencies3() throws ParsingException {
        return ListUtil.cast(getExtensionProvidedDependencies2());
    }

    @Override
    protected ExtensionRequiredDependencyParser getRequiredDependencyParser(Map<String, Object> source) {
        return new com.sap.cloud.lm.sl.mta.parsers.v3.ExtensionRequiredDependencyParser(source);
    }

    @Override
    protected ExtensionProvidedDependencyParser getProvidedDependencyParser(Map<String, Object> source) {
        return new com.sap.cloud.lm.sl.mta.parsers.v3.ExtensionProvidedDependencyParser(source);
    }

}

package com.sap.cloud.lm.sl.mta.parsers.v3_0;

import static com.sap.cloud.lm.sl.mta.handlers.v3_0.Schemas.EXT_MODULE;

import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.model.v3_0.ExtensionModule;
import com.sap.cloud.lm.sl.mta.model.v3_0.ExtensionModule.ExtensionModuleBuilder;
import com.sap.cloud.lm.sl.mta.model.v3_0.ExtensionProvidedDependency;
import com.sap.cloud.lm.sl.mta.model.v3_0.ExtensionRequiredDependency;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class ExtensionModuleParser extends com.sap.cloud.lm.sl.mta.parsers.v2_0.ExtensionModuleParser {

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
        builder.setRequiredDependencies3_0(getExtensionRequiredDependencies3_0());
        builder.setProvidedDependencies3_0(getExtensionProvidedDependencies3_0());
        return builder.build();
    }

    protected List<ExtensionRequiredDependency> getExtensionRequiredDependencies3_0() throws ParsingException {
        return ListUtil.cast(getExtensionRequiredDependencies2_0());
    }

    protected List<ExtensionProvidedDependency> getExtensionProvidedDependencies3_0() throws ParsingException {
        return ListUtil.cast(getExtensionProvidedDependencies2_0());
    }

    @Override
    protected ExtensionRequiredDependencyParser getRequiredDependencyParser(Map<String, Object> source) {
        return new com.sap.cloud.lm.sl.mta.parsers.v3_0.ExtensionRequiredDependencyParser(source);
    }

    @Override
    protected ExtensionProvidedDependencyParser getProvidedDependencyParser(Map<String, Object> source) {
        return new com.sap.cloud.lm.sl.mta.parsers.v3_0.ExtensionProvidedDependencyParser(source);
    }

}

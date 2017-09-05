package com.sap.cloud.lm.sl.mta.parsers.v3_1;

import static com.sap.cloud.lm.sl.mta.handlers.v3_1.Schemas.EXT_MODULE;

import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.common.util.ListUtil;
import com.sap.cloud.lm.sl.mta.model.v3_1.ExtensionModule;
import com.sap.cloud.lm.sl.mta.model.v3_1.ExtensionModule.ExtensionModuleBuilder;
import com.sap.cloud.lm.sl.mta.model.v3_1.ExtensionProvidedDependency;
import com.sap.cloud.lm.sl.mta.model.v3_1.ExtensionRequiredDependency;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class ExtensionModuleParser extends com.sap.cloud.lm.sl.mta.parsers.v3_0.ExtensionModuleParser {

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
        builder.setRequiredDependencies3_1(getExtensionRequiredDependencies3_1());
        builder.setProvidedDependencies3_1(getExtensionProvidedDependencies3_1());
        return builder.build();
    }

    protected List<ExtensionRequiredDependency> getExtensionRequiredDependencies3_1() throws ParsingException {
        return ListUtil.cast(getExtensionRequiredDependencies3_0());
    }

    protected List<ExtensionProvidedDependency> getExtensionProvidedDependencies3_1() throws ParsingException {
        return ListUtil.cast(getExtensionProvidedDependencies3_0());
    }

    @Override
    protected ExtensionRequiredDependencyParser getRequiredDependencyParser(Map<String, Object> source) {
        return new com.sap.cloud.lm.sl.mta.parsers.v3_1.ExtensionRequiredDependencyParser(source);
    }

    @Override
    protected ExtensionProvidedDependencyParser getProvidedDependencyParser(Map<String, Object> source) {
        return new com.sap.cloud.lm.sl.mta.parsers.v3_1.ExtensionProvidedDependencyParser(source);
    }

}

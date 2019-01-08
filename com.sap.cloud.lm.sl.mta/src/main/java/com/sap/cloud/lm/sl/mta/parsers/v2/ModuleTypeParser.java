package com.sap.cloud.lm.sl.mta.parsers.v2;

import static com.sap.cloud.lm.sl.mta.handlers.v2.Schemas.MODULE_TYPE;

import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.v2.ModuleType;
import com.sap.cloud.lm.sl.mta.model.v2.ModuleType.Builder;
import com.sap.cloud.lm.sl.mta.parsers.ModelParser;
import com.sap.cloud.lm.sl.mta.schema.MapElement;

public class ModuleTypeParser extends ModelParser<ModuleType> {

    protected static final String PROCESSED_OBJECT_NAME = "MTA module type";

    public static final String NAME = "name";
    public static final String DEPLOYER = "deployer";
    public static final String PROPERTIES = "properties";
    public static final String PARAMETERS = "parameters";

    public ModuleTypeParser(Map<String, Object> source) {
        this(MODULE_TYPE, source);
    }

    protected ModuleTypeParser(MapElement schema, Map<String, Object> source) {
        super(PROCESSED_OBJECT_NAME, schema, source);
    }

    @Override
    public ModuleType parse() throws ParsingException {
        Builder builder = new Builder();
        builder.setName(getName());
        builder.setDeployer(getDeployer());
        builder.setProperties(getProperties());
        builder.setParameters(getParameters());
        return builder.build();
    }

    protected String getName() {
        return getStringElement(NAME);
    }

    protected String getDeployer() {
        return getStringElement(DEPLOYER);
    }

    protected Map<String, Object> getProperties() {
        return getMapElement(PROPERTIES);
    }

    protected Map<String, Object> getParameters() {
        return getMapElement(PARAMETERS);
    }

}

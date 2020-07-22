package com.sap.cloud.lm.sl.mta.handlers.v3;

import java.util.Map;

import com.sap.cloud.lm.sl.mta.parsers.v3.DeploymentDescriptorParser;
import com.sap.cloud.lm.sl.mta.parsers.v3.ExtensionDescriptorParser;
import com.sap.cloud.lm.sl.mta.schema.SchemaValidator;

public class DescriptorParser extends com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorParser {

    public DescriptorParser() {
        super(new SchemaValidator(Schemas.MTAD), new SchemaValidator(Schemas.MTAEXT));
    }

    protected DescriptorParser(SchemaValidator mtadValidator, SchemaValidator mtaextValidator) {
        super(mtadValidator, mtaextValidator);
    }

    @Override
    protected ExtensionDescriptorParser getExtensionDescriptorParser(Map<String, Object> map) {
        return new com.sap.cloud.lm.sl.mta.parsers.v3.ExtensionDescriptorParser(map);
    }

    @Override
    protected DeploymentDescriptorParser getDeploymentDescriptorParser(Map<String, Object> map) {
        return new com.sap.cloud.lm.sl.mta.parsers.v3.DeploymentDescriptorParser(map);
    }

}

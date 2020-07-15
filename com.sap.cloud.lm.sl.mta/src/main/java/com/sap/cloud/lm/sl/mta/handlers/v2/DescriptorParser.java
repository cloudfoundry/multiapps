package com.sap.cloud.lm.sl.mta.handlers.v2;

import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.ExtensionDescriptor;
import com.sap.cloud.lm.sl.mta.parsers.v2.DeploymentDescriptorParser;
import com.sap.cloud.lm.sl.mta.parsers.v2.ExtensionDescriptorParser;
import com.sap.cloud.lm.sl.mta.schema.SchemaValidator;

public class DescriptorParser {

    private final SchemaValidator mtadValidator;
    private final SchemaValidator mtaextValidator;

    public DescriptorParser() {
        this(new SchemaValidator(Schemas.MTAD), new SchemaValidator(Schemas.MTAEXT));
    }

    protected DescriptorParser(SchemaValidator mtadValidator, SchemaValidator mtaextValidator) {
        this.mtadValidator = mtadValidator;
        this.mtaextValidator = mtaextValidator;
    }

    public ExtensionDescriptor parseExtensionDescriptor(Map<String, Object> map) throws ParsingException {
        mtaextValidator.validate(map);
        return getExtensionDescriptorParser(map).parse();
    }

    protected ExtensionDescriptorParser getExtensionDescriptorParser(Map<String, Object> map) {
        return new ExtensionDescriptorParser(map);
    }

    public DeploymentDescriptor parseDeploymentDescriptor(Map<String, Object> map) throws ParsingException {
        mtadValidator.validate(map);
        return getDeploymentDescriptorParser(map).parse();
    }

    protected DeploymentDescriptorParser getDeploymentDescriptorParser(Map<String, Object> map) {
        return new DeploymentDescriptorParser(map);
    }

}

package org.cloudfoundry.multiapps.mta.handlers.v2;

import java.util.Map;

import org.cloudfoundry.multiapps.common.ParsingException;
import org.cloudfoundry.multiapps.mta.model.DeploymentDescriptor;
import org.cloudfoundry.multiapps.mta.model.ExtensionDescriptor;
import org.cloudfoundry.multiapps.mta.parsers.v2.DeploymentDescriptorParser;
import org.cloudfoundry.multiapps.mta.parsers.v2.ExtensionDescriptorParser;
import org.cloudfoundry.multiapps.mta.schema.SchemaValidator;

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

package org.cloudfoundry.multiapps.mta.handlers.v3;

import java.util.Map;

import org.cloudfoundry.multiapps.mta.parsers.v3.DeploymentDescriptorParser;
import org.cloudfoundry.multiapps.mta.parsers.v3.ExtensionDescriptorParser;
import org.cloudfoundry.multiapps.mta.schema.SchemaValidator;

public class DescriptorParser extends org.cloudfoundry.multiapps.mta.handlers.v2.DescriptorParser {

    public DescriptorParser() {
        super(new SchemaValidator(Schemas.MTAD), new SchemaValidator(Schemas.MTAEXT));
    }

    protected DescriptorParser(SchemaValidator mtadValidator, SchemaValidator mtaextValidator) {
        super(mtadValidator, mtaextValidator);
    }

    @Override
    protected ExtensionDescriptorParser getExtensionDescriptorParser(Map<String, Object> map) {
        return new org.cloudfoundry.multiapps.mta.parsers.v3.ExtensionDescriptorParser(map);
    }

    @Override
    protected DeploymentDescriptorParser getDeploymentDescriptorParser(Map<String, Object> map) {
        return new org.cloudfoundry.multiapps.mta.parsers.v3.DeploymentDescriptorParser(map);
    }

}

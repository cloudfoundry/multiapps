package com.sap.cloud.lm.sl.mta.handlers.v1;

import java.io.InputStream;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.v1.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v1.ExtensionDescriptor;
import com.sap.cloud.lm.sl.mta.parsers.v1.DeploymentDescriptorParser;
import com.sap.cloud.lm.sl.mta.parsers.v1.ExtensionDescriptorParser;
import com.sap.cloud.lm.sl.mta.schema.SchemaValidator;
import com.sap.cloud.lm.sl.mta.util.YamlUtil;

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

    public ExtensionDescriptor parseExtensionDescriptorYaml(InputStream yaml) throws ParsingException {
        try {
            return parseExtensionDescriptor(YamlUtil.convertYamlToMap(yaml));
        } finally {
            IOUtils.closeQuietly(yaml);
        }
    }

    public ExtensionDescriptor parseExtensionDescriptorYaml(String yaml) throws ParsingException {
        return parseExtensionDescriptor(YamlUtil.convertYamlToMap(yaml));
    }

    public ExtensionDescriptor parseExtensionDescriptor(Map<String, Object> map) throws ParsingException {
        mtaextValidator.validate(map);
        return getExtensionDescriptorParser(map).parse();
    }

    protected ExtensionDescriptorParser getExtensionDescriptorParser(Map<String, Object> map) {
        return new ExtensionDescriptorParser(map);
    }

    public DeploymentDescriptor parseDeploymentDescriptorYaml(InputStream yaml) throws ParsingException {
        try {
            return parseDeploymentDescriptor(YamlUtil.convertYamlToMap(yaml));
        } finally {
            IOUtils.closeQuietly(yaml);
        }
    }

    public DeploymentDescriptor parseDeploymentDescriptorYaml(String yaml) throws ParsingException {
        return parseDeploymentDescriptor(YamlUtil.convertYamlToMap(yaml));
    }

    public DeploymentDescriptor parseDeploymentDescriptor(Map<String, Object> map) throws ParsingException {
        mtadValidator.validate(map);
        return getDeploymentDescriptorParser(map).parse();
    }

    protected DeploymentDescriptorParser getDeploymentDescriptorParser(Map<String, Object> map) {
        return new DeploymentDescriptorParser(map);
    }

}

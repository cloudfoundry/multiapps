package com.sap.cloud.lm.sl.mta.handlers.v2;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.model.v2.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v2.ExtensionDescriptor;
import com.sap.cloud.lm.sl.mta.parsers.v2.DeploymentDescriptorParser;
import com.sap.cloud.lm.sl.mta.parsers.v2.ExtensionDescriptorParser;
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
        // TODO: Java 9 - Remove the second variable (https://blogs.oracle.com/darcy/more-concise-try-with-resources-statements-in-jdk-9).
        try (InputStream closableYaml = yaml) {
            return parseExtensionDescriptor(YamlUtil.convertYamlToMap(closableYaml));
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
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
        // TODO: Java 9 - Remove the second variable (https://blogs.oracle.com/darcy/more-concise-try-with-resources-statements-in-jdk-9).
        try (InputStream closableYaml = yaml) {
            return parseDeploymentDescriptor(YamlUtil.convertYamlToMap(closableYaml));
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
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

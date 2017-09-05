package com.sap.cloud.lm.sl.mta.tags;

import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;

public class YamlTaggedObjectsConstructor extends Constructor {

    private static final String SENSITIVE_TAG = "!sensitive";

    public YamlTaggedObjectsConstructor() {
        super();
        this.yamlConstructors.put(new Tag(SENSITIVE_TAG), new SecureConstruct());
    }
}

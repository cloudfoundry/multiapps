package org.cloudfoundry.multiapps.common.tags;

import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.nodes.Tag;

public class YamlTaggedObjectsConstructor extends SafeConstructor {

    private static final String SENSITIVE_TAG = "!sensitive";

    public YamlTaggedObjectsConstructor() {
        this.yamlConstructors.put(new Tag(SENSITIVE_TAG), new SecureConstruct());
    }
}

package com.sap.cloud.lm.sl.mta.tags;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.nodes.Tag;

public class YamlTaggedObjectsConstructor extends SafeConstructor {

    private static final String SENSITIVE_TAG = "!sensitive";

    public YamlTaggedObjectsConstructor(LoaderOptions loaderOptions) {
        super(loaderOptions);
        this.yamlConstructors.put(new Tag(SENSITIVE_TAG), new SecureConstruct());
    }
}

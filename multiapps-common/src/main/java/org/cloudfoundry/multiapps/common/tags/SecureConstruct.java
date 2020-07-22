package org.cloudfoundry.multiapps.common.tags;

import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;

public class SecureConstruct extends AbstractConstruct {
    @Override
    public Object construct(Node node) {
        ScalarNode scalarNode = (ScalarNode) node;
        return new SecureObject(scalarNode.getValue());
    }
}

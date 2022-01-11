package org.cloudfoundry.multiapps.mta.handlers.v2;

import org.cloudfoundry.multiapps.mta.model.Resource;

import java.util.List;
import java.util.Map;

public class ResourceBatchCalculator {

    public Map<Integer, List<Resource>> groupResourcesByWeight(List<Resource> resources) {
        return Map.of(1, resources);
    }
}

package org.cloudfoundry.multiapps.mta.handlers.v2;

import org.cloudfoundry.multiapps.mta.model.Resource;

import java.util.List;

public class SelectiveDeployChecker {

    public void check(List<Resource> resourcesForDeployment) {
        // Required since selective deploy with schema version 2 should not check dependent resources
    }
}

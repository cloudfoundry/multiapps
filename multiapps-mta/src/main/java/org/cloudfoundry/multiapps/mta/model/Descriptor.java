package org.cloudfoundry.multiapps.mta.model;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public interface Descriptor {

    String getId();

    static List<String> getIds(Collection<? extends Descriptor> descriptors) {
        return descriptors.stream()
                          .map(Descriptor::getId)
                          .collect(Collectors.toList());
    }

}

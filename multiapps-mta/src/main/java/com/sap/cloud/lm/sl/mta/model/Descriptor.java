package com.sap.cloud.lm.sl.mta.model;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public interface Descriptor {

    static List<String> getIds(Collection<? extends Descriptor> descriptors) {
        return descriptors.stream()
                          .map(Descriptor::getId)
                          .collect(Collectors.toList());
    }

    String getId();

}

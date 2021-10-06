package org.cloudfoundry.multiapps.mta.handlers.v3;

import org.cloudfoundry.multiapps.mta.model.WeightedResource;

import java.util.Comparator;

public class ResourceComparator implements Comparator<WeightedResource> {

    @Override
    public int compare(WeightedResource o1, WeightedResource o2) {
        int weightComparing = Integer.compare(o1.getResourceWeight(), o2.getResourceWeight());
        if (weightComparing != 0) {
            return weightComparing;
        }
        return o1.getResourceName()
                 .compareTo(o2.getResourceName());
    }
}

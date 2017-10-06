package com.sap.activiti.common.groupers;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sap.activiti.common.groupers.filters.AbstractFilter;
import com.sap.activiti.common.groupers.filters.FilterTree;

public class GeneralFilterGrouper<T> implements IGrouper<T> {

    private FilterTree<T> filterTree;

    public GeneralFilterGrouper(FilterTree<T> filterTree) {
        this.filterTree = filterTree;
    }

    private Map<String, List<T>> group(List<T> instances, AbstractFilter<T> filter) {

        Map<String, List<T>> groupedInstances = new HashMap<String, List<T>>();

        List<T> positive = filter.filterInstances(instances);
        instances.removeAll(positive);
        List<T> negative = instances;

        if (hasNextPositiveFilter(filter)) {
            groupedInstances.putAll(group(positive, filter.getPositiveGroupFilter()));
        } else {
            groupedInstances.put(filter.getPositiveGroupName(), positive);
        }
        if (hasNextNegativeFilter(filter)) {
            groupedInstances.putAll(group(negative, filter.getNegativeGroupFilter()));
        } else {
            groupedInstances.put(filter.getNegativeGroupName(), negative);
        }

        return groupedInstances;
    }

    private boolean hasNextNegativeFilter(AbstractFilter<T> filter) {
        return filter.getNegativeGroupFilter() != null;
    }

    private boolean hasNextPositiveFilter(AbstractFilter<T> filter) {
        return filter.getPositiveGroupFilter() != null;
    }

    @Override
    public Map<String, List<T>> doGroup(List<T> instances) {
        if (instances == null) {
            throw new IllegalArgumentException("Grouping set cannot be null.");
        }

        if (instances.isEmpty()) {
            return Collections.emptyMap();
        }

        return group(instances, filterTree.getRootFilter());
    }

    @Override
    public String getGroupName() {
        return filterTree.getFilterCriteriaName();
    }
}

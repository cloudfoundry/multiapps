package com.sap.activiti.common.groupers.filters;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFilter<T> {

    private AbstractFilter<T> positiveGroupFilter;
    private AbstractFilter<T> negativeGroupFilter;

    public abstract String getPositiveGroupName();

    public abstract String getNegativeGroupName();

    public abstract boolean isAccepted(T instance);

    public List<T> filterInstances(List<T> instances) {

        List<T> filteredInstances = new ArrayList<T>();

        for (T instance : instances) {
            if (isAccepted(instance)) {
                filteredInstances.add(instance);
            }
        }
        return filteredInstances;
    }

    public AbstractFilter<T> getPositiveGroupFilter() {
        return positiveGroupFilter;
    }

    public void setPositiveGroupFilter(AbstractFilter<T> positiveGroupFilter) {
        this.positiveGroupFilter = positiveGroupFilter;
    }

    public AbstractFilter<T> getNegativeGroupFilter() {
        return negativeGroupFilter;
    }

    public void setNegativeGroupFilter(AbstractFilter<T> negativeGroupFilter) {
        this.negativeGroupFilter = negativeGroupFilter;
    }
}
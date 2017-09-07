package com.sap.activiti.common.groupers.filters;


public class FilterTree<T> {

    private AbstractFilter<T> rootFilter;
    private String filterCriteriaName;

    public FilterTree(AbstractFilter<T> rootFilter, String filterCriteriaName) {
        this.rootFilter = rootFilter;
        this.filterCriteriaName = filterCriteriaName;
    }

    public String getFilterCriteriaName() {
        return filterCriteriaName;
    }

    public AbstractFilter<T> getRootFilter() {
        return rootFilter;
    }
}

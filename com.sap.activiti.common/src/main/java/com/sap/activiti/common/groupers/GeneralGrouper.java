package com.sap.activiti.common.groupers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sap.activiti.common.groupers.criteria.ICriteria;

public class GeneralGrouper<T> implements IGrouper<T> {

    private ICriteria<T> criteria;

    public GeneralGrouper(ICriteria<T> criteria) {
        this.criteria = criteria;
    }

    @Override
    public Map<String, List<T>> doGroup(List<T> instances) {

        if (instances == null) {
            throw new IllegalArgumentException("Initial grouping set cannot be null");
        }

        Map<String, List<T>> groupMap = new HashMap<String, List<T>>();

        for (T instance : instances) {
            String groupKey = criteria.getCriteria(instance);

            if (groupMap.containsKey(groupKey)) {
                groupMap.get(groupKey)
                    .add(instance);
            } else {
                ArrayList<T> newGroupList = new ArrayList<T>();
                newGroupList.add(instance);
                groupMap.put(groupKey, newGroupList);
            }
        }
        return groupMap;
    }

    @Override
    public String getGroupName() {
        return criteria.getCriteriaName();
    }
}
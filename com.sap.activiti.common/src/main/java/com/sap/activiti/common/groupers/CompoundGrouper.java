package com.sap.activiti.common.groupers;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CompoundGrouper<T> {

    private List<IGrouper<T>> groupers;
    private Map<List<Entry<String, String>>, List<T>> groupedInstances;

    public CompoundGrouper(List<IGrouper<T>> grouppers) {
        this.groupers = grouppers;
        this.groupedInstances = new HashMap<List<Entry<String, String>>, List<T>>();
    }

    public Map<List<Entry<String, String>>, List<T>> group(List<T> allInstances) {
        if (allInstances == null) {
            throw new IllegalArgumentException("Grouping set cannot be null.");
        }
        
        groupedInstances.put(new ArrayList<Entry<String, String>>(), allInstances);

        for (IGrouper<T> grouper : groupers) {
            Map<List<Entry<String, String>>, List<T>> newGroupedInstances = new HashMap<List<Entry<String, String>>, List<T>>();
            for (Entry<List<Entry<String, String>>, List<T>> groupEntry : groupedInstances
                    .entrySet()) {
                
                Map<String, List<T>> newGroup = grouper.doGroup(groupEntry
                        .getValue());

                List<Entry<String, String>> oldKey = groupEntry.getKey();

                for (Entry<String, List<T>> nextGroup : newGroup.entrySet()) {
                    List<Entry<String, String>> newKey = new ArrayList<Entry<String, String>>(
                            oldKey);
                    newKey.add(new SimpleEntry<String, String>(grouper.getGroupName(),
                            nextGroup.getKey()));
                    
                    newGroupedInstances.put(newKey, nextGroup.getValue());
                }
            }
            groupedInstances = newGroupedInstances;
        }
        return groupedInstances;
    }
}

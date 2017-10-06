package com.sap.cloud.lm.sl.common.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;

public class RangeCollection<KeyType extends Comparable<KeyType>, DataType> {

    private NavigableMap<KeyType, DataType> itemsMap;

    public RangeCollection(NavigableMap<KeyType, DataType> itemsMap) {
        this.itemsMap = itemsMap;
    }

    public List<DataType> getItems(KeyType lowLimit, KeyType highLimit) {
        if (itemsMap.isEmpty())
            return Collections.emptyList();

        Entry<KeyType, DataType> currentEntry = lowLimit == null ? itemsMap.firstEntry() : itemsMap.ceilingEntry(lowLimit);
        KeyType finalKey = highLimit == null ? itemsMap.lastKey() : itemsMap.floorKey(highLimit);

        if (currentEntry == null || finalKey == null) // all items are lower than the low limit or higher than the high limit
            return Collections.emptyList();

        List<DataType> result = new LinkedList<DataType>();
        while (currentEntry != null && currentEntry.getKey().compareTo(finalKey) <= 0) {
            result.add(currentEntry.getValue());
            currentEntry = itemsMap.higherEntry(currentEntry.getKey());
        }
        return result;
    }

    public List<DataType> getItems() {
        return new ArrayList<DataType>(itemsMap.values());
    }

}

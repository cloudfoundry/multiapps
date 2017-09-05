package com.sap.cloud.lm.sl.common.util;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class RangeCollectionTest<KeyType extends Comparable<KeyType>, DataType> {

    @SuppressWarnings("unchecked")
    private static Pair<Integer, String>[] odds = new Pair[] { new Pair<Integer, String>(1, "one"), new Pair<Integer, String>(3, "three"),
        new Pair<Integer, String>(5, "five"), new Pair<Integer, String>(7, "seven"), new Pair<Integer, String>(9, "nine") };
    @SuppressWarnings("unchecked")
    private static Pair<Integer, String>[] empty = new Pair[] {};
    
    
    @Parameters
    public static Collection<Object[]> data() {

        return Arrays.asList(new Object[][] { 
            { odds, 2, 4, new String[] { "three" } }, // odd numbers between 2 and 4
            { odds, 3, 3, new String[] { "three" } }, // odd numbers between 2 and 4            
            { empty, 2, 4, new String[] {} }, // empty collection
            { odds, 5, null, new String[] {"five", "seven", "nine" } }, // odd numbers starting from 5
            { odds, null, 5, new String[] {"one", "three", "five" } }, // odd numbers starting from 5            
            { odds, 11, 23, new String[] {} }, // range is not in the collection       
            { odds, 2, 2, new String[] {} },                
            { empty, 5, null, new String[] {} }, 
            { odds, 9, 11, new String[] {"nine"} },
            { odds, 0, 1, new String[] {"one"} },  
            { odds, -1, 0, new String[] {} },                                                    
        });
    }

    private KeyType lowLimit;
    private KeyType highLimit;
    private DataType[] expected;
    private RangeCollection<KeyType, DataType> rangeCollection;

    public RangeCollectionTest(Pair<KeyType, DataType>[] items, KeyType lowLimit, KeyType highLimit, DataType[] expected) {
        NavigableMap<KeyType, DataType> itemsMap = new TreeMap<KeyType, DataType>();
        for (Pair<KeyType, DataType> item : items) {            
            itemsMap.put(item._1, item._2);
        }
        this.rangeCollection = new RangeCollection<KeyType, DataType>(itemsMap);
        this.lowLimit = lowLimit;
        this.highLimit = highLimit;
        this.expected = expected;
    }

    @Test
    public void testGetItems() {
        List<DataType> items = rangeCollection.getItems(lowLimit, highLimit);
        assertEquals(Arrays.asList(expected), items);
    }

}

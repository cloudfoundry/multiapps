package com.sap.activiti.common.groupers;

import static com.sap.activiti.common.groupers.CompoundGrouperPageObject.containsInAnyOrder;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.Matchers.is;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;

import com.sap.activiti.common.groupers.BasicGrouperPageObject.TestUser;

public class CompoundGrouperTest {

    private CompoundGrouperPageObject pageObject;
    private CompoundGrouper<TestUser> compoundGrouper;

    @Before
    public void setup() {
        pageObject = new CompoundGrouperPageObject();
        compoundGrouper = pageObject.initCompoundGrouper();
        pageObject.setup();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGroupingSetIsNull() {
        compoundGrouper.group(null);
    }
    
    @Test
    public void testGroupingSetIsEmpty() {
        Map<List<Entry<String, String>>, List<TestUser>> groupedInstances = compoundGrouper
                .group(Collections
                .<TestUser> emptyList());

        assertTrue("There shouldn't be grouped instances", groupedInstances.isEmpty());
    }
    
    @Test
    public void testCompoundGroupingByFilterAndCriteria() {

        Map<List<Entry<String, String>>, List<TestUser>> groupedInstances = compoundGrouper
                .group(pageObject.getUsers());

        assertThat(groupedInstances.entrySet().size(), is(4));
        assertThat(groupedInstances.keySet(), containsInAnyOrder(pageObject.getExpectedKeys()));
        assertThat(groupedInstances.values(), containsInAnyOrder(pageObject.getExpectedGroups()));
    }
}
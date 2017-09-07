package com.sap.activiti.common.groupers;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.sap.activiti.common.groupers.BasicGrouperPageObject.TestUser;
import com.sap.activiti.common.groupers.GeneralGrouperPageObject.TestUserAgeCriteria;

public class GeneralGrouperTest {
    
    private GeneralGrouper<TestUser> generalGrouper;
    private GeneralGrouperPageObject pageObject = new GeneralGrouperPageObject();
    
    @Before
    public void setup() {
        generalGrouper = new GeneralGrouper<TestUser>(new TestUserAgeCriteria());
        pageObject.setup();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGroupingSetIsNull() {
        generalGrouper.doGroup(null);
    }
    
    @Test
    public void testGroupingSetIsEmpty() {
        Map<String, List<TestUser>> groupedInstances = generalGrouper.doGroup(Collections
                .<TestUser> emptyList());

        assertTrue("There shouldn't be grouped instances", groupedInstances.isEmpty());
    }

    @Test
    public void testGroupingByCriteria() {
        Map<String, List<TestUser>> groupedInstances = generalGrouper
                .doGroup(pageObject.getUsers());

        assertThat(groupedInstances.entrySet().size(), is(3));
        assertThat(groupedInstances.keySet(), containsInAnyOrder("17", "25", "35"));

        assertThat(groupedInstances.get("17").size(), is(2));
        assertThat(pageObject.getGroupNames(groupedInstances.get("17")),
                containsInAnyOrder("Ivan", "Elena"));

        assertThat(groupedInstances.get("25").size(), is(2));
        assertThat(pageObject.getGroupNames(groupedInstances.get("25")),
                containsInAnyOrder("John", "Andrey"));

        assertThat(groupedInstances.get("35").size(), is(1));
        assertThat(pageObject.getGroupNames(groupedInstances.get("35")),
 containsInAnyOrder("Lora"));
    }
}
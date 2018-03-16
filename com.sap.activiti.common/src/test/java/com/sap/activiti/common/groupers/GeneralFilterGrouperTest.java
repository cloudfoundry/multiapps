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
import com.sap.activiti.common.groupers.GeneralFilterGrouperPageObject.GenderFilter;
import com.sap.activiti.common.groupers.filters.AbstractFilter;
import com.sap.activiti.common.groupers.filters.FilterTree;

public class GeneralFilterGrouperTest {

    private GeneralFilterGrouper<TestUser> generalGrouper;
    private GeneralFilterGrouperPageObject pageObject;

    @Before
    public void setup() {
        AbstractFilter<TestUser> rootFilter = new GenderFilter();
        FilterTree<TestUser> filterTree = new FilterTree<TestUser>(rootFilter, "Gender");
        generalGrouper = new GeneralFilterGrouper<TestUser>(filterTree);
        pageObject = new GeneralFilterGrouperPageObject();
        pageObject.setup();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGroupingSetIsNull() {
        generalGrouper.doGroup(null);
    }

    @Test
    public void testGroupingSetIsEmpty() {
        Map<String, List<TestUser>> groupedInstances = generalGrouper.doGroup(Collections.<TestUser> emptyList());

        assertTrue("There shouldn't be grouped instances", groupedInstances.isEmpty());
    }

    @Test
    public void testGroupingByFilter() {
        Map<String, List<TestUser>> groupedInstances = generalGrouper.doGroup(pageObject.getUsers());

        assertThat(groupedInstances.entrySet()
            .size(), is(2));
        assertThat(groupedInstances.keySet(), containsInAnyOrder("Male", "Female"));

        assertThat(groupedInstances.get("Male")
            .size(), is(3));
        assertThat(pageObject.getGroupNames(groupedInstances.get("Male")), containsInAnyOrder("Ivan", "Andrey", "John"));

        assertThat(groupedInstances.get("Female")
            .size(), is(2));
        assertThat(pageObject.getGroupNames(groupedInstances.get("Female")), containsInAnyOrder("Elena", "Lora"));
    }
}
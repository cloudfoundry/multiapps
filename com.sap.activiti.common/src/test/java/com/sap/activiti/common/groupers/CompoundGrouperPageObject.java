package com.sap.activiti.common.groupers;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import com.sap.activiti.common.groupers.GeneralFilterGrouperPageObject.GenderFilter;
import com.sap.activiti.common.groupers.GeneralGrouperPageObject.TestUserAgeCriteria;
import com.sap.activiti.common.groupers.filters.AbstractFilter;
import com.sap.activiti.common.groupers.filters.FilterTree;

public class CompoundGrouperPageObject extends BasicGrouperPageObject {

    @SuppressWarnings("unchecked")
    public List<List<Entry<String, String>>> getExpectedKeys() {
        List<Entry<String, String>> firstKey = new ArrayList<Entry<String,String>>();
        firstKey.add(new SimpleEntry<String, String>("Gender", "Male"));
        firstKey.add(new SimpleEntry<String, String>("Age", "17"));

        List<Entry<String, String>> secondKey = new ArrayList<Entry<String,String>>();
        secondKey.add(new SimpleEntry<String, String>("Gender", "Male"));
        secondKey.add(new SimpleEntry<String, String>("Age", "25"));
        
        List<Entry<String, String>> thirdKey = new ArrayList<Entry<String,String>>();
        thirdKey.add(new SimpleEntry<String, String>("Gender", "Female"));
        thirdKey.add(new SimpleEntry<String, String>("Age", "17"));
        
        List<Entry<String, String>> fourthKey = new ArrayList<Entry<String,String>>();
        fourthKey.add(new SimpleEntry<String, String>("Gender", "Female"));
        fourthKey.add(new SimpleEntry<String, String>("Age", "35"));
        
        return Arrays.asList(firstKey, secondKey, thirdKey, fourthKey);
    }

    @SuppressWarnings("unchecked")
    public List<List<TestUser>> getExpectedGroups() {
        List<TestUser> firstValue = new ArrayList<TestUser>();
        firstValue.add(new TestUser("Ivan", "17", "M"));

        List<TestUser> secondValue = new ArrayList<TestUser>();
        secondValue.add(new TestUser("Elena", "17", "F"));

        List<TestUser> thirdValue = new ArrayList<TestUser>();
        thirdValue.add(new TestUser("Lora", "35", "F"));

        List<TestUser> fourthValue = new ArrayList<TestUser>();
        fourthValue.add(new TestUser("Andrey", "25", "M"));
        fourthValue.add(new TestUser("John", "25", "M"));

        return Arrays.asList(firstValue, secondValue, thirdValue, fourthValue);
    }

    @SuppressWarnings("unchecked")
    CompoundGrouper<TestUser> initCompoundGrouper() {
        AbstractFilter<TestUser> rootFilter = new GenderFilter();
        FilterTree<TestUser> filterTree = new FilterTree<TestUser>(rootFilter, "Gender");
        GeneralFilterGrouper<TestUser> generalFilterGrouper = new GeneralFilterGrouper<TestUser>(
                filterTree);

        GeneralGrouper<TestUser> generalGrouper = new GeneralGrouper<TestUser>(
                new TestUserAgeCriteria());

        return new CompoundGrouper<TestUser>(Arrays.asList(generalFilterGrouper, generalGrouper));
    }

    static <T extends Collection<?>> Matcher<Collection<T>> containsInAnyOrder(
            final List<T> expectedKeys) {

        return new BaseMatcher<Collection<T>>() {

            @SuppressWarnings("unchecked")
            @Override
            public boolean matches(Object items) {
                for (T actualKey : (Collection<T>) items) {
                    if (!contains(actualKey, expectedKeys)) {
                        return false;
                    }
                }
                return true;
            }

            private boolean contains(T actualKey, List<T> expectedKeys) {
                for (T expectedKey : expectedKeys) {
                    if (expectedKey.containsAll(actualKey)) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }
}
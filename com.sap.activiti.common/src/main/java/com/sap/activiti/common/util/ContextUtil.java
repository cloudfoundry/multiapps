package com.sap.activiti.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.activiti.engine.delegate.DelegateExecution;

public class ContextUtil {

    public static String[] getArrayVariable(DelegateExecution context, String name) {
        return GsonHelper.getFromBinaryJson((byte[]) context.getVariable(name), String[].class);
    }

    public static List<String> getArrayVariableAsList(DelegateExecution context, String name) {
        return Arrays.asList(getArrayVariable(context, name));
    }

    public static Set<String> getArrayVariableAsSet(DelegateExecution context, String name) {
        return new HashSet<String>(Arrays.asList(getArrayVariable(context, name)));
    }

    public static void setArrayVariable(DelegateExecution context, String name, String[] array) {
        context.setVariable(name, GsonHelper.getAsBinaryJson(array));
    }

    public static void setArrayVariableFromCollection(DelegateExecution context, String name, Collection<String> collection) {
        setArrayVariable(context, name, collection.toArray(new String[collection.size()]));
    }

    public static void addToArrayVariable(DelegateExecution context, String name, String value) {
        List<String> list = new ArrayList<String>(getArrayVariableAsList(context, name));
        list.add(value);
        setArrayVariableFromCollection(context, name, list);
    }

    public static void removeFromArrayVariable(DelegateExecution context, String name, String value) {
        List<String> list = new ArrayList<String>(getArrayVariableAsList(context, name));
        list.remove(value);
        setArrayVariableFromCollection(context, name, list);
    }

    public static String getArrayVariableElementAtIndex(DelegateExecution context, String arrayName, String indexName) {
        List<String> list = ContextUtil.getArrayVariableAsList(context, arrayName);
        int index = (Integer) context.getVariable(indexName);
        return list.get(index);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getVariable(DelegateExecution context, String name, T defaultValue) {
        T value = (T) context.getVariable(name);
        return (value != null) ? value : defaultValue;
    }

    public static void incrementVariable(DelegateExecution context, String name) {
        int value = (Integer) context.getVariable(name);
        context.setVariable(name, value + 1);
    }

    public static void decrementVariable(DelegateExecution context, String name) {
        int value = (Integer) context.getVariable(name);
        context.setVariable(name, value - 1);
    }

    public static void setAsBinaryJson(DelegateExecution context, String name, Object object) {
        context.setVariable(name, GsonHelper.getAsBinaryJson(object));
    }
}

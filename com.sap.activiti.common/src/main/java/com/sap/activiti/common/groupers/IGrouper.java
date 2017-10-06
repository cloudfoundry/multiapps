package com.sap.activiti.common.groupers;

import java.util.List;
import java.util.Map;

public interface IGrouper<T> {

    Map<String, List<T>> doGroup(List<T> instances);

    String getGroupName();
}

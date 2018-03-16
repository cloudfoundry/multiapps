package com.sap.activiti.common.groupers.criteria;

public interface ICriteria<T> {

    String getCriteria(T instance);

    String getCriteriaName();
}

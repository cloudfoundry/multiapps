package com.sap.activiti.common.groupers;

import com.sap.activiti.common.groupers.criteria.ICriteria;

public class GeneralGrouperPageObject extends BasicGrouperPageObject {

    static class TestUserAgeCriteria implements ICriteria<TestUser> {

        @Override
        public String getCriteria(TestUser instance) {
            return instance.age;
        }

        @Override
        public String getCriteriaName() {
            return "Age";
        }
    }
}

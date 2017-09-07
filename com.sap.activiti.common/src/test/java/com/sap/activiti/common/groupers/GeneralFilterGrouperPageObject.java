package com.sap.activiti.common.groupers;

import com.sap.activiti.common.groupers.filters.AbstractFilter;

public class GeneralFilterGrouperPageObject extends BasicGrouperPageObject {

    static class GenderFilter extends AbstractFilter<TestUser> {

        @Override
        public String getPositiveGroupName() {
            return "Male";
        }

        @Override
        public String getNegativeGroupName() {
            return "Female";
        }

        @Override
        public boolean isAccepted(TestUser instance) {
            return "M".equalsIgnoreCase(instance.gender);
        }
    }
}

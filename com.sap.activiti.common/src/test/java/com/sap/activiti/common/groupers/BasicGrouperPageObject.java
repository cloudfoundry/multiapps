package com.sap.activiti.common.groupers;

import java.util.ArrayList;
import java.util.List;

public class BasicGrouperPageObject {

    private List<TestUser> users;

    public void setup() {
        users = new ArrayList<TestUser>();
        users.add(new TestUser("Ivan", "17", "M"));
        users.add(new TestUser("John", "25", "M"));
        users.add(new TestUser("Lora", "35", "F"));
        users.add(new TestUser("Andrey", "25", "M"));
        users.add(new TestUser("Elena", "17", "F"));
    }

    public List<TestUser> getUsers() {
        return users;
    }

    public List<String> getGroupNames(List<TestUser> userList) {
        List<String> usernames = new ArrayList<String>();

        for (TestUser user : userList) {
            usernames.add(user.name);
        }
        return usernames;
    }

    static class TestUser {

        String name;
        String age;
        String gender;

        public TestUser(String name, String age, String gender) {
            this.name = name;
            this.age = age;
            this.gender = gender;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((age == null) ? 0 : age.hashCode());
            result = prime * result + ((gender == null) ? 0 : gender.hashCode());
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            TestUser other = (TestUser) obj;
            if (age == null) {
                if (other.age != null)
                    return false;
            } else if (!age.equals(other.age))
                return false;
            if (gender == null) {
                if (other.gender != null)
                    return false;
            } else if (!gender.equals(other.gender))
                return false;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            return true;
        }
    }
}

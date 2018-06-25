package com.sap.cloud.lm.sl.mta.model.v2_0;

import static com.sap.cloud.lm.sl.common.util.CommonUtil.getOrDefault;

import java.util.Collections;
import java.util.List;

import com.sap.cloud.lm.sl.mta.parsers.v2_0.ProvidedDependencyParser;
import com.sap.cloud.lm.sl.mta.util.YamlElement;

public class ProvidedDependency extends com.sap.cloud.lm.sl.mta.model.v1_0.ProvidedDependency {

    @YamlElement(ProvidedDependencyParser.PUBLIC)
    private boolean isPublic;

    protected ProvidedDependency() {

    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    @Override
    public List<String> getGroups() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setGroups(List<String> groups) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ProvidedDependency copyOf() {
        Builder result = new Builder();
        result.setName(getName());
        result.setPublic(isPublic());
        result.setProperties(getProperties());
        return result.build();
    }

    public static class Builder extends com.sap.cloud.lm.sl.mta.model.v1_0.ProvidedDependency.Builder {

        protected Boolean isPublic;

        @Override
        public ProvidedDependency build() {
            ProvidedDependency result = new ProvidedDependency();
            result.setName(name);
            result.setPublic(getOrDefault(isPublic, true));
            result.setProperties(getOrDefault(properties, Collections.<String, Object> emptyMap()));
            return result;
        }

        public void setPublic(Boolean isPublic) {
            this.isPublic = isPublic;
        }

        @Override
        public void setGroups(List<String> groups) {
            throw new UnsupportedOperationException();
        }

    }

}

package com.sap.cloud.lm.sl.mta.model.v1;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;

import com.sap.cloud.lm.sl.common.util.MapUtil;
import com.sap.cloud.lm.sl.mta.model.ElementContext;
import com.sap.cloud.lm.sl.mta.model.NamedElement;
import com.sap.cloud.lm.sl.mta.model.PropertiesContainer;
import com.sap.cloud.lm.sl.mta.model.VisitableElement;
import com.sap.cloud.lm.sl.mta.model.Visitor;

public class ModuleType implements VisitableElement, NamedElement, PropertiesContainer {

    private String name;
    private String deployer;
    private Map<String, Object> properties;

    protected ModuleType() {

    }

    public String getName() {
        return name;
    }

    public String getDeployer() {
        return deployer;
    }

    @Override
    public Map<String, Object> getProperties() {
        return MapUtil.unmodifiable(properties);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDeployer(String deployer) {
        this.deployer = deployer;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = new LinkedHashMap<>(properties);
    }

    @Override
    public void accept(ElementContext context, Visitor visitor) {
        visitor.visit(context, this);
    }

    public static class Builder {

        protected String name;
        protected String deployer;
        protected Map<String, Object> properties;

        public ModuleType build() {
            ModuleType result = new ModuleType();
            result.setName(name);
            result.setDeployer(deployer);
            result.setProperties(ObjectUtils.defaultIfNull(properties, Collections.<String, Object> emptyMap()));
            return result;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setDeployer(String deployer) {
            this.deployer = deployer;
        }

        public void setProperties(Map<String, Object> properties) {
            this.properties = properties;
        }

    }

}

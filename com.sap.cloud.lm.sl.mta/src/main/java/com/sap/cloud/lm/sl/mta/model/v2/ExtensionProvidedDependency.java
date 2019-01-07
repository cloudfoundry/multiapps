package com.sap.cloud.lm.sl.mta.model.v2;

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

public class ExtensionProvidedDependency implements VisitableElement, NamedElement, PropertiesContainer {

    //TODO: implement ParametersContainer ?
    private String name;
    private Map<String, Object> properties;
    
    protected ExtensionProvidedDependency() {

    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public static class Builder {
        
        protected String name;
        protected Map<String, Object> properties;

        public ExtensionProvidedDependency build() {
            ExtensionProvidedDependency result = new ExtensionProvidedDependency();
            result.setName(name);
            result.setProperties(ObjectUtils.defaultIfNull(properties, Collections.<String, Object> emptyMap()));
            return result;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public void setProperties(Map<String, Object> properties) {
            this.properties = properties;
        }

    }

    @Override
    public Map<String, Object> getProperties() {
        return MapUtil.unmodifiable(properties);
    }

    @Override
    public void setProperties(Map<String, Object> properties) {
        this.properties = new LinkedHashMap<>(properties);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void accept(ElementContext context, Visitor visitor) {
        visitor.visit(context, this);
    }
}

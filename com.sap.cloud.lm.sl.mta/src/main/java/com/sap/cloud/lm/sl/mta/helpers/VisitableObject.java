package com.sap.cloud.lm.sl.mta.helpers;

import static com.sap.cloud.lm.sl.mta.util.ValidatorUtil.getPrefixedName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class VisitableObject {

    private final Object object;

    public VisitableObject(Object object) {
        this.object = object;
    }

    public Object accept(String key, SimplePropertyVisitor visitor) {
        return accept(key, this.object, visitor);
    }

// @formatter:off
    public Object accept(SimplePropertyVisitor visitor) {
        return accept("" , this.object, visitor);
    }

    @SuppressWarnings("unchecked")
    private Object accept(String key, Object value, SimplePropertyVisitor visitor) {
        if (value instanceof Collection) {
            return acceptInternal(key, (Collection<Object>) value, visitor);
        } else if (value instanceof Map) {
            return acceptInternal(key, (Map<String,Object>) value, visitor);
        } else if (value instanceof String) {
            return acceptInternal(key, (String) (value), visitor);
        } else {
            return value;
        }
    }

    private Object acceptInternal(String key, Map<String,Object> value, SimplePropertyVisitor visitor) {
        Map<String, Object> result = new TreeMap<>();
        for (Map.Entry<String, Object> entry : value.entrySet()) {
            result.put(entry.getKey(), accept(getPrefixedName(key, entry.getKey()), entry.getValue(), visitor));
        }
        return result;
    }
// @formatter:on

    private Object acceptInternal(String key, Collection<Object> value, SimplePropertyVisitor visitor) {
        List<Object> result = new ArrayList<>();
        int i = 0;
        for (Object element : value) {
            result.add(accept(getPrefixedName(key, Integer.toString(i++)), element, visitor));
        }
        return result;
    }

    private Object acceptInternal(String key, String value, SimplePropertyVisitor visitor) {
        return visitor.visit(key, value);
    }

}
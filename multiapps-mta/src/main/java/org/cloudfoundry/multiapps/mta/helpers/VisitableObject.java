package org.cloudfoundry.multiapps.mta.helpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.cloudfoundry.multiapps.mta.util.NameUtil;

public class VisitableObject {

    private final Object object;

    public VisitableObject(Object object) {
        this.object = object;
    }

    public Object accept(String key, SimplePropertyVisitor visitor) {
        return accept(key, this.object, visitor);
    }

    public Object accept(SimplePropertyVisitor visitor) {
        return accept("", this.object, visitor);
    }

    @SuppressWarnings("unchecked")
    private Object accept(String key, Object value, SimplePropertyVisitor visitor) {
        if (value instanceof Collection) {
            return acceptInternal(key, (Collection<?>) value, visitor);
        } else if (value instanceof Map) {
            return acceptInternal(key, (Map<String, ?>) value, visitor);
        } else if (value instanceof String) {
            return acceptInternal(key, (String) value, visitor);
        }
        return value;
    }

    private Object acceptInternal(String key, Map<String, ?> value, SimplePropertyVisitor visitor) {
        Map<String, Object> result = new TreeMap<>();
        for (Map.Entry<String, ?> entry : value.entrySet()) {
            result.put(entry.getKey(), accept(NameUtil.getPrefixedName(key, entry.getKey()), entry.getValue(), visitor));
        }
        return result;
    }

    private Object acceptInternal(String key, Collection<?> value, SimplePropertyVisitor visitor) {
        List<Object> result = new ArrayList<>();
        int i = 0;
        for (Object element : value) {
            result.add(accept(NameUtil.getPrefixedName(key, Integer.toString(i++)), element, visitor));
        }
        return result;
    }

    private Object acceptInternal(String key, String value, SimplePropertyVisitor visitor) {
        return visitor.visit(key, value);
    }

}
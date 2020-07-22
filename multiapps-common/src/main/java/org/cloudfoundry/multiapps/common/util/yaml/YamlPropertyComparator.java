package org.cloudfoundry.multiapps.common.util.yaml;

import java.util.Comparator;
import java.util.List;

import org.yaml.snakeyaml.introspector.Property;

public class YamlPropertyComparator implements Comparator<Property> {

    private List<String> fieldOrder;

    YamlPropertyComparator(List<String> fieldOrder) {
        this.fieldOrder = fieldOrder;
    }

    @Override
    public int compare(Property o1, Property o2) {
        if (fieldOrder.contains(o1.getName()) && fieldOrder.contains(o2.getName())) {
            return fieldOrder.indexOf(o1.getName()) > fieldOrder.indexOf(o2.getName()) ? 1 : -1;
        }
        if (!fieldOrder.contains(o1.getName()) && !fieldOrder.contains(o2.getName())) {
            return o1.getName()
                     .compareTo(o2.getName());
        }
        if (fieldOrder.contains(o1.getName())) {
            return -1;
        }
        return 1;
    }
}

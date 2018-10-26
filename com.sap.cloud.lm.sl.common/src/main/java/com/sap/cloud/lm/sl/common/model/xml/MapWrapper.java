package com.sap.cloud.lm.sl.common.model.xml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "map")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso(MapWrapper.Entry.class)
public class MapWrapper extends Wrapper {

    @XmlElement(name = "entry")
    public List<Entry> map = new ArrayList<>();

    public MapWrapper() {
        // Required by JAXB
    }

    @SuppressWarnings("unchecked")
    public MapWrapper(Object o) {
        Map<String, Object> map = o == null ? Collections.<String, Object> emptyMap() : (Map<String, Object>) o;
        for (String key : map.keySet()) {
            this.map.add(new Entry(key, wrap(map.get(key))));
        }
    }

    @Override
    public Map<String, Object> unwrap() {
        Map<String, Object> result = new TreeMap<>();
        for (Entry entry : map) {
            result.put(entry.getKey(), unwrap(entry.getValue()));
        }
        return result;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Entry {

        @XmlElement
        private String key;
        @XmlElements({ @XmlElement(name = "value", type = String.class), @XmlElement(name = "list", type = ListWrapper.class),
            @XmlElement(name = "map", type = MapWrapper.class), })
        private Object value;

        public Entry() {
            // Required by JAXB
        }

        public Entry(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Object getValue() {
            return value;
        }

        public String getKey() {
            return key;
        }

    }

}

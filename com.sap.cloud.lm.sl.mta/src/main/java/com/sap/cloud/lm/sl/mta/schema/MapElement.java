package com.sap.cloud.lm.sl.mta.schema;

import java.util.HashMap;
import java.util.Map;

public class MapElement extends Element {

    private final Map<String, Element> map;

    public MapElement() {
        this(new ElementBuilder(), new HashMap<>());
    }

    public MapElement(ElementBuilder builder) {
        this(builder, new HashMap<>());
    }

    public MapElement(ElementBuilder builder, Map<String, Element> map) {
        super(builder);
        this.map = map;
    }

    public Map<String, Element> getMap() {
        return map;
    }

    public void add(String key, Element element) {
        map.put(key, element);
    }

    @Override
    public Class<?> getType() {
        return Map.class;
    }

}

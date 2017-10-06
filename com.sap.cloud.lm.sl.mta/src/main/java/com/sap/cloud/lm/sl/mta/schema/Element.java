package com.sap.cloud.lm.sl.mta.schema;

import java.util.Map;

public class Element {

    private final boolean required;
    private final boolean unique;
    private final Class<?> type;
    private final String pattern;
    private final int maxLength;

    public Element(ElementBuilder builder) {
        this.required = builder.required;
        this.unique = builder.unique;
        this.type = builder.type;
        this.pattern = builder.pattern;
        this.maxLength = builder.maxLength;
    }

    public boolean isRequired() {
        return required;
    }

    public boolean isUnique() {
        return unique;
    }

    public Class<?> getType() {
        return type;
    }


    public String getPattern() {
        return pattern;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public static class ElementBuilder {

        private boolean required = false;
        private boolean unique = false;
        private Class<?> type = String.class;
        private String pattern = null;
        private int maxLength = 0;

        public ElementBuilder required(boolean required) {
            this.required = required;
            return this;
        }

        public ElementBuilder unique(boolean unique) {
            this.unique = unique;
            return this;
        }

        public ElementBuilder type(Class<?> type) {
            this.type = type;
            return this;
        }

        public ElementBuilder pattern(String pattern) {
            this.pattern = pattern;
            return this;
        }

        public ElementBuilder maxLength(int maxLength) {
            this.maxLength = maxLength;
            return this;
        }

        public Element buildSimple() {
            return new Element(this);
        }

        public ListElement buildList(Element element) {
            return new ListElement(this, element);
        }

        public MapElement buildMap() {
            return new MapElement(this);
        }

        public MapElement buildMap(Map<String, Element> map) {
            return new MapElement(this, map);
        }

    }

}

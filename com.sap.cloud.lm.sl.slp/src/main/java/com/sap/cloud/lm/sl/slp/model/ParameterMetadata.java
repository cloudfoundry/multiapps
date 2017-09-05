package com.sap.cloud.lm.sl.slp.model;

import com.sap.cloud.lm.sl.common.util.CommonUtil;

/**
 * Provides the metadata for a process parameter that is handled over SLP.
 */
public class ParameterMetadata {

    /**
     * Represents supported parameter types.
     */
    public enum ParameterType {
        STRING, INTEGER, BOOLEAN, TABLE
    }

    public static ParameterMetadataBuilder builder() {
        return new ParameterMetadataBuilder();
    }

    private String id;
    private boolean required;
    private boolean secure;
    private ParameterType type;
    private Object defaultValue;

    protected ParameterMetadata() {
    }

    public String getId() {
        return id;
    }

    public boolean isRequired() {
        return required;
    }

    public boolean isSecure() {
        return secure;
    }

    public ParameterType getType() {
        return type;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public boolean hasDefaultValue() {
        return defaultValue != null;
    }

    public static class ParameterMetadataBuilder {

        private String id;
        private Boolean required;
        private Boolean secure;
        private ParameterType type;
        private Object defaultValue;

        public ParameterMetadata build() {
            ParameterMetadata result = new ParameterMetadata();
            result.id = id;
            result.required = CommonUtil.getOrDefault(required, false);
            result.secure = CommonUtil.getOrDefault(secure, false);
            result.type = CommonUtil.getOrDefault(type, ParameterType.STRING);
            result.defaultValue = defaultValue;
            return result;
        }

        public ParameterMetadataBuilder id(String id) {
            this.id = id;
            return this;
        }

        public ParameterMetadataBuilder required(boolean isRequired) {
            this.required = isRequired;
            return this;
        }

        public ParameterMetadataBuilder secure(boolean isSecure) {
            this.secure = isSecure;
            return this;
        }

        public ParameterMetadataBuilder type(ParameterType type) {
            this.type = type;
            return this;
        }

        public ParameterMetadataBuilder defaultValue(Object defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

    }

}

package com.sap.cloud.lm.sl.common.tags;

public class SecureObject implements TaggedObject {

    private String value;

    public SecureObject(String value) {
        this.value = value;
    }

    @Override
    public String getName() {
        return "sensitive";
    }

    @Override
    public boolean getMetadataValue() {
        return true;
    }

    @Override
    public String getValue() {
        return value;
    }
}

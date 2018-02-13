package com.sap.cloud.lm.sl.mta.model;

public class ConfigurationIdentifier {

    private String identifierName;
    private String identifierValue;

    public ConfigurationIdentifier(String identifierName, String identifierValue) {
        this.identifierName = identifierName;
        this.identifierValue = identifierValue;
    }

    public String getIdentifierName() {
        return identifierName;
    }

    public void setIdentifierName(String identifierName) {
        this.identifierName = identifierName;
    }

    public String getIdentifierValue() {
        return identifierValue;
    }

    public void setIdentifierValue(String identifierValue) {
        this.identifierValue = identifierValue;
    }
}

package com.sap.cloud.lm.sl.mta.handlers.v3_0;

public class HandlerConstructor extends com.sap.cloud.lm.sl.mta.handlers.v2_0.HandlerConstructor {

    @Override
    public DescriptorParser getDescriptorParser() {
        return new DescriptorParser();
    }

    @Override
    public ConfigurationParser getConfigurationParser() {
        return new ConfigurationParser();
    }
}

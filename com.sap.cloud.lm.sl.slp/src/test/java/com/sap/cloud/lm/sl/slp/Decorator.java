package com.sap.cloud.lm.sl.slp;

public interface Decorator<T> {
    void decorate(T mock);
}

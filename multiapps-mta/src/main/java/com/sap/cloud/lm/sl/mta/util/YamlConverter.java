package com.sap.cloud.lm.sl.mta.util;

public interface YamlConverter<T, V> {

    V convert(T value);

}

package com.sap.cloud.lm.sl.common.util.yaml;

public interface YamlConverter<T, V> {

    V convert(T value);

}

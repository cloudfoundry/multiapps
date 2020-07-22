package org.cloudfoundry.multiapps.common.util.yaml;

public interface YamlConverter<T, V> {

    V convert(T value);

}

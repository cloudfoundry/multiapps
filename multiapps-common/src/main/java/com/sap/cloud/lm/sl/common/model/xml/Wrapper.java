package com.sap.cloud.lm.sl.common.model.xml;

import java.util.Collection;
import java.util.Map;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
@XmlSeeAlso({ ListWrapper.class, MapWrapper.class })
public abstract class Wrapper {

    public static Object wrap(Object o) {
        if (o instanceof Map) {
            return new MapWrapper(o);
        } else if (o instanceof Collection) {
            return new ListWrapper(o);
        } else {
            return o;
        }
    }

    public static Object unwrap(Object o) {
        if (o instanceof Wrapper) {
            return ((Wrapper) o).unwrap();
        } else {
            return o;
        }
    }

    public abstract Object unwrap();

}

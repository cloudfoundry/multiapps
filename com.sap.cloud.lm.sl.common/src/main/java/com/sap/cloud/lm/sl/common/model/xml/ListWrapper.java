package com.sap.cloud.lm.sl.common.model.xml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "list")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListWrapper extends Wrapper {

    @XmlElements({ @XmlElement(name = "value", type = String.class), @XmlElement(name = "list", type = ListWrapper.class),
        @XmlElement(name = "map", type = MapWrapper.class), })
    private List<Object> list = new ArrayList<>();

    public ListWrapper() {
        // Required by JAXB
    }

    @SuppressWarnings("unchecked")
    public ListWrapper(Object list) {
        for (Object element : (Collection<Object>) list) {
            this.list.add(wrap(element));
        }
    }

    @Override
    public List<Object> unwrap() {
        return list.stream()
                   .map(Wrapper::unwrap)
                   .collect(Collectors.toList());
    }

}

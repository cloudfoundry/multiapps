package org.cloudfoundry.multiapps.mta.schema;

import java.util.List;

public class ListElement extends Element {

    private final Element element;

    public ListElement(Element element) {
        this(new ElementBuilder(), element);
    }

    public ListElement(ElementBuilder elementBuilder, Element element) {
        super(elementBuilder);
        this.element = element;
    }

    public Element getElement() {
        return element;
    }

    @Override
    public Class<?> getType() {
        return List.class;
    }

}

package org.cloudfoundry.multiapps.mta.model;

public interface VisitableElement {

    void accept(ElementContext context, Visitor visitor);

}

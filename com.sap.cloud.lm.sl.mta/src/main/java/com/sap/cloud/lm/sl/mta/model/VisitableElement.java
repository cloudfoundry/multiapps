package com.sap.cloud.lm.sl.mta.model;

public interface VisitableElement {

    void accept(ElementContext context, Visitor visitor);

}

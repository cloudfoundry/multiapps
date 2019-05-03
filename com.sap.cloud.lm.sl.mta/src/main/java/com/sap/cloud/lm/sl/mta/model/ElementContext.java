package com.sap.cloud.lm.sl.mta.model;

import com.sap.cloud.lm.sl.mta.util.ValidatorUtil;

public class ElementContext {

    private VisitableElement visitableElement;
    private ElementContext previousElementContext;

    public ElementContext(VisitableElement visitableElement, ElementContext previousElementContext) {
        this.visitableElement = visitableElement;
        this.previousElementContext = previousElementContext;
    }

    public VisitableElement getVisitableElement() {
        return visitableElement;
    }

    public String getVisitableElementName() {
        if (visitableElement instanceof NamedElement) {
            return ((NamedElement) visitableElement).getName();
        }
        return null;
    }

    public ElementContext getPreviousElementContext() {
        return previousElementContext;
    }

    public String getPrefixedName() {
        if (previousElementContext != null) {
            String parentPrefix = previousElementContext.getPrefixedName();
            if (parentPrefix != null) {
                return ValidatorUtil.getPrefixedName(parentPrefix, getVisitableElementName());
            }
        }
        return getVisitableElementName();
    }

}

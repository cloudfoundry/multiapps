package com.sap.cloud.lm.sl.mta.helpers;

public interface SimplePropertyVisitor {

    Object visit(String key, String value);

}

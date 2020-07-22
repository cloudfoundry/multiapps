package org.cloudfoundry.multiapps.mta.helpers;

public interface SimplePropertyVisitor {

    Object visit(String key, String value);

}

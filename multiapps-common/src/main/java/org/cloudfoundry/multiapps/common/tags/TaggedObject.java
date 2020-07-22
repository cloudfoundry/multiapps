package org.cloudfoundry.multiapps.common.tags;

public interface TaggedObject {

    String getName();

    boolean getMetadataValue();

    String getValue();
}

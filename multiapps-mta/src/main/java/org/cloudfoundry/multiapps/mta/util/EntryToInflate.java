package org.cloudfoundry.multiapps.mta.util;

import java.io.InputStream;

public record EntryToInflate(String name, long maxSizeInBytes, InputStream inputStream) {

}

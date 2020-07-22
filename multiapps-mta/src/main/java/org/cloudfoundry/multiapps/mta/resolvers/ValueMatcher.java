package org.cloudfoundry.multiapps.mta.resolvers;

import java.util.List;

public interface ValueMatcher {

    List<Reference> match(String line);

}

package com.sap.cloud.lm.sl.mta.resolvers;

import java.util.List;

public interface ValueMatcher {

    public List<Reference> match(String line);

}

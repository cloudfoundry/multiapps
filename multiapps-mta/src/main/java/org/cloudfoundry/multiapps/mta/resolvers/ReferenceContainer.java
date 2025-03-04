package org.cloudfoundry.multiapps.mta.resolvers;

import java.util.List;

public class ReferenceContainer {

    private final String referenceOwner;
    private final List<Reference> references;

    public ReferenceContainer(String referenceOwner, List<Reference> references) {
        this.referenceOwner = referenceOwner;
        this.references = references;
    }

    public String getReferenceOwner() {
        return referenceOwner;
    }

    public List<Reference> getReferences() {
        return references;
    }

}

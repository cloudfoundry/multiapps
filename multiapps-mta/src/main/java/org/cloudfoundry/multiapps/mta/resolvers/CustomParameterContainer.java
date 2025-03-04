package org.cloudfoundry.multiapps.mta.resolvers;

import java.util.List;

public class CustomParameterContainer {

    private final String parameterOwner;
    private final List<String> customParameters;

    private final String prefixedName;

    public CustomParameterContainer(String parameterOwner, List<String> parameters, String prefixedName) {
        this.parameterOwner = parameterOwner;
        this.customParameters = parameters;
        this.prefixedName = prefixedName;
    }

    public String getParameterOwner() {
        return parameterOwner;
    }

    public String getPrefixedName() {
        return prefixedName;
    }

    public List<String> getParameters() {
        return customParameters;
    }

}

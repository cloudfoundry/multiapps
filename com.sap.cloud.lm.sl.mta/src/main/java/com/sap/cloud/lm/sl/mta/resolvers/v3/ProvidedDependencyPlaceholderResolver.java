package com.sap.cloud.lm.sl.mta.resolvers.v3;

import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.mta.builders.v2.ParametersChainBuilder;
import com.sap.cloud.lm.sl.mta.model.ParametersContainer;
import com.sap.cloud.lm.sl.mta.model.SystemParameters;
import com.sap.cloud.lm.sl.mta.model.v3.Module;
import com.sap.cloud.lm.sl.mta.model.v3.ProvidedDependency;
import com.sap.cloud.lm.sl.mta.resolvers.ResolverBuilder;
import com.sap.cloud.lm.sl.mta.util.PropertiesUtil;

public class ProvidedDependencyPlaceholderResolver extends com.sap.cloud.lm.sl.mta.resolvers.v2.ProvidedDependencyPlaceholderResolver {

    protected final ProvidedDependency providedDependency3;

    public ProvidedDependencyPlaceholderResolver(Module module, ProvidedDependency providedDependency, String prefix,
                                                 ParametersChainBuilder parametersChainBuilder, SystemParameters systemParameters,
                                                 ResolverBuilder propertiesResolverBuilder) {
        super(module, providedDependency, prefix, parametersChainBuilder, systemParameters, propertiesResolverBuilder);
        this.providedDependency3 = providedDependency;
    }

    @Override
    public ProvidedDependency resolve() throws ContentException {
        providedDependency3.setProperties(getResolvedProperties());
        providedDependency3.setParameters(getResolvedParameters());
        return providedDependency3;
    }

    private Map<String, Object> getResolvedParameters() {
        List<Map<String, Object>> parametersChain = getParametersChain();
        return resolve(((ParametersContainer) providedDependency).getParameters(), PropertiesUtil.mergeProperties(parametersChain));
    }

}

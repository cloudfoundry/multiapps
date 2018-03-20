package com.sap.cloud.lm.sl.mta.resolvers.v3_1;

import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.mta.builders.v2_0.ParametersChainBuilder;
import com.sap.cloud.lm.sl.mta.model.ParametersContainer;
import com.sap.cloud.lm.sl.mta.model.SystemParameters;
import com.sap.cloud.lm.sl.mta.model.v3_1.Module;
import com.sap.cloud.lm.sl.mta.model.v3_1.ProvidedDependency;
import com.sap.cloud.lm.sl.mta.resolvers.ResolverBuilder;
import com.sap.cloud.lm.sl.mta.util.PropertiesUtil;

public class ProvidedDependencyPlaceholderResolver extends com.sap.cloud.lm.sl.mta.resolvers.v2_0.ProvidedDependencyPlaceholderResolver {

    protected final ProvidedDependency providedDependency3_1;

    public ProvidedDependencyPlaceholderResolver(Module module, ProvidedDependency providedDependency, String prefix,
        ParametersChainBuilder parametersChainBuilder, SystemParameters systemParameters, ResolverBuilder propertiesResolverBuilder) {
        super(module, providedDependency, prefix, parametersChainBuilder, systemParameters, propertiesResolverBuilder);
        this.providedDependency3_1 = providedDependency;
    }

    @Override
    public ProvidedDependency resolve() throws ContentException {
        providedDependency3_1.setProperties(getResolvedProperties());
        providedDependency3_1.setParameters(getResolvedParameters());
        return providedDependency3_1;
    }

    private Map<String, Object> getResolvedParameters() {
        List<Map<String, Object>> parametersChain = getParametersChain();
        return resolve(((ParametersContainer) providedDependency).getParameters(), PropertiesUtil.mergeProperties(parametersChain));
    }
}

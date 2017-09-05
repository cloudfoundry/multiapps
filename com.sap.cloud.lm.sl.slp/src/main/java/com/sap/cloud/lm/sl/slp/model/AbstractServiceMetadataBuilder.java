package com.sap.cloud.lm.sl.slp.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.sap.cloud.lm.sl.common.util.CommonUtil;
import com.sap.cloud.lm.sl.slp.model.builder.ServiceMetadataBuilder;

abstract class AbstractServiceMetadataBuilder<T extends ServiceMetadata, BT extends ServiceMetadataBuilder>
    extends AbstractStepMetadataBuilder<T, BT>implements ServiceMetadataBuilder {

    private static final String DEFAULT_SERVICE_VERSION = "1.0";

    protected String implementationId;
    protected Set<ParameterMetadata> parameters;
    protected Boolean beta;
    protected String requiredPermission;
    protected List<String> versions;

    @Override
    public BT requiredPermission(String requiredPermission) {
        this.requiredPermission = requiredPermission;
        return getThis();
    }

    @Override
    public BT parameters(Set<ParameterMetadata> parameters) {
        this.parameters = parameters;
        return getThis();
    }

    @Override
    public BT implementationId(String implementationId) {
        this.implementationId = implementationId;
        return getThis();
    }

    @Override
    public BT beta(boolean beta) {
        this.beta = beta;
        return getThis();
    }

    @Override
    public BT versions(List<String> versions) {
        this.versions = versions;
        return getThis();
    }

    @Override
    public T build() {
        T meta = super.build();
        meta.visible = true;
        meta.implementationId = CommonUtil.getOrDefault(implementationId, id);
        meta.parameters = CommonUtil.getOrDefault(parameters, Collections.<ParameterMetadata> emptySet());
        meta.beta = CommonUtil.getOrDefault(beta, false);
        meta.versions = CommonUtil.getOrDefault(versions, Arrays.asList(DEFAULT_SERVICE_VERSION));
        meta.requiredPermission = requiredPermission;
        return meta;
    }

    @Override
    public BT versions(String... versions) {
        return this.versions(Arrays.asList(versions));
    }

}

package com.sap.cloud.lm.sl.mta.handlers;

import static java.text.MessageFormat.format;

import com.sap.cloud.lm.sl.mta.handlers.v2.ConfigurationParser;
import com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorMerger;
import com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorParser;
import com.sap.cloud.lm.sl.mta.handlers.v2.DescriptorValidator;
import com.sap.cloud.lm.sl.mta.message.Messages;
import com.sap.cloud.lm.sl.mta.model.SystemParameters;
import com.sap.cloud.lm.sl.mta.model.v2.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v2.Platform;
import com.sap.cloud.lm.sl.mta.resolvers.PlaceholderResolver;
import com.sap.cloud.lm.sl.mta.resolvers.Resolver;
import com.sap.cloud.lm.sl.mta.resolvers.ResolverBuilder;

public class HandlerFactory implements HandlerConstructor {

    public static final Integer HIGHEST_VERSION_INPUT = Integer.MAX_VALUE;

    protected int majorVersion;
    protected HandlerConstructor handlerDelegate;

    public HandlerFactory(int majorVersion) {
        this.majorVersion = majorVersion;
    }

    protected HandlerConstructor getHandlerDelegate() {
        if (handlerDelegate == null) {
            initDelegates();
        }
        return handlerDelegate;
    }

    protected void initDelegates() {
        switch (majorVersion) {
            case 2:
                initV2Delegates();
                break;
            case Integer.MAX_VALUE: // HandlerFactory.MAX_VERSION_SUPPORTED_INPUT
            case 3:
                initV3Delegates();
                break;
            default:
                throw new UnsupportedOperationException(format(Messages.UNSUPPORTED_VERSION, majorVersion));
        }
    }
    protected void initV2Delegates() {
        handlerDelegate = new com.sap.cloud.lm.sl.mta.handlers.v2.HandlerConstructor();
    }

    protected void initV3Delegates() {
        handlerDelegate = new com.sap.cloud.lm.sl.mta.handlers.v3.HandlerConstructor();
    }

    public int getMajorVersion() {
        return majorVersion;
    }

    @Override
    public DescriptorParser getDescriptorParser() {
        return getHandlerDelegate().getDescriptorParser();
    }

    @Override
    public DescriptorHandler getDescriptorHandler() {
        return getHandlerDelegate().getDescriptorHandler();
    }

    @Override
    public ConfigurationParser getConfigurationParser() {
        return getHandlerDelegate().getConfigurationParser();
    }

    @Override
    public DescriptorMerger getDescriptorMerger() {
        return getHandlerDelegate().getDescriptorMerger();
    }

    @Override
    public DescriptorValidator getDescriptorValidator() {
        return getHandlerDelegate().getDescriptorValidator();
    }

    @Override
    public PlaceholderResolver<? extends DeploymentDescriptor> getDescriptorPlaceholderResolver(final DeploymentDescriptor mergedDescriptor,
        Platform platform, SystemParameters systemParameters, ResolverBuilder propertiesResolver, ResolverBuilder parametersResolver) {
        return getHandlerDelegate().getDescriptorPlaceholderResolver(mergedDescriptor, platform, systemParameters,
            propertiesResolver, parametersResolver);
    }

    @Override
    public Resolver<? extends DeploymentDescriptor> getDescriptorReferenceResolver(final DeploymentDescriptor mergedDescriptor,
        ResolverBuilder modulesPropertiesResolverBuilder, ResolverBuilder resourcePropertiesResolverBuilder,
        ResolverBuilder requiredDepencenciesPropertiesResolverBuilder) {
        return getHandlerDelegate().getDescriptorReferenceResolver(mergedDescriptor, modulesPropertiesResolverBuilder,
            resourcePropertiesResolverBuilder, requiredDepencenciesPropertiesResolverBuilder);
    }

}

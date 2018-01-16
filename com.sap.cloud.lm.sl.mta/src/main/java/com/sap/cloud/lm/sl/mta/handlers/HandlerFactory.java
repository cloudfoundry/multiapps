package com.sap.cloud.lm.sl.mta.handlers;

import static java.text.MessageFormat.format;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.mta.handlers.v1_0.ConfigurationParser;
import com.sap.cloud.lm.sl.mta.handlers.v1_0.DescriptorHandler;
import com.sap.cloud.lm.sl.mta.handlers.v1_0.DescriptorMerger;
import com.sap.cloud.lm.sl.mta.handlers.v1_0.DescriptorParser;
import com.sap.cloud.lm.sl.mta.handlers.v1_0.DescriptorValidator;
import com.sap.cloud.lm.sl.mta.message.Messages;
import com.sap.cloud.lm.sl.mta.model.SystemParameters;
import com.sap.cloud.lm.sl.mta.model.v1_0.DeploymentDescriptor;
import com.sap.cloud.lm.sl.mta.model.v1_0.Platform;
import com.sap.cloud.lm.sl.mta.model.v1_0.Target;
import com.sap.cloud.lm.sl.mta.resolvers.PlaceholderResolver;
import com.sap.cloud.lm.sl.mta.resolvers.Resolver;
import com.sap.cloud.lm.sl.mta.resolvers.ResolverBuilder;

public class HandlerFactory implements HandlerConstructor {

    public static final Integer HIGHEST_VERSION_INPUT = Integer.MAX_VALUE;

    protected int majorVersion;
    protected int minorVersion;
    protected HandlerConstructor handlerDelegate;

    public HandlerFactory(int majorVersion) {
        this(majorVersion, 0);
    }

    public HandlerFactory(int majorVersion, int minorVersion) {
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
    }

    protected HandlerConstructor getHandlerDelegate() {
        if (handlerDelegate == null) {
            initDelegates();
        }
        return handlerDelegate;
    }

    protected void initDelegates() {
        switch (majorVersion) {
            case 1:
                initV1Delegates();
                break;
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

    protected void initV3Delegates() {
        switch (minorVersion) {
            case 0:
                initV3_0Delegates();
                break;
            case Integer.MAX_VALUE: // HandlerFactory.MAX_VERSION_SUPPORTED_INPUT
            case 1:
                initV3_1Delegates();
                break;
            default:
                throw new UnsupportedOperationException(format(Messages.UNSUPPORTED_VERSION, majorVersion + "." + minorVersion));
        }
    }

    protected void initV1Delegates() {
        handlerDelegate = new com.sap.cloud.lm.sl.mta.handlers.v1_0.HandlerConstructor();
    }

    protected void initV2Delegates() {
        handlerDelegate = new com.sap.cloud.lm.sl.mta.handlers.v2_0.HandlerConstructor();
    }

    protected void initV3_0Delegates() {
        handlerDelegate = new com.sap.cloud.lm.sl.mta.handlers.v3_0.HandlerConstructor();
    }

    protected void initV3_1Delegates() {
        handlerDelegate = new com.sap.cloud.lm.sl.mta.handlers.v3_1.HandlerConstructor();
    }

    public int getMajorVersion() {
        return majorVersion;
    }

    public int getMinorVersion() {
        return minorVersion;
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
        Platform platform, Target target, SystemParameters systemParameters, ResolverBuilder propertiesResolver,
        ResolverBuilder parametersResolver) {
        return getHandlerDelegate().getDescriptorPlaceholderResolver(mergedDescriptor, platform, target, systemParameters,
            propertiesResolver, parametersResolver);
    }

    @Override
    public Resolver<? extends DeploymentDescriptor, ContentException> getDescriptorReferenceResolver(
        final DeploymentDescriptor mergedDescriptor, ResolverBuilder modulesPropertiesResolverBuilder,
        ResolverBuilder resourcePropertiesResolverBuilder, ResolverBuilder requiredDepencenciesPropertiesResolverBuilder) {
        return getHandlerDelegate().getDescriptorReferenceResolver(mergedDescriptor, modulesPropertiesResolverBuilder,
            resourcePropertiesResolverBuilder, requiredDepencenciesPropertiesResolverBuilder);
    }

}

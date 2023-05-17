package org.cloudfoundry.multiapps.mta.resolvers.v2;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.cloudfoundry.multiapps.common.ContentException;
import org.cloudfoundry.multiapps.mta.builders.v2.ParametersChainBuilder;
import org.cloudfoundry.multiapps.mta.model.Module;
import org.cloudfoundry.multiapps.mta.model.ProvidedDependency;
import org.cloudfoundry.multiapps.mta.model.RequiredDependency;
import org.cloudfoundry.multiapps.mta.resolvers.PlaceholderResolver;
import org.cloudfoundry.multiapps.mta.resolvers.PropertiesPlaceholderResolver;
import org.cloudfoundry.multiapps.mta.resolvers.ResolverBuilder;
import org.cloudfoundry.multiapps.mta.util.PropertiesUtil;

public class ModulePlaceholderResolver extends PlaceholderResolver<Module> {

    protected final Module module;
    protected final ParametersChainBuilder parametersChainBuilder;
    protected final ResolverBuilder propertiesResolverBuilder;
    protected final ResolverBuilder parametersResolverBuilder;

    public ModulePlaceholderResolver(Module module, String prefix, ParametersChainBuilder parametersChainBuilder,
                                     ResolverBuilder propertiesResolverBuilder, ResolverBuilder parametersResolverBuilder,
                                     Map<String, String> singularToPluralMapping, Set<String> dynamicResolvableParameters) {
        super(module.getName(), prefix, singularToPluralMapping, dynamicResolvableParameters);
        this.module = module;
        this.parametersChainBuilder = parametersChainBuilder;
        this.propertiesResolverBuilder = propertiesResolverBuilder;
        this.parametersResolverBuilder = parametersResolverBuilder;
    }

    @Override
    public Module resolve() throws ContentException {
        Map<String, Object> mergedParameters = getMergedParameters();
        module.setProperties(getResolvedProperties(mergedParameters));
        module.setParameters(getResolvedParameters(mergedParameters));
        module.setRequiredDependencies(getResolvedRequiredDependencies());
        module.setProvidedDependencies(getResolvedProvidedDependencies());
        return module;
    }

    protected Map<String, Object> getMergedParameters() {
        String moduleName = module.getName();
        List<Map<String, Object>> parametersList = parametersChainBuilder.buildModuleChainWithoutDependencies(moduleName);
        addSingularParametersIfNecessary(parametersList);
        return PropertiesUtil.mergeProperties(parametersList);
    }

    protected Map<String, Object> getResolvedProperties(Map<String, Object> mergedParameters) {
        return new PropertiesPlaceholderResolver(propertiesResolverBuilder, dynamicResolvableParameters).resolve(module.getProperties(),
                                                                                                              mergedParameters, prefix);
    }

    protected Map<String, Object> getResolvedParameters(Map<String, Object> mergedParameters) {
        return new PropertiesPlaceholderResolver(parametersResolverBuilder, dynamicResolvableParameters).resolve(module.getParameters(),
                                                                                                              mergedParameters, prefix);
    }

    protected List<ProvidedDependency> getResolvedProvidedDependencies() {
        return module.getProvidedDependencies()
                     .stream()
                     .map(providedDependency -> getProvidedDependencyResolver(providedDependency).resolve())
                     .collect(Collectors.toList());
    }

    protected ProvidedDependencyPlaceholderResolver getProvidedDependencyResolver(ProvidedDependency providedDependency) {
        return new ProvidedDependencyPlaceholderResolver(module,
                                                         providedDependency,
                                                         prefix,
                                                         parametersChainBuilder,
                                                         propertiesResolverBuilder,
                                                         singularToPluralMapping,
                                                         dynamicResolvableParameters);
    }

    protected List<RequiredDependency> getResolvedRequiredDependencies() {
        return module.getRequiredDependencies()
                     .stream()
                     .map(requiredDependency -> getRequiredDependencyResolver(requiredDependency).resolve())
                     .collect(Collectors.toList());
    }

    protected RequiredDependencyPlaceholderResolver getRequiredDependencyResolver(RequiredDependency requiredDependency) {
        return new RequiredDependencyPlaceholderResolver(module,
                                                         requiredDependency,
                                                         prefix,
                                                         parametersChainBuilder,
                                                         propertiesResolverBuilder,
                                                         parametersResolverBuilder,
                                                         singularToPluralMapping,
                                                         dynamicResolvableParameters);
    }

}

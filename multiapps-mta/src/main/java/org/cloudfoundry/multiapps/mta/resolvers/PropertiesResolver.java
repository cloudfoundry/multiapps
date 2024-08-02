package org.cloudfoundry.multiapps.mta.resolvers;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.cloudfoundry.multiapps.common.ContentException;
import org.cloudfoundry.multiapps.mta.Messages;
import org.cloudfoundry.multiapps.mta.helpers.SimplePropertyVisitor;
import org.cloudfoundry.multiapps.mta.helpers.VisitableObject;
import org.cloudfoundry.multiapps.mta.util.DynamicParameterUtil;
import org.cloudfoundry.multiapps.mta.util.NameUtil;

public class PropertiesResolver implements SimplePropertyVisitor, Resolver<Map<String, Object>> {

    private Map<String, Object> properties;
    private String prefix;
    private ProvidedValuesResolver valuesResolver;
    private ReferencePattern referencePattern;
    private boolean isStrict;
    private ResolutionContext resolutionContext;
    private Set<String> dynamicResolvableParameters;

    public PropertiesResolver() {
        // do nothing
    }

    public PropertiesResolver(Map<String, Object> properties, ProvidedValuesResolver valuesResolver, ReferencePattern referencePattern,
                              String prefix, Set<String> dynamicResolvableParameters) {
        this(properties, valuesResolver, referencePattern, prefix, true, dynamicResolvableParameters);
    }

    public PropertiesResolver(Map<String, Object> properties, ProvidedValuesResolver valuesResolver, ReferencePattern referencePattern,
                              String prefix, boolean isStrict, Set<String> dynamicResolvableParameters) {
        this.properties = properties;
        this.prefix = prefix;
        this.referencePattern = referencePattern;
        this.isStrict = isStrict;
        this.valuesResolver = valuesResolver;
        this.dynamicResolvableParameters = dynamicResolvableParameters;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> resolve() {
        return (Map<String, Object>) resolve(properties);
    }

    private Object resolve(Object value) {
        return new VisitableObject(value).accept(this);
    }

    private Object resolve(String key, Object value) {
        return new VisitableObject(value).accept(key, this);
    }

    @Override
    public Object visit(String key, String value) {
        if (resolutionContext != null) {
            resolutionContext.addToReferencedKeys(key);
        }
        return resolveReferences(key, value);
    }

    private Object resolveReferences(String key, String value) {
        List<Reference> references = detectReferences(value);
        if (isSimpleReference(value, references)) {
            return resolveReferenceInContext(key, references.get(0));
        }
        StringBuilder result = new StringBuilder(value);
        for (Reference reference : references) {
            result = resolveReferenceInPlace(key, result, reference);
            if (result == null) {
                return null;
            }
        }
        return result.toString();
    }

    private StringBuilder resolveReferenceInPlace(String key, StringBuilder value, Reference reference) {
        String matchedPattern = reference.getMatchedPattern();
        int patternStartIndex = value.indexOf(matchedPattern);
        Object resolvedReference = resolveReferenceInContext(key, reference, resolutionContext != null);
        if (resolvedReference != null) {
            return value.replace(patternStartIndex, patternStartIndex + matchedPattern.length(), resolvedReference.toString());
        }
        return null;
    }

    private boolean isSimpleReference(String value, List<Reference> references) {
        return references.size() == 1 && value.length() == references.get(0)
                                                                     .getMatchedPattern()
                                                                     .length();
    }

    protected Object resolveReferenceInContext(String key, Reference reference) {
        return resolveReferenceInContext(key, reference, false);
    }

    protected Object resolveReferenceInContext(String key, Reference reference, boolean shouldBackupContext) {
        boolean resolutionContextWasCreated = false;
        HashSet<String> contextKeysBackup = null;
        if (resolutionContext == null) {
            resolutionContext = new ResolutionContext(NameUtil.getPrefixedName(prefix, key));
            resolutionContextWasCreated = true;
        } else if (shouldBackupContext) {
            // if multiple refs are resolved sequentially in a value - backup and revert context after each ref
            contextKeysBackup = new HashSet<>(resolutionContext.referencedKeys);
        }
        Object resolvedValue = resolveReference(reference);
        if (resolutionContextWasCreated) {
            resolutionContext = null;
        } else if (contextKeysBackup != null) {
            resolutionContext.setReferencedKeys(contextKeysBackup);
        }
        return resolvedValue;
    }

    private Object resolveReference(Reference reference) {
        String referenceKey = reference.getKey();
        Map<String, Object> replacementValues = valuesResolver.resolveProvidedValues(reference.getDependencyName());

        boolean canResolveInDepth = referencePattern.hasDepthOfReference() && referenceKey.contains("/");
        if (!referenceResolutionIsPossible(referenceKey, replacementValues, canResolveInDepth)) {
            return getObjectForLateResolveIfPresent(reference);
        }
        // always try to resolve as a flat reference first
        Object referencedProperty = replacementValues.get(referenceKey);

        if (referencedProperty == null && canResolveInDepth) {
            referencedProperty = resolveReferenceInDepth(reference, replacementValues);
        }

        String referencedPropertyKeyWithSuffix = getReferencedPropertyKeyWithSuffix(reference);
        return resolve(referencedPropertyKeyWithSuffix, referencedProperty);
    }

    private boolean referenceResolutionIsPossible(String referenceKey, Map<String, Object> referencedProperties,
                                                  boolean canResolveInDepth) {
        if (referencedProperties == null) {
            return false;
        }
        return referencedProperties.containsKey(referenceKey) || canResolveInDepth;
    }

    private Object getObjectForLateResolveIfPresent(Reference reference) {
        String referenceKey = reference.getKey();
        if (isStrict && !dynamicResolvableParameters.contains(referenceKey)) {
            throw new ContentException(Messages.UNABLE_TO_RESOLVE, NameUtil.getPrefixedName(prefix, referenceKey));
        }
        if (dynamicResolvableParameters.contains(referenceKey) && prefix != null) {
            return MessageFormat.format(DynamicParameterUtil.PATTERN_FOR_DYNAMIC_PARAMETERS, prefix, reference.getKey());
        }
        return null;
    }

    protected Object resolveReferenceInDepth(Reference reference, Map<String, Object> referencedProperties) {
        String deepReferenceKey = reference.getKey();
        Matcher referencePartsMatcher = Pattern.compile("([^/]+)/?")
                                               .matcher(deepReferenceKey);

        if (!referencePartsMatcher.find()) {
            return getObjectForLateResolveIfPresent(reference);
        }

        Object currentProperty = referencedProperties.get(referencePartsMatcher.group(1));
        String keyPart = "";

        while (referencePartsMatcher.find()) {
            keyPart = addOldKeyAsPrefixIfUnresolved(keyPart, referencePartsMatcher.group(1));

            if (currentProperty instanceof Collection) {
                currentProperty = resolveKeyInIterable(keyPart, (Collection<?>) currentProperty, deepReferenceKey);
                keyPart = "";
            } else if (currentProperty instanceof Map) {
                @SuppressWarnings("unchecked")
                Object subProperty = MapUtils.getObject((Map<Object, ?>) currentProperty, keyPart);
                if (subProperty != null) {
                    currentProperty = subProperty;
                    keyPart = "";
                }
            } else {
                throw new ContentException(Messages.UNABLE_TO_RESOLVE, NameUtil.getPrefixedName(prefix, deepReferenceKey));
            }
        }

        if (!keyPart.isEmpty()) {
            throw new ContentException(Messages.UNABLE_TO_RESOLVE, NameUtil.getPrefixedName(prefix, deepReferenceKey));
        }

        return currentProperty;
    }

    private String addOldKeyAsPrefixIfUnresolved(String oldKey, String newKey) {
        if (oldKey.isEmpty()) {
            return newKey;
        }
        return oldKey + "/" + newKey;
    }

    private Object resolveKeyInIterable(String key, Collection<?> listOfProperties, String longKey) {
        if (StringUtils.isNumeric(key)) {
            try {
                return IterableUtils.get(listOfProperties, Integer.parseInt(key));
            } catch (IndexOutOfBoundsException e) {
                throw new ContentException(e, Messages.UNABLE_TO_RESOLVE, NameUtil.getPrefixedName(prefix, longKey));
            }
        }
        throw new ContentException(Messages.UNABLE_TO_RESOLVE, NameUtil.getPrefixedName(prefix, longKey));
    }

    private String getReferencedPropertyKeyWithSuffix(Reference reference) {
        if (reference.getDependencyName() != null) {
            return NameUtil.getPrefixedName(reference.getDependencyName(), reference.getKey());
        }
        return reference.getKey();
    }

    private List<Reference> detectReferences(String line) {
        return referencePattern.match(line);
    }

    private static class ResolutionContext {

        private final String rootKey;
        private Set<String> referencedKeys = new HashSet<>();

        public ResolutionContext(String rootKey) {
            this.rootKey = rootKey;
        }

        public void addToReferencedKeys(String key) {
            if (referencedKeys.contains(key)) {
                throw new ContentException(Messages.DETECTED_CIRCULAR_REFERENCE, rootKey);
            }
            referencedKeys.add(key);
        }

        public void setReferencedKeys(Set<String> keys) {
            this.referencedKeys = keys;
        }

    }

}
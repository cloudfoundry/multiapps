package com.sap.cloud.lm.sl.mta.resolvers;

import static com.sap.cloud.lm.sl.mta.util.ValidatorUtil.getPrefixedName;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.mta.helpers.SimplePropertyVisitor;
import com.sap.cloud.lm.sl.mta.helpers.VisitableObject;
import com.sap.cloud.lm.sl.mta.message.Messages;

public class PropertiesResolver implements SimplePropertyVisitor, Resolver<Map<String, Object>, ContentException> {

    private Map<String, Object> properties;
    private String prefix;
    private ProvidedValuesResolver<? extends ContentException> valuesResolver;
    private String origin;
    private ReferencePattern referencePattern;
    private boolean isStrict;
    private boolean isOrigin;
    private Set<String> seenKeys = new TreeSet<String>();

    public PropertiesResolver() {
        // do nothing
    }

    public PropertiesResolver(Map<String, Object> properties, ProvidedValuesResolver<? extends ContentException> valuesResolver,
        ReferencePattern referencePattern, String prefix) {
        this(properties, valuesResolver, referencePattern, prefix, true);
    }

    public PropertiesResolver(Map<String, Object> properties, ProvidedValuesResolver<? extends ContentException> valuesResolver,
        ReferencePattern referencePattern, String prefix, boolean isStrict) {
        this.properties = properties;
        this.prefix = prefix;
        this.referencePattern = referencePattern;
        this.isStrict = isStrict;
        this.isOrigin = true;
        this.valuesResolver = valuesResolver;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> resolve() throws ContentException {
        return (Map<String, Object>) resolve(properties);
    }

    private Object resolve(Object value) throws ContentException {
        return new VisitableObject(value).accept(this);
    }

    private Object resolve(String key, Object value) throws ContentException {
        return new VisitableObject(value).accept(key, this);
    }

    @Override
    public Object visit(String key, String value) throws ContentException {
        if (seenKeys.contains(key)) {
            throw new ContentException(Messages.DETECTED_CIRCULAR_REFERENCE, getPrefixedName(prefix, origin));
        }
        boolean usedToBeOrigin = isOrigin;
        if (usedToBeOrigin) {
            isOrigin = !isOrigin;
            origin = key;
        } else {
            seenKeys.add(key);
        }
        Object resolved = resolveReferences(value);
        if (usedToBeOrigin) {
            isOrigin = !isOrigin;
            seenKeys.clear();
        }
        return resolved;
    }

    private Object resolveReferences(String value) throws ContentException {
        List<Reference> references = detectReferences(value);
        if (isSimpleReference(value, references)) {
            return resolveReference(references.get(0));
        }
        StringBuilder result = new StringBuilder(value);
        for (Reference referrence : references) {
            result = resolveReferenceInPlace(result, referrence);
            if (result == null) {
                return null;
            }
        }
        return result.toString();
    }

    private StringBuilder resolveReferenceInPlace(StringBuilder value, Reference reference) throws ContentException {
        String matchedPattern = reference.getMatchedPattern();
        int patternStartIndex = value.indexOf(matchedPattern);
        Object resolvedReference = resolveReference(reference);
        if (resolvedReference != null) {
            return value.replace(patternStartIndex, patternStartIndex + matchedPattern.length(), resolvedReference.toString());
        }
        return null;
    }

    private boolean isSimpleReference(String value, List<Reference> references) {
        return references.size() == 1 && value.length() == references.get(0).getMatchedPattern().length();
    }

    protected Object resolveReference(Reference reference) throws ContentException {
        String referenceName = reference.getPropertyName();
        Map<String, Object> replacementValues = valuesResolver.resolveProvidedValues(reference.getDependencyName());
        if (replacementValues == null || !replacementValues.containsKey(referenceName)) {
            if (!isStrict) {
                return null;
            }
            throw new ContentException(Messages.UNABLE_TO_RESOLVE, getPrefixedName(prefix, referenceName));
        }
        String key = null;
        if (reference.getDependencyName() != null) {
            key = getPrefixedName(reference.getDependencyName(), reference.getPropertyName());
        } else {
            key = reference.getPropertyName();
        }
        return resolve(key, replacementValues.get(referenceName));
    }

    private List<Reference> detectReferences(String line) {
        return referencePattern.match(line);
    }
}
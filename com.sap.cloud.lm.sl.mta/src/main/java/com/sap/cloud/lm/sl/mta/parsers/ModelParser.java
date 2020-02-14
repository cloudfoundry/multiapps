package com.sap.cloud.lm.sl.mta.parsers;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.Messages;
import com.sap.cloud.lm.sl.mta.model.Metadata;
import com.sap.cloud.lm.sl.mta.model.Version;
import com.sap.cloud.lm.sl.mta.schema.Element;
import com.sap.cloud.lm.sl.mta.schema.MapElement;
import com.sap.cloud.lm.sl.mta.util.MetadataUpdater;

public abstract class ModelParser<T> implements Parser<T> {

    protected String processedObjectName;
    protected Map<String, Object> source;
    protected Map<String, Object> uniqueElementsCache = new HashMap<>();
    protected Set<String> usedValues = new HashSet<>();
    protected MapElement schema;

    public ModelParser(String processedObjectName, MapElement schema, Map<String, Object> source) {
        this.processedObjectName = processedObjectName;
        this.schema = schema;
        this.source = source;
    }

    public ModelParser<T> setUsedValues(Set<String> usedValues) {
        this.usedValues = usedValues;
        return this;
    }

    protected String getSchemaVersion(String key) {
        // If the user specified a partial schema version like '2' or '2.0' without quoting it, the YAML parser would parse it to Integer or
        // Double, which is why the type of possiblyPartialSchemaVersion needs to be Object.
        Object schemaVersion = getElement(key);
        return Version.parseVersion(schemaVersion.toString())
                      .toString();
    }

    protected String getStringElement(String key) {
        return (String) getElement(key);
    }

    protected Boolean getBooleanElement(String key) {
        return (Boolean) getElement(key);
    }

    protected Object getElement(String key) {
        Element element = schema.getMap()
                                .get(key);
        if (element == null) {
            throw new ParsingException(Messages.COULD_NOT_FIND_ELEMENT_IN_SCHEMA, key, processedObjectName);
        }
        if (element.isRequired()) {
            if (element.isUnique()) {
                return getRequiredUniqueStringValue(key);
            }
            return getRequiredValue(key);
        }
        return source.get(key);
    }

    /**
     * Gets a required and unique value from the source. The uniqueness of the value is checked only the first time this method is called.
     * Every other call gets the value from a cache so a NAME_NOT_UNIQUE ParsingException can be thrown only on the first attempt to get the
     * value.
     * 
     * @param key The key whose associated value is to be returned
     * @return The value to which the specified key is mapped
     * @throws ParsingException If the value is not unique or it is missing
     */
    private String getRequiredUniqueStringValue(String key) {
        if (uniqueElementsCache.get(key) == null) {
            uniqueElementsCache.put(key, getRequiredUniqueValue(key));
        }
        return uniqueElementsCache.get(key)
                                  .toString();
    }

    @SuppressWarnings("unchecked")
    protected List<String> getListElement(String key) {
        return (List<String>) source.get(key);
    }

    @SuppressWarnings("unchecked")
    protected <E> List<E> getListElement(String key, ListParser<E> builder) {
        Object list = source.get(key);
        if (list != null) {
            return builder.setSource(((List<Object>) list))
                          .parse();
        }
        return Collections.emptyList();
    }

    @SuppressWarnings("unchecked")
    protected <K, V> Map<K, V> getMapElement(String key) {
        return (Map<K, V>) source.get(key);
    }

    protected Metadata getMetadata(String metadataYamlElementName, Map<String, Object> metadataDescribedProperties) {
        Map<String, Map<String, Object>> metadataMapElement = getMapElement(metadataYamlElementName);
        MetadataUpdater propertiesUpdater = new MetadataUpdater(metadataDescribedProperties);
        return propertiesUpdater.getUpdatedMetadata(metadataMapElement);
    }

    private Object getRequiredUniqueValue(String key) {
        Object value = getRequiredValue(key);
        validateUniqueness(key, value, usedValues);
        return value;
    }

    private Object getRequiredValue(String key) {
        Object value = source.get(key);
        if (value == null) {
            throw new ParsingException(Messages.REQUIRED_ELEMENT_IS_MISSING, key, processedObjectName);
        }
        return value;
    }

    @SuppressWarnings("unchecked")
    private <V> void validateUniqueness(String key, Object value, Set<V> usedValues) {
        if (usedValues.contains(value)) {
            throw new ParsingException(Messages.VALUE_NOT_UNIQUE, value, key, processedObjectName);
        }
        usedValues.add((V) value);
    }

}

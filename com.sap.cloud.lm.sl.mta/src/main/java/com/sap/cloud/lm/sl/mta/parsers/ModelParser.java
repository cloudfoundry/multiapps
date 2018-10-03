package com.sap.cloud.lm.sl.mta.parsers;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.message.Messages;
import com.sap.cloud.lm.sl.mta.model.Metadata;
import com.sap.cloud.lm.sl.mta.model.Version;
import com.sap.cloud.lm.sl.mta.schema.Element;
import com.sap.cloud.lm.sl.mta.schema.MapElement;
import com.sap.cloud.lm.sl.mta.util.MetadataUpdater;
import com.sap.cloud.lm.sl.mta.util.ParserUtil;

public abstract class ModelParser<T> implements Parser<T> {

    protected String processedObjectName;
    protected Map<String, Object> source;
    protected Map<String, Object> uniqueElementsCache = new HashMap<String, Object>();
    protected Set<? extends Object> usedValues = new HashSet<Object>();
    protected MapElement schema;

    public ModelParser(String processedObjectName, MapElement schema, Map<String, Object> source) {
        this.processedObjectName = processedObjectName;
        this.schema = schema;
        this.source = source;
    }

    public ModelParser<T> setUsedValues(Set<? extends Object> usedValues) {
        this.usedValues = usedValues;
        return this;
    }

    protected String getSchemaVersion(String key) throws ParsingException {
        // If the user specified a partial schema version like '2' or '2.0' without quoting it, the YAML parser would parse it to Integer or
        // Double, which is why the type of possiblyPartialSchemaVersion needs to be Object.
        Object schemaVersion = getElement(key);
        return Version.parseVersion(schemaVersion.toString())
            .toString();
    }

    protected String getStringElement(String key) throws ParsingException {
        return (String) getElement(key);
    }

    protected Boolean getBooleanElement(String key) throws ParsingException {
        return (Boolean) getElement(key);
    }

    protected Object getElement(String key) throws ParsingException {
        Element element = schema.getMap()
            .get(key);
        if (element == null) {
            throw new ParsingException(Messages.COULD_NOT_FIND_ELEMENT_IN_SCHEMA, key, processedObjectName);
        }
        if (element.isRequired()) {
            if (element.isUnique()) {
                return getRequiredUniqueStringValue(key);
            }
            return ParserUtil.getRequiredValue(source, key, processedObjectName);
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
    private String getRequiredUniqueStringValue(String key) throws ParsingException {
        if (uniqueElementsCache.get(key) == null) {
            uniqueElementsCache.put(key, ParserUtil.getRequiredUniqueValue(source, usedValues, key, processedObjectName));
        }
        return uniqueElementsCache.get(key)
            .toString();
    }

    @SuppressWarnings("unchecked")
    protected List<String> getListElement(String key) {
        return (List<String>) source.get(key);
    }

    @SuppressWarnings("unchecked")
    protected <E> List<E> getListElement(String key, ListParser<E> builder) throws ParsingException {
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
}

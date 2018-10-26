package com.sap.cloud.lm.sl.mta.schema;

import static com.sap.cloud.lm.sl.mta.util.ValidatorUtil.getPrefixedName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.common.util.MiscUtil;
import com.sap.cloud.lm.sl.mta.message.Messages;

public class SchemaValidator {

    private final Element schema;

    public SchemaValidator(Element schema) {
        this.schema = schema;
    }

    public void validate(Map<String, Object> map) throws ParsingException {
        validate(map, schema, "", new HashMap<String, Set<Object>>());
    }

    public void validate(List<Object> list) throws ParsingException {
        validate(list, schema, "", new HashMap<String, Set<Object>>());
    }

    @SuppressWarnings("unchecked")
    private static void validate(Object object, Element schema, String prefix, Map<String, Set<Object>> uniqueValuesMap) {
        checkNull(object, prefix);
        checkType(object, schema, prefix);

        if (schema instanceof MapElement) {
            validate((Map<String, Object>) object, (MapElement) schema, prefix, uniqueValuesMap);
        } else if (schema instanceof ListElement) {
            validate((List<Object>) object, (ListElement) schema, prefix, new HashMap<String, Set<Object>>());
        } else {
            validate(object, schema, prefix);
        }
    }

    private static void validate(Map<String, Object> map, MapElement schema, String prefix, Map<String, Set<Object>> uniqueValuesMap) {
        // Validate existing keys:
        for (String key : map.keySet()) {
            Element element = schema.getMap()
                .get(key);
            String elementPrefix = getPrefixedName(prefix, key);
            if (element == null) {
                throw new ParsingException(Messages.INVALID_KEY, elementPrefix);
            }
            Object object = map.get(key);
            validate(object, element, elementPrefix, uniqueValuesMap);
        }

        // Check for non-existing required keys:
        for (String key : schema.getMap()
            .keySet()) {
            Element element = schema.getMap()
                .get(key);
            if (element.isRequired() && !map.containsKey(key)) {
                throw new ParsingException(Messages.MISSING_REQUIRED_KEY, getPrefixedName(prefix, key));
            }
        }
    }

    private static void validate(List<Object> list, ListElement schema, String prefix, Map<String, Set<Object>> uniqueValuesMap) {
        Element element = schema.getElement();
        for (int i = 0; i < list.size(); i++) {
            String elementPrefix = getPrefixedName(prefix, Integer.toString(i));
            Object value = list.get(i);
            validate(value, element, elementPrefix, uniqueValuesMap);
        }
    }

    private static void validate(Object object, Element schema, String objectName) {
        if (schema.getType() == String.class) {
            validateStringElement(object, schema, objectName);
        }
    }

    private static void validateStringElement(Object object, Element schema, String objectName) {
        if (schema.getPattern() != null) {
            String value = String.valueOf(object);

            if (value.length() > schema.getMaxLength()) {
                throw new ParsingException(Messages.VALUE_TOO_LONG, objectName, schema.getMaxLength());
            }

            String pattern = schema.getPattern();
            if (!value.matches(pattern)) {
                throw new ParsingException(Messages.INVALID_STRING_VALUE_FOR_KEY, objectName,
                    MiscUtil.outlineProblematicCharacter(pattern, value));
            }
        }
    }

    private static void checkNull(Object object, String prefix) {
        if (object == null) {
            if (!isRootElement(prefix)) {
                throw new ParsingException(Messages.NULL_VALUE_FOR_KEY, prefix);
            }
            throw new ParsingException(Messages.NULL_CONTENT);
        }
    }

    private static void checkType(Object object, Element element, String prefix) {
        if (!element.getType()
            .isInstance(object)) {
            if (!isRootElement(prefix)) {
                throw new ParsingException(Messages.INVALID_TYPE_FOR_KEY, prefix, element.getType()
                    .getSimpleName(),
                    object.getClass()
                        .getSimpleName());
            }
            throw new ParsingException(Messages.INVALID_CONTENT_TYPE, element.getType()
                .getSimpleName(),
                object.getClass()
                    .getSimpleName());
        }
    }

    private static boolean isRootElement(String prefix) {
        return prefix == null || prefix.isEmpty();
    }

}

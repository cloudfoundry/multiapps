package org.cloudfoundry.multiapps.mta.schema;

import static org.cloudfoundry.multiapps.mta.util.ValidatorUtil.getPrefixedName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.cloudfoundry.multiapps.common.ParsingException;
import org.cloudfoundry.multiapps.common.util.MiscUtil;
import org.cloudfoundry.multiapps.mta.Messages;

public class SchemaValidator {

    private final Element schema;

    public SchemaValidator(Element schema) {
        this.schema = schema;
    }

    public void validate(Map<String, Object> map) throws ParsingException {
        validate(map, schema, "", new HashMap<>());
    }

    public void validate(List<Object> list) throws ParsingException {
        validate(list, schema, "", new HashMap<>());
    }

    @SuppressWarnings("unchecked")
    private static void validate(Object object, Element schema, String prefix, Map<String, Set<Object>> uniqueValuesMap) {
        checkNull(object, prefix);
        checkType(object, schema, prefix);

        if (schema instanceof MapElement) {
            validate((Map<String, Object>) object, (MapElement) schema, prefix, uniqueValuesMap);
        } else if (schema instanceof ListElement) {
            validate((List<Object>) object, (ListElement) schema, prefix, new HashMap<>());
        } else {
            validate(object, schema, prefix);
        }
    }

    private static void validate(Map<String, Object> map, MapElement schema, String prefix, Map<String, Set<Object>> uniqueValuesMap) {
        // Validate existing keys:
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Element element = schema.getMap()
                                    .get(entry.getKey());
            String elementPrefix = getPrefixedName(prefix, entry.getKey());
            if (element == null) {
                continue;
            }
            Object object = entry.getValue();
            validate(object, element, elementPrefix, uniqueValuesMap);
        }

        // Check for non-existing required keys:
        for (Map.Entry<String, Element> entry : schema.getMap()
                                                      .entrySet()) {
            Element element = entry.getValue();
            if (element.isRequired() && !map.containsKey(entry.getKey())) {
                throw new ParsingException(Messages.MISSING_REQUIRED_KEY, getPrefixedName(prefix, entry.getKey()));
            }
        }
    }

    private static void validate(List<Object> list, ListElement schema, String prefix, Map<String, Set<Object>> uniqueValuesMap) {
        Element element = schema.getElement();
        int i = 0;
        for (Object value : list) {
            validate(value, element, getPrefixedName(prefix, Integer.toString(i++)), uniqueValuesMap);
        }
    }

    private static void validate(Object object, Element schema, String objectName) {
        if (schema.getType()
                  .equals(String.class)) {
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
                throw new ParsingException(Messages.INVALID_STRING_VALUE_FOR_KEY,
                                           objectName,
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
                throw new ParsingException(Messages.INVALID_TYPE_FOR_KEY,
                                           prefix,
                                           element.getType()
                                                  .getSimpleName(),
                                           object.getClass()
                                                 .getSimpleName());
            }
            throw new ParsingException(Messages.INVALID_CONTENT_TYPE,
                                       element.getType()
                                              .getSimpleName(),
                                       object.getClass()
                                             .getSimpleName());
        }
    }

    private static boolean isRootElement(String prefix) {
        return StringUtils.isEmpty(prefix);
    }

}

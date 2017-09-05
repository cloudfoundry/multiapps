package com.sap.cloud.lm.sl.mta.util;

import java.util.Map;
import java.util.Set;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.mta.message.Messages;

public class ParserUtil {

    public static Object getRequiredUniqueValue(Map<String, Object> map, Set<? extends Object> usedValues, String key, String object)
        throws ParsingException {
        Object value = getRequiredValue(map, key, object);
        checkUniqueValue(value, key, usedValues, object);
        return value;
    }

    public static Object getRequiredValue(Map<String, Object> map, String key, String object) throws ParsingException {
        Object value = map.get(key);
        if (value == null) {
            throw new ParsingException(Messages.REQUIRED_ELEMENT_IS_MISSING, key, object);
        }
        return value;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Object> void checkUniqueValue(Object value, String key, Set<T> usedValues, String object) throws ParsingException {
        if (usedValues.contains(value)) {
            throw new ParsingException(Messages.VALUE_NOT_UNIQUE, value, key, object);
        }
        usedValues.add((T) value);
    }

}

package com.sap.cloud.lm.sl.mta.util;

import org.apache.commons.lang3.StringUtils;

import com.sap.cloud.lm.sl.common.ContentException;
import com.sap.cloud.lm.sl.common.util.CommonUtil;
import com.sap.cloud.lm.sl.mta.message.Messages;

public class ValidatorUtil {

    public static final String DEFAULT_SEPARATOR = "#";

    public static void validateModifiableElements(String elementType, String elementPrefix, String containerName, String key, Object value,
        Object parentValue) throws ContentException {
        if (!CommonUtil.isNullOrEmpty(parentValue) && !parentValue.equals(value)) {
            throw new ContentException(Messages.CANNOT_MODIFY_ELEMENT, elementType, getPrefixedName(elementPrefix, key), containerName);
        }
    }

    public static String getPrefixedName(String prefix, String name, String separator) {
        return (!StringUtils.isEmpty(prefix) ? prefix + separator : "") + name;
    }

    public static String getPrefixedName(String prefix, String name) {
        return getPrefixedName(prefix, name, DEFAULT_SEPARATOR);
    }

}

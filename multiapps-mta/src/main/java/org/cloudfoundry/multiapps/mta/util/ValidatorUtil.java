package org.cloudfoundry.multiapps.mta.util;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.cloudfoundry.multiapps.common.ContentException;
import org.cloudfoundry.multiapps.mta.Messages;

public class ValidatorUtil {

    public static final String DEFAULT_SEPARATOR = "#";

    private ValidatorUtil() {
    }

    public static void validateModifiableElements(String elementType, String elementPrefix, String containerName, String key, Object value,
                                                  Object parentValue)
        throws ContentException {
        if (!ObjectUtils.isEmpty(parentValue) && !parentValue.equals(value)) {
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

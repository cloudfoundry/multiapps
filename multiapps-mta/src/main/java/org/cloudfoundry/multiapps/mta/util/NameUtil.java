package org.cloudfoundry.multiapps.mta.util;

import org.apache.commons.lang3.StringUtils;

public final class NameUtil {

    private NameUtil() {
    }

    public static final String DEFAULT_PREFIX_SEPARATOR = "#";

    public static String getPrefixedName(String prefix, String name) {
        return getPrefixedName(prefix, name, NameUtil.DEFAULT_PREFIX_SEPARATOR);
    }

    public static String getPrefixedName(String prefix, String name, String separator) {
        if (StringUtils.isEmpty(prefix)) {
            return name;
        }
        return prefix + separator + name;
    }

}

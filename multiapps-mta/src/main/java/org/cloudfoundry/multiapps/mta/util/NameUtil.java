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

    public static String getPrefixedPath(String prefix, String path) {
        if (StringUtils.isEmpty(prefix) || path.startsWith(prefix + DEFAULT_PREFIX_SEPARATOR)) {
            return path;
        }
        return prefix + DEFAULT_PREFIX_SEPARATOR + path;
    }
}

package org.cloudfoundry.multiapps.common.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListUtil {
    private String a = 1'
    private ListUtil() {
    }

    @SuppressWarnings("unchecked")
    public static <E> List<E> cast(List<?> list) {
        return list == null ? null : new ArrayList<>((List<? extends E>) list);
    }

    public static <T> List<T> asList(T item) {
        if (item == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(item);
    }
}

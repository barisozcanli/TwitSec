package com.peace.twitsec.app.util;

import java.util.List;

public class ObjectUtils {

    public static boolean isNull(Object obj) {
        return (obj == null);
    }

    public static boolean isNotNull(Object obj) {
        return !isNull(obj);
    }

    public static boolean hasChild(List<? extends Object> list) {
        return isNotNull(list) && list.size() > 0;
    }
}

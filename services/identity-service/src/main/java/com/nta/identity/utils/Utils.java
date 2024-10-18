package com.nta.identity.utils;

import org.springframework.stereotype.Component;

@Component
public class Utils {
    public <T extends Enum<T>> String writeEnumValuesAsString(final Class<T> myEnumClass) {
        StringBuilder jsonBuilder = new StringBuilder("[");

        T[] enumConstants = myEnumClass.getEnumConstants();
        if (enumConstants.length > 0) {
            for (T type : enumConstants) {
                jsonBuilder.append(type.name()).append(", ");
            }
            // Xóa dấu phẩy và khoảng trắng cuối cùng
            jsonBuilder.setLength(jsonBuilder.length() - 2);
        }

        return jsonBuilder.append("]").toString();
    }
}

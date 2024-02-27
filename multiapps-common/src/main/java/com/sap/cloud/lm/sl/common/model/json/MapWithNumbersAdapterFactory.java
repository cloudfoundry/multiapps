package com.sap.cloud.lm.sl.common.model.json;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 * Converts numbers from JSON Map element to Integer not Double <br>
 * This is needed because GSON converts all numbers to Double by default <br>
 * Adapter must be applied over Map objects only, not to List or other complex collections
 */
public class MapWithNumbersAdapterFactory implements TypeAdapterFactory {

    private static final Type PROPERTY_KEY_TYPE = new TypeToken<String>() {
    }.getType();

    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(final Gson gson, TypeToken<T> typeToken) {
        if (!isPropertyMap(typeToken)) {
            return null;
        }
        return (TypeAdapter<T>) new MapWithNumbersAdapter(gson.getAdapter(Object.class)).nullSafe();
    }

    private boolean isPropertyMap(TypeToken<?> typeToken) {
        if (!Map.class.isAssignableFrom(typeToken.getRawType())) {
            return false;
        }
        if (!(typeToken.getType() instanceof ParameterizedType)) {
            return false;
        }
        Type keyType = ((ParameterizedType) typeToken.getType()).getActualTypeArguments()[0];
        return PROPERTY_KEY_TYPE.equals(keyType);
    }

}

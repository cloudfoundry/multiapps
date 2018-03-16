package com.sap.cloud.lm.sl.common.model.json;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

/** Converts numbers from JSON Map element to Integer not Double <br>
 * This is needed because GSON converts all numbers to Double by default <br>
 * Adapter must be applied over Map objects only, not to List or other complex collections
 */
public class MapWithNumbersAdapterFactory implements TypeAdapterFactory {

    private static final Type PROPERTY_KEY_TYPE = new TypeToken<String>() {
    }.getType();

    @Override
    public <T> TypeAdapter<T> create(final Gson gson, TypeToken<T> typeToken) {
        if (!isPropertyMap(typeToken)) {
            return null;
        }
        final TypeAdapter<Object> objectDelegate = gson.getAdapter(Object.class);

        final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, typeToken);

        return new TypeAdapter<T>() {

            @SuppressWarnings("unchecked")
            @Override
            public T read(JsonReader in) throws IOException {
                Map<String, Object> result = new TreeMap<String, Object>();
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                    return null;
                }

                in.beginObject();
                while (in.hasNext()) {
                    parseProperty(in, result);
                }
                in.endObject();
                return (T) result;
            }

            private void parseProperty(JsonReader in, Map<String, Object> result) throws IOException {
                String key = in.nextName();
                result.put(key, parseValue(objectDelegate.read(in)));
            }

            private Object parseValue(Object value) {
                if (value instanceof Double) {
                    return attemptToCastToNumber((Double) value);
                } else if (value instanceof List) {
                    List<?> list = (List<?>) value;
                    List<Object> result = new ArrayList<Object>();
                    for (Object innerValue : list) {
                        result.add(parseValue(innerValue));
                    }
                    return result;
                } else if (value instanceof Map) {
                    Map<?, ?> map = (Map<?, ?>) value;
                    Map<Object, Object> result = new TreeMap<Object, Object>();
                    for (Object key : map.keySet()) {
                        result.put(key, parseValue(map.get(key)));
                    }
                    return result;
                }
                return value;
            }

            private Number attemptToCastToNumber(Double value) {
                if (isInteger(value)) {
                    if (Double.compare(value, (double) Integer.MAX_VALUE) < 0) {
                        return value.intValue();
                    }
                    if (Double.compare(value, (double) Long.MAX_VALUE) < 0) {
                        return value.longValue();
                    }
                }
                return value;
            }

            private boolean isInteger(Double value) {
                return value % 1 == 0;
            }

            @Override
            public void write(JsonWriter out, T object) throws IOException {
                delegate.write(out, object);
            }

        };
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

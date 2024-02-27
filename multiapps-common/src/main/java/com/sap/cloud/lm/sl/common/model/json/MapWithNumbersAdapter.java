package com.sap.cloud.lm.sl.common.model.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class MapWithNumbersAdapter extends TypeAdapter<Map<String, Object>> {

    private final TypeAdapter<Object> delegate;

    public MapWithNumbersAdapter(TypeAdapter<Object> delegate) {
        this.delegate = delegate;
    }

    @Override
    public Map<String, Object> read(JsonReader in) throws IOException {
        Map<String, Object> result = new TreeMap<>();
        in.beginObject();
        while (in.hasNext()) {
            parseProperty(in, result);
        }
        in.endObject();
        return result;
    }

    private void parseProperty(JsonReader in, Map<String, Object> result) throws IOException {
        String key = in.nextName();
        result.put(key, parseValue(delegate.read(in)));
    }

    private Object parseValue(Object value) {
        if (value instanceof Double) {
            return attemptToCastToNumber((Double) value);
        } else if (value instanceof List) {
            List<?> list = (List<?>) value;
            List<Object> result = new ArrayList<>();
            for (Object innerValue : list) {
                result.add(parseValue(innerValue));
            }
            return result;
        } else if (value instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) value;
            Map<Object, Object> result = new TreeMap<>();
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
    public void write(JsonWriter out, Map<String, Object> object) throws IOException {
        delegate.write(out, object);
    }

}

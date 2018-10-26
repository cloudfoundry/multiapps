package com.sap.cloud.lm.sl.mta.parsers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.sap.cloud.lm.sl.common.ParsingException;
import com.sap.cloud.lm.sl.common.util.ListUtil;

public abstract class ListParser<T> implements Parser<List<T>> {

    protected List<Map<String, Object>> source = Collections.emptyList();

    public ListParser<T> setSource(List<Object> source) {
        this.source = ListUtil.cast(source);
        return this;
    }

    @Override
    public List<T> parse() throws ParsingException {
        List<T> result = new ArrayList<T>();
        for (Map<String, Object> item : source) {
            result.add(parseItem(item));
        }
        return Collections.unmodifiableList(result);
    }

    protected abstract T parseItem(Map<String, Object> map);

}

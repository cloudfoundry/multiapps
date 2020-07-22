package org.cloudfoundry.multiapps.mta.parsers;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.cloudfoundry.multiapps.common.ParsingException;
import org.cloudfoundry.multiapps.common.util.ListUtil;

public abstract class ListParser<T> implements Parser<List<T>> {

    protected List<Map<String, Object>> source = Collections.emptyList();

    public ListParser<T> setSource(List<Object> source) {
        this.source = ListUtil.cast(source);
        return this;
    }

    @Override
    public List<T> parse() throws ParsingException {
        return Collections.unmodifiableList(source.stream()
                                                  .map(this::parseItem)
                                                  .collect(Collectors.toList()));
    }

    protected abstract T parseItem(Map<String, Object> map);

}

package com.sap.cloud.lm.sl.slp.model.converter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.cloud.lm.sl.slp.message.Messages;
import com.sap.lmsl.slp.Parameter;
import com.sap.lmsl.slp.Parameter.Tablevalue;
import com.sap.lmsl.slp.Parameters;
import com.sap.lmsl.slp.Tuple;

public class SlpParameterTablevalueToMapTypeConverter
    implements TypeConverter<Parameter.Tablevalue, List<Map<String, Map<String, Object>>>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SlpParameterTablevalueToMapTypeConverter.class);

    @Override
    public List<Map<String, Map<String, Object>>> convertForward(Parameter.Tablevalue tablevalue) {
        if (tablevalue == null) {
            throw new IllegalArgumentException(Messages.TABLE_VALUE_CANNOT_BE_NULL);
        }

        if (tablevalue.getTuple() != null && tablevalue.getTuple().size() == 0) {
            return Collections.emptyList();
        }

        List<Map<String, Map<String, Object>>> result = new ArrayList<Map<String, Map<String, Object>>>();
        SlpParametersToMapTypeConverter tupleValueConverter = new SlpParametersToMapTypeConverter();

        for (Tuple tuple : tablevalue.getTuple()) {
            Map<String, Map<String, Object>> tupleMapHolder = new HashMap<String, Map<String, Object>>();
            if (tuple.getValue() != null) {
                Map<String, Object> tupleValues = tupleValueConverter.convertForward(tuple.getValue());
                tupleMapHolder.put(tuple.getId(), tupleValues);
            } else {
                String message = Messages.VALUE_OF_TUPLE_ELEMENT_CANNOT_BE_NULL;
                LOGGER.error(message);
                throw new IllegalArgumentException(message);
            }
            result.add(tupleMapHolder);
        }

        return result;
    }

    @Override
    public Parameter.Tablevalue convertBackward(List<Map<String, Map<String, Object>>> allTuples) {
        Parameter.Tablevalue tablevalue = new Tablevalue();
        if (allTuples == null) {
            return tablevalue;
        }

        SlpParametersToMapTypeConverter tupleValueConverter = new SlpParametersToMapTypeConverter();
        for (Map<String, Map<String, Object>> tupleEntry : allTuples) {
            Iterator<Entry<String, Map<String, Object>>> iterator = tupleEntry.entrySet().iterator();
            if (iterator.hasNext()) {
                // Key is tuple ID, value is tuple map:
                Entry<String, Map<String, Object>> tupleIdAndParamsMap = iterator.next();
                Tuple tuple = new Tuple();
                tuple.setId(tupleIdAndParamsMap.getKey());

                Parameters parameters = tupleValueConverter.convertBackward(tupleIdAndParamsMap.getValue());
                tuple.setValue(parameters);

                tablevalue.getTuple().add(tuple);
            }

            if (iterator.hasNext()) {
                String message = Messages.TUPLE_ENTRY_SHOULD_HAVE_ONLY_ONE_PARAMETER_INSIDE;
                LOGGER.error(message);
                throw new IllegalArgumentException(message);
            }
        }

        return tablevalue;
    }
}

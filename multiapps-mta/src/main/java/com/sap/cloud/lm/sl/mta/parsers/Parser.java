package com.sap.cloud.lm.sl.mta.parsers;

import com.sap.cloud.lm.sl.common.ParsingException;

public interface Parser<T> {

    T parse() throws ParsingException;

}

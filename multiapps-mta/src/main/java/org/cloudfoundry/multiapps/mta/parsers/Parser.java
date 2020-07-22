package org.cloudfoundry.multiapps.mta.parsers;

import org.cloudfoundry.multiapps.common.ParsingException;

public interface Parser<T> {

    T parse() throws ParsingException;

}

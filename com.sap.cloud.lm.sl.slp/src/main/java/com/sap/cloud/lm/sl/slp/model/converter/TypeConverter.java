package com.sap.cloud.lm.sl.slp.model.converter;

/**
 * Provides overall interface to define the conversion of SLP types to the supported Alfresco Activiti types and vice versa.
 * 
 * @author D058570
 *
 */
public interface TypeConverter<A, B> {

    B convertForward(A value);

    A convertBackward(B value);
}

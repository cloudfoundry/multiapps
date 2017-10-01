
package com.sap.lmsl.slp;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for slp_parameter_type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="slp_parameter_type">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="slp.parameter.type.SCALAR"/>
 *     &lt;enumeration value="slp.parameter.type.TUPLE"/>
 *     &lt;enumeration value="slp.parameter.type.TABLE"/>
 *     &lt;enumeration value="slp.parameter.type.FILE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "slp_parameter_type")
@XmlEnum
public enum SlpParameterType {

    @XmlEnumValue("slp.parameter.type.SCALAR")
    SLP_PARAMETER_TYPE_SCALAR("slp.parameter.type.SCALAR"),
    @XmlEnumValue("slp.parameter.type.TUPLE")
    SLP_PARAMETER_TYPE_TUPLE("slp.parameter.type.TUPLE"),
    @XmlEnumValue("slp.parameter.type.TABLE")
    SLP_PARAMETER_TYPE_TABLE("slp.parameter.type.TABLE"),
    @XmlEnumValue("slp.parameter.type.FILE")
    SLP_PARAMETER_TYPE_FILE("slp.parameter.type.FILE");
    private final String value;

    SlpParameterType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SlpParameterType fromValue(String v) {
        for (SlpParameterType c: SlpParameterType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
